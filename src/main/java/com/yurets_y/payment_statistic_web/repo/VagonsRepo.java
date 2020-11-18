package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.Vagon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagonsRepo extends JpaRepository<Vagon, Long> {


}
