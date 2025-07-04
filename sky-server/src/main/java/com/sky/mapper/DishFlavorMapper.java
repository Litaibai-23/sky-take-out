package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: DishFlavorMapper
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/3/11 9:58
 */

@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);

    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteByDishId(Long id);

 /*
     * 根据菜品id批量删除口味数据
 */
    void deleteByDishIds(List<Long> ids);

    List<DishFlavor> getByDishId(Long id);
}
