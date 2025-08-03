package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * ClassName: ReportController
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/8/1 12:11
 */
@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计：{}到{}", begin, end);
        return Result.success(reportService.turnoverStatistics(begin, end));
    }

    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(@DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计：{}到{}", begin, end);
        return Result.success(reportService.userStatistics(begin, end));
    }

    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> orderStatistics(@DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate begin,
                                                 @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计：{}到{}", begin, end);
        return Result.success(reportService.orderStatistics(begin, end));
    }

    @GetMapping("/top10")
    @ApiOperation("销量排名")
    public Result top10(@DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate begin,
                       @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate end){
        log.info("销量排名：{}到{}", begin, end);
        return Result.success(reportService.top10(begin, end));
    }
}
