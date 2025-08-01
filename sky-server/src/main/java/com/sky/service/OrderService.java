package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.*;

/**
 * ClassName: OrderService
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/27 15:22
 */
public interface OrderService {
    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    PageResult pageQuery(int pageNum, int pageSize, Integer status);

    OrderVO getOrderDetail(Long id);

    void cancel(Long id);

    void repetition(Long id) throws Exception;

    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    void confirm(OrdersCancelDTO ordersCancelDTO);

    OrderVO details(Long id);

    void delivery(Long id);

    void complete(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void reminder(Long id);
}

