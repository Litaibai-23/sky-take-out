package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ShopController
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/11 11:50
 */
@RestController("UserShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {
    @Autowired
    private ShopService shopService;
    @GetMapping("/status")
    public Result getStatus()
    {
        log.info("获取店铺营业状态");
        return Result.success(shopService.getStatus());
    }
}
