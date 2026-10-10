package com.liusp.security;
import com.liusp.entity.PageResult;
import com.liusp.pojo.CheckItem;
import java.util.List;
/**
 * 检查项服务接口*/
public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    public void delete(Integer id);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    public List<CheckItem> findAll();
}