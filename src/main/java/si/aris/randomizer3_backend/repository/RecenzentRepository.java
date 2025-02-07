package si.aris.randomizer3_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.aris.randomizer3_backend.entity.Recenzent;

public interface RecenzentRepository extends JpaRepository<Recenzent, Long> {
    // Tu lahko dodate metode po meri, če bodo potrebne
}