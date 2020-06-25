package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.dto.ChartDto;
import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;

import java.util.Date;

public interface StatisticService {

    DailyStatisticDto getDailyStatistic(Date dateFrom, Date dateUntil);

    ChartDto getChartStatistic(Date dateFrom, Date datUntil);
}
