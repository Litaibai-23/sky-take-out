package com.sky.service.impl;

import com.sky.result.Result;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * ClassName: ShopServiceImpl
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/10 18:20
 */
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Integer changeStatus(Integer status) {
            redisTemplate.opsForValue().set("SHOP_STATUS", status);
        return (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
    }

    @Override
    public Integer getStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        return shopStatus;
    }
}
