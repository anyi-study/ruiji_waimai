package com.laoou.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laoou.reggie.common.CustomException;
import com.laoou.reggie.dto.SetmealDto;
import com.laoou.reggie.entity.Setmeal;
import com.laoou.reggie.entity.SetmealDish;
import com.laoou.reggie.mapper.SetmealMapper;
import com.laoou.reggie.service.SetmealDishService;
import com.laoou.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品关联关系
     * @param setmealDto
     */
    @Override
    @Transactional//事务注解
    public void saveWithDish(SetmealDto setmealDto) {
        //        保存套餐基本信息，操作setmeal，执行insert
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //        保存套餐和菜品关联信息，操作setmeal_dish，执行insert
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，并且删除关联关系
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态 ，确定是否可以删除
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.in(Setmeal::getId, ids);
        qw.eq(Setmeal::getStatus, 1);
        int count = this.count(qw);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lq = new LambdaQueryWrapper<>();
        lq.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(lq);
    }
}
