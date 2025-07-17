package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ShopController
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/10 18:17
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {
    @Autowired
    private ShopService shopService;
    @GetMapping("/status")
    public Result getStatus(){
        log.info("查询店铺营业状态");
        return Result.success(shopService.getStatus());
    }
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置营业状态:{}",status==1?"营业中":"打烊中");
        return Result.success(shopService.changeStatus(status));
    }

}
