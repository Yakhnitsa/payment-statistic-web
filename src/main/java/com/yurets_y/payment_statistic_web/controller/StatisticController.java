package com.yurets_y.payment_statistic_web.controller;

import com.yurets_y.payment_statistic_web.dto.ChartDto;
import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;
import com.yurets_y.payment_statistic_web.dto.ChartStatisticDto;
import com.yurets_y.payment_statistic_web.service.StatisticService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            @RequestParam Integer payerCode,
            @RequestParam(defaultValue = "3") Integer averageIndex
    ){
        if (dateFrom == null || dateUntil == null) {
            return new ResponseEntity<String>(messageProvider.get(WRONG_PARAMETERS_MESSAGE),HttpStatus.BAD_REQUEST);
        }
        //        TODO - сделать проверку периода

        List<ChartStatisticDto> dto = statisticService.getDailyChartStatisticNew(dateFrom,dateUntil,payerCode);
        return new ResponseEntity<>(dto,HttpStatus.OK);

    }

    @GetMapping("/daily-statistic")
    public ResponseEntity<?> getDailyStatistic(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam Integer payerCode
    ) throws ParseException {
        if (dateFrom == null || dateUntil == null) {
            return new ResponseEntity<String>(messageProvider.get(WRONG_PARAMETERS_MESSAGE),HttpStatus.BAD_REQUEST);
        }
//        TODO - сделать проверку периода
        DailyStatisticDto dto = statisticService.getDailyStatistic(dateFrom,dateUntil,payerCode);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("year-chart")
    public ResponseEntity<?> getYearStatistic(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam Integer payerCode
    ){
        if (dateFrom == null || dateUntil == null) {
            return new ResponseEntity<>(messageProvider.get(WRONG_PARAMETERS_MESSAGE),HttpStatus.BAD_REQUEST);
        }

        List<ChartStatisticDto> dto = statisticService.getYearChartStatistic(dateFrom,dateUntil,payerCode);

        return new ResponseEntity<>(dto,HttpStatus.OK);

    }


}
