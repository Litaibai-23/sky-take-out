package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * ClassName: OrderService
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/27 15:22
 */
public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
