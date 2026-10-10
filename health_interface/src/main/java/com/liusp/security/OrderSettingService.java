package com.liusp.security;

import com.liusp.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    public void add(List<OrderSetting> list);

    public List<Map> getOrderSettingByMonth(String date);

    public void editNumberByDate(OrderSetting orderSetting);
}
