package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.Station;

import java.util.List;

public interface StationService {

    List<Station> getStationByIdOrCode(String str);

    Station getByStationCode(Integer stationCode);

    List<Station> getAllStations();

    Station updateStation(Station station);

}
