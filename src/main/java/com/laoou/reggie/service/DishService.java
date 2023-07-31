package com.laoou.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laoou.reggie.dto.DishDto;
import com.laoou.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //    插入口味数据 操作两张表
    public void saveWithFlavor(DishDto dishDto);


    //    根据菜品id查询菜品
    public DishDto getByIdWithFlavor(Long id);

    //    gengxin caipin xinxi 同时更新口味信息
    void updateWithFlavor(DishDto dishDto);
}
