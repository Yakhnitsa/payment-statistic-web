package com.yurets_y.payment_statistic_web.service.railroad_documents_services;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class RailroadDocumentsSpecification {

    public Specification<RailroadDocument> docDateSpecification(Date dateFrom, Date dateTo){
        return new Specification<RailroadDocument>() {
            @Override
            public Predicate toPredicate(Root<RailroadDocument> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if(dateFrom == null && dateTo == null) return predicate;
                if(dateFrom != null){
                    predicate = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("dateStamp"), dateFrom);
                }
                if(dateTo != null){
                    Predicate dateToPredicate = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("dateStamp"), dateTo);
                    predicate = criteriaBuilder.and(predicate,dateToPredicate);
                };

                return predicate;

            }
        };
    }


}
