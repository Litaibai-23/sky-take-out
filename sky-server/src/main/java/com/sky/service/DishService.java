package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * ClassName: DishService
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/2/26 20:45
 */
public interface DishService {
    public void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    Result updateWithFlavor(DishDTO dishDTO);

    List<DishVO> listWithFlavor(Dish dish);

    void update(Dish dish);
}
