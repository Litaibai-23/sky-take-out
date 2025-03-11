package com.sky.mapper;

import com.sky.entity.DishFlavor;
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
}
