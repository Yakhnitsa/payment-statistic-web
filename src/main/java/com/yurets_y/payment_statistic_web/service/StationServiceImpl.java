package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.Station;
import com.yurets_y.payment_statistic_web.repo.StationsRepo;
import com.yurets_y.payment_statistic_web.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StationServiceImpl implements StationService {


    @Autowired
    public StationServiceImpl(StationsRepo stationsRepo) {
        this.stationsRepo = stationsRepo;
    }

    private StationsRepo stationsRepo;

    @Override
    public List<Station> getStationByIdOrCode(String str) {
        int stationCode = 0;
        if(str.matches("\\d+")){
            stationCode = Integer.parseInt(str);
        }
        return stationsRepo
                .findByCodeOrRusNameContainsIgnoreCaseOrUkrNameContainsIgnoreCase(stationCode,str,str);
    }

    @Override
    public Station getByStationCode(Integer stationCode) {
        return stationsRepo.findById(stationCode).orElse(null);
    }

    @Override
    public List<Station> getAllStations() {
        return stationsRepo.findAll();
    }

    @Override
    public Station addStation(Station station) {
        return stationsRepo.saveAndFlush(station);
    }
}
