package org.ornarowicz.refuseReport.database.repository;

import org.ornarowicz.refuseReport.database.entity.LastProcessedEmailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudNumOfLastEmailRepository extends CrudRepository<LastProcessedEmailEntity, Integer> {
}
