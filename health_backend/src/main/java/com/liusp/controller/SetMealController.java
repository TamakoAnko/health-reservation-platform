package com.liusp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liusp.constant.MessageConstant;
import com.liusp.constant.RedisConstant;
import com.liusp.entity.PageResult;
import com.liusp.entity.QueryPageBean;
import com.liusp.entity.Result;
import com.liusp.pojo.SetMeal;
import com.liusp.security.SetMealService;
import com.liusp.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Reference
    private SetMealService setMealService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        try{
            //获取原始文件名
            String originalFileName = imgFile.getOriginalFilename();
            //找到文件后缀
            int lastIndexOf = originalFileName.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFileName.substring(lastIndexOf-1);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            //调用QiniuUtils类中的upload2Qiniu方法上传文件
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            redisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //返回数据
            return result;
        }catch (Exception e){
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    @RequestMapping("/add.do")
    public Result add(@RequestBody SetMeal setMeal,  @RequestParam(required = false, value = "checkgroupIds") Integer[] checkGroupIds) {
        try {
            setMealService.add(setMeal,checkGroupIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setMealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try{
            //判断图片是否有图片，如果有，则删除图片
            SetMeal oldSetMeal = setMealService.findById(id);
            if (oldSetMeal != null && oldSetMeal.getImg() != null) {
                QiniuUtils.deleteFileFromQiniu(oldSetMeal.getImg());
            }
            setMealService.delete(id);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e){
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody SetMeal setMeal , @RequestParam(required = false, value = "checkgroupIds")Integer[] checkGroupIds){
        try {
            //判断图片是否被修改，如果被修改，则删除被修改的图片
            SetMeal oldSetMeal = setMealService.findById(setMeal.getId());
            if (oldSetMeal != null && oldSetMeal.getImg() != null
                    && setMeal.getImg() != null
                    && !oldSetMeal.getImg().equals(setMeal.getImg())) {
                QiniuUtils.deleteFileFromQiniu(oldSetMeal.getImg());
            }
            //进行修改
            setMealService.edit(setMeal,checkGroupIds);
        }catch (Exception e){
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        try {
            SetMeal setMeal = setMealService.findById(id);
            return new Result(true,
                    MessageConstant.QUERY_SETMEAL_SUCCESS, setMeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findCheckGroupIdsBySetMealId.do")
    public Result findCheckGroupIdsBySetMealId(Integer id){try{
        List<Integer> checkGroupIds = setMealService.findCheckGroupIdsBySetMealId(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,checkGroupIds);
    }catch (Exception e){
        e.printStackTrace();
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }
    }

}