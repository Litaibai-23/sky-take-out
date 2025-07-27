package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * ClassName: ShoppingCartService
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/27 12:25
 */
public interface ShoppingCartService {
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();

    void clean();

    void sub(ShoppingCartDTO shoppingCartDTO);
}
