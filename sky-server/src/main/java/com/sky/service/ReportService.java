package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: ReportService
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/8/1 12:32
 */
public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);
}
