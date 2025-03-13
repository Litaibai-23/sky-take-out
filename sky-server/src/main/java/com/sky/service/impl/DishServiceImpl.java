package com.sky.service.impl;

import com.sky.mapper.SetmealDishMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: DishServiceImpl
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/2/26 20:48
 */

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDish;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional//添加事务
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //向菜品表插入数据，获取插入之后的id
        dishMapper.insert(dish);
        Long dishId = dish.getId();

        //向口味表插入数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            //向口味表批量插入数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<Dish> page = dishMapper.pageQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<Dish> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    public void deleteBatch(List<Long> ids) {
        //判断菜品能否删除
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new RuntimeException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断是否与套餐关联
        List<Long> setmealDishMapper = setmealDish.getSetmealIdByDishId(ids);
        if (setmealDishMapper.size() > 0) {
            throw new RuntimeException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除选中菜品的数据
        for (Long id : ids) {
            dishMapper.deleteById(id);
        }
        //删除菜品对应的口味数据
        for (Long id : ids) {
            dishFlavorMapper.deleteByDishId(id);
        }
    }
}
