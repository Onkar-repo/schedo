package info.ogkapps.schedo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ogkapps.schedo.entity.Log;

@Repository
public interface LogsRepository extends JpaRepository<Log, Integer>{
List<Log> findByOwner_OwnerEmail(String ownerEmail);
List<Log> findByLogVisitorTimeBetween(long start, long end);
}
