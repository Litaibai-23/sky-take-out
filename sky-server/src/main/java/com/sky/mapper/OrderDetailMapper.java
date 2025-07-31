package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: OrderDetailMapper
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/27 15:27
 */
@Mapper
public interface OrderDetailMapper {

    void insertBatch(List<OrderDetail> ordersDetailList);

    List<OrderDetail> getByOrderId(Long orderId);
}
