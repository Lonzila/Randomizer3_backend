package si.aris.randomizer3_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.aris.randomizer3_backend.entity.ErcDomena;

import java.util.Optional;

@Repository
public interface ErcDomenaRepository extends JpaRepository<ErcDomena, Long> {
    Optional<ErcDomena> findByNaziv(String naziv);
}
