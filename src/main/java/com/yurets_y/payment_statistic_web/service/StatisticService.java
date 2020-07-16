package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.dto.ChartDto;
import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;
import com.yurets_y.payment_statistic_web.dto.ChartStatisticDtoEntry;

import java.util.Date;
import java.util.List;

public interface StatisticService {

    DailyStatisticDto getDailyStatistic(Date dateFrom, Date dateUntil,Integer payerCode);

    ChartDto getDailyChartStatistic(Date dateFrom, Date datUntil, Integer averageIndex, Integer payerCode);

    List<ChartStatisticDtoEntry> getYearChartStatistic(Date dateFrom, Date dateUntil, Integer payerCode);

    List<ChartStatisticDtoEntry> getDailyChartStatisticNew(
            Date dateFrom, Date dateUntil, Integer payerCode, Integer averageIndex);
}
