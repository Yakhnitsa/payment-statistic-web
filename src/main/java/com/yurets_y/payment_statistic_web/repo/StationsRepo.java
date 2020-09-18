package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationsRepo extends JpaRepository<Station, Integer> {
    List<Station> findByCodeOrRusNameContainsIgnoreCaseOrUkrNameContainsIgnoreCase(Integer code,String rusName, String ukrName);
}
