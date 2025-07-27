package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: ShoppingCartMapper
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/27 12:29
 */
@Mapper
public interface ShoppingCartMapper {
    /**
     * 动态sql查询
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据id更新购物车
     * @param
     */

    void updateNumberById(ShoppingCart cart);


    @Insert("insert into shopping_cart (name, image, dish_id, user_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES (#{name}, #{image}, #{dishId}, #{userId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    List<ShoppingCart> listByUserId(ShoppingCart build);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
