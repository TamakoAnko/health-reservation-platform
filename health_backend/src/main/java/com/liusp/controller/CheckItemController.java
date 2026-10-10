package com.liusp.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.liusp.constant.MessageConstant;
import com.liusp.entity.PageResult;
import com.liusp.entity.QueryPageBean;
import com.liusp.entity.Result;
import com.liusp.pojo.CheckItem;
import com.liusp.security.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    //新增
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try{
            checkItemService.delete(id);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e){
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,
                    MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();//服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


    @RequestMapping("/findAll.do")
    public Result findAll(){
        try {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,
                    checkItemService.findAll());
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

}
