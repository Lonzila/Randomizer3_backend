package si.aris.randomizer3_backend.service;

import org.springframework.stereotype.Service;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.repository.RecenzentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class RecenzentService {

    private final RecenzentRepository recenzentRepository;

    public RecenzentService(RecenzentRepository recenzentRepository) {
        this.recenzentRepository = recenzentRepository;
    }

    public Recenzent saveRecenzent(Recenzent recenzent) {
        return recenzentRepository.save(recenzent);
    }

    public List<Recenzent> getAllRecenzenti() {
        return recenzentRepository.findAll();
    }

    public List<Recenzent> nakljucniIzborRecenzentov(String naziv, boolean jePoddomena, int steviloPredlogov, Set<Long> zeIzbraniRecenzenti) {
        List<Recenzent> kandidati;

        if (jePoddomena) {
            // Iskanje recenzentov po specifični poddomeni
            kandidati = recenzentRepository.findByPoddomenaNaziv(naziv);
        } else {
            // Iskanje recenzentov po splošni domeni
            kandidati = recenzentRepository.findByDomenaNaziv(naziv);
        }

        // Odstranimo že izbrane recenzente
        kandidati.removeIf(recenzent -> zeIzbraniRecenzenti.contains(recenzent.getId()));

        // Premešamo seznam za naključen izbor
        Collections.shuffle(kandidati);

        // Preverimo, ali imamo dovolj kandidatov
        if (kandidati.size() < steviloPredlogov) {
            throw new IllegalArgumentException("Premalo recenzentov za zahtevo: " + naziv);
        }

        // Vrni želeno število predlogov
        return kandidati.subList(0, steviloPredlogov);
    }
}
