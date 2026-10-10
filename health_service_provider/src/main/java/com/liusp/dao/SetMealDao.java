package com.liusp.dao;

import com.github.pagehelper.Page;
import com.liusp.pojo.CheckGroup;
import com.liusp.pojo.CheckItem;
import com.liusp.pojo.SetMeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {

    public void add(SetMeal setMeal);

    public void setSetMealAndCheckGroup(Map<String ,Integer> map);

    public Page<SetMeal> selectByCondition(String queryString);

    public long findCheckGroupCountBySetMealId(Integer id);

    public void deleteAssociation(Integer id);

    public void deleteById(Integer id);

    public void edit(SetMeal setMeal);

    public SetMeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    public List<SetMeal> findAll();

    public SetMeal findByIdWithGroups(Integer id);


}
