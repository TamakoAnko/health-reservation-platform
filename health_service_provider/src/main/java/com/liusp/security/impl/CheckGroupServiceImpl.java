package com.liusp.security.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liusp.dao.CheckGroupDao;
import com.liusp.entity.PageResult;
import com.liusp.pojo.CheckGroup;
import com.liusp.security.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //添加检查组合，同时需要设置检查组合和检查项的关联关系
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkItemIds, checkGroup.getId());
    }

    //设置检查组合和检查项的关联关系
    public void setCheckGroupAndCheckItem(Integer[] checkItemIds, Integer checkGroupId) {
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.deleteAssociation(checkGroup.getId());
        setCheckGroupAndCheckItem(checkItemIds,checkGroup.getId());
        checkGroupDao.edit(checkGroup);
    }


    public void delete(Integer id) {
        long count = checkGroupDao.findSetMealCountByCheckGroupId(id);
        if (count > 0) {
            throw new RuntimeException("当前组已订阅套餐，不能被删除");
        }
        checkGroupDao.deleteAssociation(id);
        checkGroupDao.deleteById(id);
    }


    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}