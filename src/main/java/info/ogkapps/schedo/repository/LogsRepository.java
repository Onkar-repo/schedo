package info.ogkapps.schedo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ogkapps.schedo.entity.Log;

@Repository
public interface LogsRepository extends JpaRepository<Log, Integer>{

}
