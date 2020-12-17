package com.yurets_y.payment_statistic_web.service.payment_statistic_services;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

@Component
public class PaymentDetailsSpecification {

    public Specification<PaymentDetails> dateSpecification(Date dateFrom, Date dateTo){
        return new Specification<PaymentDetails>() {
            @Override
            public Predicate toPredicate(Root<PaymentDetails> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if(dateFrom == null && dateTo == null) return criteriaBuilder.and();
                if(dateFrom != null && dateTo != null){
                    return criteriaBuilder.between(root.<Date>get("date"),dateFrom,dateTo);
                }
                if(dateFrom != null){
                    Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("date"), dateFrom);
                    return predicate;
                }
                Predicate pr = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("date"), dateTo);
                return pr;

            }
        };
    }
}
