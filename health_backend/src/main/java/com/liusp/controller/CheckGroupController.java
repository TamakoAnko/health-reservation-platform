package com.liusp.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.liusp.constant.MessageConstant;
import com.liusp.entity.PageResult;
import com.liusp.entity.QueryPageBean;
import com.liusp.entity.Result;
import com.liusp.pojo.CheckGroup;
import com.liusp.security.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup,  @RequestParam(required = false, value = "checkitemIds") Integer[] checkItemIds) {
        try {
            checkGroupService.add(checkGroup , checkItemIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try{
            checkGroupService.delete(id);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e){
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckGroup checkGroup ,  @RequestParam(required = false, value = "checkitemIds")Integer[] checkItemIds){
        try {
            checkGroupService.edit(checkGroup,checkItemIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();//服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //根据检查组合id查询对应的所有检查项id
    @RequestMapping("/findCheckItemIdsByCheckGroupId.do")
    public Result findCheckItemIdsByCheckGroupId(Integer id){try{
        List<Integer> checkitemIds =checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
    }catch (Exception e){
        e.printStackTrace();
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }
    }

    @RequestMapping("/findAll.do")
    public Result findALL(){
        try {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,
                    checkGroupService.findAll());
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


}
