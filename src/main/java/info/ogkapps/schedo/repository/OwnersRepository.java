package info.ogkapps.schedo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ogkapps.schedo.entity.Owner;

@Repository
public interface OwnersRepository extends JpaRepository<Owner, Integer>{

}
