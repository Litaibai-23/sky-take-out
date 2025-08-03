package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
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
}
