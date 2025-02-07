package si.aris.randomizer3_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.aris.randomizer3_backend.entity.RecenzentDomena;

@Repository
public interface RecenzentDomenaRepository extends JpaRepository<RecenzentDomena, Long> {

}