package com.laoou.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laoou.reggie.common.CustomException;
import com.laoou.reggie.entity.Category;
import com.laoou.reggie.entity.Dish;
import com.laoou.reggie.entity.Setmeal;
import com.laoou.reggie.mapper.CategoryMapper;
import com.laoou.reggie.service.CategoryService;
import com.laoou.reggie.service.DishService;
import com.laoou.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //        查询是否有关联 菜品
        if (count1 > 0) {
//            已经关联,抛出业务异常
            throw new CustomException("当前分类项关联了菜品，不可删除");
        }


//        查询是否有关联 套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
//            已经关联,抛出业务异常
            throw new CustomException("当前分类项关联了套餐，不可删除");
        }
//        正常删除
        super.removeById(id);
    }
}
