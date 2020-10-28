package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RailroadDocumentsRepo extends JpaRepository<RailroadDocument, RailroadDocumentId>, JpaSpecificationExecutor<RailroadDocument> {

}
