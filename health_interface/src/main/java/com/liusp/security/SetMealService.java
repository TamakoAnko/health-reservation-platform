package com.liusp.security;

import com.liusp.entity.PageResult;
import com.liusp.pojo.SetMeal;

import java.util.List;

public interface SetMealService {

    public void add(SetMeal setMeal,Integer[] checkGroupIds);

    public void setSetMealAndCheckGroup(Integer[] checkGroups, Integer setMealId);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    public void delete(Integer id);

    public void edit(SetMeal setMeal, Integer[] checkGroupIds);

    public SetMeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    public List<SetMeal> findAll();

    public SetMeal findById(int id);

}
