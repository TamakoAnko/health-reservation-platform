package com.liusp.controller;

import com.liusp.POIUtils;
import com.liusp.constant.MessageConstant;
import com.liusp.entity.Result;
import com.liusp.pojo.OrderSetting;
import com.liusp.security.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile) {
        try {
            //调用POIUtils对象里面的方法readExcel读取excel文件(POIUtils是一个我们创建的工具类，里面有操作excel文件的方法)
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() > 0) {
                List<OrderSetting> orderSettingList = new ArrayList<>();
                for (String[] strings : list) {
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    orderSettingList.add(orderSetting);
                }
                orderSettingService.add(orderSettingList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(String date){
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //获取预约设置数据成功
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            //获取预约设置数据失败
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate.do")
    public Result editNumberByOrder(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.editNumberByDate(orderSetting);//预约设置成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();//预约设置失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}

