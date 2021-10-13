package org.ornarowicz.refuseReport.database.repository;

import org.ornarowicz.refuseReport.database.entity.SentReportsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudReportsListRepository extends CrudRepository<SentReportsEntity, Long> {
}
