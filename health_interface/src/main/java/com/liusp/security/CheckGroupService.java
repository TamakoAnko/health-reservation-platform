package com.liusp.security;

import com.liusp.entity.PageResult;
import com.liusp.pojo.CheckGroup;

import java.util.List;


public interface CheckGroupService {

    public void add(CheckGroup checkGroup , Integer[] checkItemIds);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    public void delete(Integer id);

    public void edit(CheckGroup checkGroup , Integer[] checkItemIds);

    public CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    List<CheckGroup> findAll();

}
