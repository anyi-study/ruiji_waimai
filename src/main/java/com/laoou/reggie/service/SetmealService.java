package com.laoou.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laoou.reggie.dto.SetmealDto;
import com.laoou.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品关联关系
     *
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，并且删除关联关系
     *
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

}
