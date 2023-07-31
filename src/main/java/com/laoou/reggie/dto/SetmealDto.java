package com.laoou.reggie.dto;

import com.laoou.reggie.entity.Setmeal;
import com.laoou.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
