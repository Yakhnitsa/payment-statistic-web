package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationsRepo extends JpaRepository<Station, Long> {
}
