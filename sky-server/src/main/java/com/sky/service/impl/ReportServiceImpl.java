package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private WorkspaceService workspaceService;
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

    /**
     * 导出近30天的运营数据报表
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        //查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin,LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            //基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            //获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                //准备明细数据
                businessData = workspaceService.getBusinessData(LocalDateTime.of(date,LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            //通过输出流将文件下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            //关闭资源
            out.flush();
            out.close();
            excel.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
