package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * ClassName: DishController
 * Description:菜品管理
 *
 * @Author 乒乓界李大帅
 * @Create 2025/2/26 20:42
 */


/**
 * 新增菜品
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        String key = "dish_" + dishDTO.getCategoryId();
        clearCache(key);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    public Result<Object> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation(value = "菜品批量删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品开始批量删除：{}", ids);
        dishService.deleteBatch(ids);
        clearCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation(value = "修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        clearCache( "dish_*");
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询菜品")
    public Result<List<DishVO>> list(Dish dish){
        //查询redis中是否有缓存
        String key = "dish_" + dish.getCategoryId();
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() > 0) {
            log.info("在缓存中查询菜品：{}", dish);
            return Result.success(list);
        }
         log.info("在数据库中查询菜品：{}", dish);
         list = dishService.listWithFlavor(dish);
         redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }
    /**
     * 商品起售
     */
    @PostMapping("/status/{status}")
    @ApiOperation("商品起售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("商品起售或停售：{}", id);
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishService.update(dish);
        String key = "dish_" + dish.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 清除缓存
     */
    private void clearCache(String pattern) {
        log.info("清空缓存");
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
