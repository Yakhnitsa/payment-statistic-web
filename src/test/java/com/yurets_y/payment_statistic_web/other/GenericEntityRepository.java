package com.yurets_y.payment_statistic_web.other;


import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericEntityRepository extends JpaRepository<PaymentList, PaymentListId> {

}
