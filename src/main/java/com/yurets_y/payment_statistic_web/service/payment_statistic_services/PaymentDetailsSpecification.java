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

    public Specification<PaymentDetails> dateSpec(Date dateFrom, Date dateTo){
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

    public Specification<RailroadDocument> payerCodeSpec(Integer payerCode){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> payerCode == null ? criteriaBuilder.and() :
                criteriaBuilder.equal(root.get("paymentList").get("payerCode"), payerCode);
    }

    public Specification<RailroadDocument> stationSpec(Integer stationCode){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> stationCode == null ? criteriaBuilder.and() :
                criteriaBuilder.equal(root.get("stationCode"),stationCode);
    }

    public Specification<RailroadDocument> paymentTypeSpec(String paymentType){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> paymentType == null || paymentType.equals("") ? criteriaBuilder.and() :
                criteriaBuilder.equal(root.get("type"),paymentType);
    }

    public Specification<PaymentDetails> totalPaymentSpec(Long higherThan, Long lessThan){
        return new Specification<PaymentDetails>() {
            @Override
            public Predicate toPredicate(Root<PaymentDetails> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if(higherThan == null && lessThan == null) return criteriaBuilder.and();
                if(higherThan != null && lessThan != null){
                    return criteriaBuilder.between(root.get("totalPayment"),higherThan,lessThan);
                }
                if(higherThan != null){
                    Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("totalPayment"), higherThan);
                    return predicate;
                }
                Predicate pr = criteriaBuilder.lessThanOrEqualTo(root.get("totalPayment"), lessThan);
                return pr;

            }
        };
    }
}
