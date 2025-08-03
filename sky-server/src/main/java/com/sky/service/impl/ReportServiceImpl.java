package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: ReportServiceImpl
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/8/1 12:35
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String dateString = StringUtils.join(dateList,",");

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);

            //查询日期对应的营业额
            Double turnover = orderMapper.sumByDate(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        String turnoverString = StringUtils.join(turnoverList,",");
        return TurnoverReportVO.builder()
                .turnoverList(turnoverString)
                .dateList(dateString)
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String dateString = StringUtils.join(dateList,",");
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            LocalDateTime thatTime = LocalDateTime.of(date.plusDays(1), LocalTime.MIN);
            map.put("thatTime", thatTime);
            Integer totalUser = userMapper.countTotalByMap(map);
            totalUserList.add(totalUser);
        }
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end", endTime);
            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);
            newUserList.add(newUser);
        }
        String totalUserString = StringUtils.join(totalUserList,",");
        String newUserString = StringUtils.join(newUserList,",");
        UserReportVO userReportVO = UserReportVO.builder()
                .totalUserList(totalUserString)
                .dateList(dateString)
                .newUserList(newUserString)
                .build();
        return userReportVO;
    }

    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String dateString = StringUtils.join(dateList,",");
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            map.put("status", Orders.PENDING_PAYMENT);
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.put("begin", beginTime);
            map.put("end", endTime);
            Integer orderCount = orderMapper.countByMap(map);
            orderCountList.add(orderCount);
        }
        for (LocalDate date : dateList) {

            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.put("begin", beginTime);
            map.put("end", endTime);
            Integer validOrderCount = orderMapper.countStatusAndOrderTime(map);
            validOrderCountList.add(validOrderCount);
        }
        String orderCountString = StringUtils.join(orderCountList,",");
        String validOrderCountString = StringUtils.join(validOrderCountList,",");
        Integer totalOrderCount = orderMapper.countTotalByMap();
        Integer validOrderCount = orderMapper.countStatus(Orders.COMPLETED);

        Double orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;

        OrderReportVO orderReportVO = OrderReportVO.builder()
                .orderCountList(orderCountString)
                .validOrderCountList(validOrderCountString)
                .dateList(dateString)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();

        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List prudctList = orderDetailMapper.getTopName(beginTime, endTime);
        List numberList = orderDetailMapper.getTopNumber(beginTime, endTime);
        String nameList = StringUtils.join(prudctList, ",");
        String numberLista = StringUtils.join(numberList, ",");
        return new SalesTop10ReportVO(nameList, numberLista);
    }
}
