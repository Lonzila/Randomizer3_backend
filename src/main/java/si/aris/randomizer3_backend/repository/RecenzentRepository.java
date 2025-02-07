package si.aris.randomizer3_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import si.aris.randomizer3_backend.entity.Recenzent;

import java.util.List;

public interface RecenzentRepository extends JpaRepository<Recenzent, Long> {
    @Query("SELECT r FROM Recenzent r JOIN r.recenzentDomene rd JOIN rd.ercPoddomena p WHERE p.naziv = :poddomenaNaziv")
    List<Recenzent> findByPoddomenaNaziv(String poddomenaNaziv);

    @Query("SELECT r FROM Recenzent r JOIN r.recenzentDomene rd JOIN rd.ercPoddomena p JOIN p.ercDomena d WHERE d.naziv = :domenaNaziv")
    List<Recenzent> findByDomenaNaziv(String domenaNaziv);
}
