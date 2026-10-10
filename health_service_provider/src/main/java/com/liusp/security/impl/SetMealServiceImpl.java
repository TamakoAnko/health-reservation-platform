package com.liusp.security.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liusp.constant.RedisConstant;
import com.liusp.dao.SetMealDao;
import com.liusp.entity.PageResult;
import com.liusp.pojo.SetMeal;
import com.liusp.security.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService{

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    //从属性文件读取输出目录的路径
    @Value("${spring.freemarker.out_put_path}")
    private String outputpath ;

    public void add(SetMeal setMeal, Integer[] checkGroupIds) {
        //添加setMeal对象信息到数据库
        setMealDao.add(setMeal);
        //将套餐包含的检查组信息添加到数据库
        if(checkGroupIds != null && checkGroupIds.length > 0){
            //绑定套餐和检查组的多对多关系
            setSetMealAndCheckGroup(checkGroupIds,setMeal.getId());
        }
        //将图片名称保存到Redis
        savePic2Redis(setMeal.getImg());
        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    //生成静态页面
    public void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<SetMeal> setmealList = this.findAll();//生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);//生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<SetMeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<SetMeal> setmealList)
    {
        for (SetMeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", setMealDao.findByIdWithGroups(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_" + setmeal.getId() + ".html",
                    dataMap);
        }
    }

    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outputpath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }



    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        redisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }



    public void setSetMealAndCheckGroup(Integer[] checkGroups, Integer setMealId) {
        if (checkGroups != null && checkGroups.length > 0) {
            for (Integer checkGroup : checkGroups) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", setMealId);
                map.put("checkgroup_id", checkGroup);
                setMealDao.setSetMealAndCheckGroup(map);
            }
        }
    }

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString ){
        PageHelper.startPage(currentPage,pageSize);
        Page<SetMeal> page = setMealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    public void delete(Integer id){
        long count = setMealDao.findCheckGroupCountBySetMealId(id);
        if (count>0){
            setMealDao.deleteAssociation(id);
        }
        setMealDao.deleteById(id);
    }

    public void edit(SetMeal setMeal , Integer[] checkItemIds) {
        setMealDao.deleteAssociation(setMeal.getId());
        setSetMealAndCheckGroup(checkItemIds,setMeal.getId());
        setMealDao.edit(setMeal);
    }

    @Override
    public SetMeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    public List<Integer> findCheckGroupIdsBySetMealId(Integer id){
        return setMealDao.findCheckGroupIdsBySetMealId(id);
    }


    public List<SetMeal> findAll() {
        return setMealDao.findAll();
    }

    public SetMeal findById(int id) {
        return setMealDao.findByIdWithGroups(id);
    }
}
