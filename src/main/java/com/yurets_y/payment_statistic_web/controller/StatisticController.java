package com.yurets_y.payment_statistic_web.controller;

import com.yurets_y.payment_statistic_web.dto.ChartDto;
import com.yurets_y.payment_statistic_web.service.StatisticService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api/statistic")
public class StatisticController {
/*
* Реализовать статистику для графиков:
* 1. Ежедневная статистика
*  - Линейный график:
*       - статистика затрат
*       - статистика перечислений
*       - остаток на конец периода
*  - Круговой график
*       - распределение затрат по типу затрат
*       - распределение затрат по станциям
* 2. Годовая статистика с группировкой: по месяцам, по декадам, по неделям (кнопки переключения)
*   - суммарные данные за период
*   - движение графика в право-влево на 1 период (месяц, декада, неделя)
* */

    @Value("{application.controller.void-request-param}")
    private String WRONG_PERIOD_MESSAGE;
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final String WRONG_PARAMETERS_MESSAGE = "application.controller.void-request-param";


    private MessageProvider messageProvider;

    private StatisticService statisticService;

    @Autowired
    public StatisticController(MessageProvider messageProvider, StatisticService statisticService) {
        this.messageProvider = messageProvider;
        this.statisticService = statisticService;
    }

    @GetMapping("/daily-chart")
    public ResponseEntity<?> dailyChartStatistic(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam(defaultValue = "3") Integer averageIndex
    ){
        if (dateFrom == null || dateUntil == null) {
            return new ResponseEntity<String>(messageProvider.get(WRONG_PARAMETERS_MESSAGE),HttpStatus.BAD_REQUEST);
        }

        ChartDto dto = statisticService.getChartStatistic(dateFrom,dateUntil,averageIndex);
        return new ResponseEntity<ChartDto>(dto,HttpStatus.OK);

    }
}
