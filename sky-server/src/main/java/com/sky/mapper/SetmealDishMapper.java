package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: SetmealDish
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/3/13 19:47
 */
@Mapper
public interface SetmealDishMapper {
    //根据菜品id查询套餐id
    List<Long> getSetmealIdByDishId(List<Long> dishIds);

}
