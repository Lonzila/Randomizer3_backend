package si.aris.randomizer3_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.aris.randomizer3_backend.entity.ErcPoddomena;

import java.util.Optional;

@Repository
public interface ErcPoddomenaRepository extends JpaRepository<ErcPoddomena, Long> {
    Optional<ErcPoddomena> findByNaziv(String naziv);
}
