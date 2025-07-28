package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: OrderMapper
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/27 15:25
 */
@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);
}
