package com.yurets_y.payment_statistic_web.service.railroad_documents_services;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;


@Component
public class RailroadDocumentsSpecification {

    public Specification<RailroadDocument> docDateSpecification(Date dateFrom, Date dateTo){
        return new Specification<RailroadDocument>() {
            @Override
            public Predicate toPredicate(Root<RailroadDocument> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if(dateFrom == null && dateTo == null) return criteriaBuilder.and();
                if(dateFrom != null && dateTo != null){
                    return criteriaBuilder.between(root.<Date>get("docDate"),dateFrom,dateTo);
                }
                if(dateFrom != null){
                    Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("docDate"), dateFrom);
                    return predicate;
                }
                Predicate pr = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("docDate"), dateTo);
                return pr;

            }
        };
    }

    public Specification<RailroadDocument> docNumberSpecification(Integer docNumber){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("docNumber"),docNumber);
    }

    public Specification<RailroadDocument> sendStationSpec(Integer stationCode){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("sendStation").get("code"),stationCode);
    }
    public Specification<RailroadDocument> receiveStationSpec(Integer stationCode){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("receiveStation").get("code"),stationCode);
    }

    public Specification<RailroadDocument> senderCodeSpec(Integer code){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("cargoSender").get("railroadCode"),code);
    }

    public Specification<RailroadDocument> receiverCodeSpec(Integer code){
        return (Specification<RailroadDocument>) (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("cargoReceiver").get("railroadCode"),code);
    }




}
