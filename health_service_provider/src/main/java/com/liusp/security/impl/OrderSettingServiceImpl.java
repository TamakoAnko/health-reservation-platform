package com.liusp.security.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.liusp.dao.OrderSettingDao;
import com.liusp.pojo.OrderSetting;
import com.liusp.security.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
// @Transactional 注解的作用：开启 Spring 声明式事务管理，保证当前类中所有公共方法的数据库操作要么全部成功，要么全部回滚，确保数据一致性。
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    public void add(List<OrderSetting> list){
        if(list != null && list.size() > 0){
            for(OrderSetting orderSetting : list){
                //检查此数据（日期）是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0){
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据日期查询预约设置数据
    public List<Map> getOrderSettingByMonth(String date){
        String dateBegin = date + "-1";
        String dateEnd ="";String temp = date.substring(date.length() - 1);
        if (temp.equals("6") || temp.equals("9")|| temp.equals("11")){
            dateEnd = date + "-30";
        }else{
            dateEnd = date + "-31";
        }

        Map map = new HashMap();map.put("dateBegin",dateBegin);map.put("dateEnd",dateEnd);List<OrderSetting> list =orderSettingDao.getOrderSettingByMonth(map);List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;
    }

    //根据日期修改可预约人数
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }

}
