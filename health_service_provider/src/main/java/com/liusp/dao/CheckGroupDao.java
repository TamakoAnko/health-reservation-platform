package com.liusp.dao;

import com.github.pagehelper.Page;
import com.liusp.pojo.CheckGroup;
import com.liusp.pojo.CheckItem;

import java.util.List;
import java.util.Map;


public interface CheckGroupDao {

    public void add(CheckGroup checkGroup);

    public void setCheckGroupAndCheckItem(Map map);

    public Page<CheckGroup> selectByCondition(String queryString);

    public void deleteById(Integer id);

    public long findSetMealCountByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup);

    public CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupById(Integer id);
}
