package si.aris.randomizer3_backend.service;

import org.springframework.stereotype.Service;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.repository.RecenzentRepository;

import java.security.SecureRandom;
import java.util.ArrayList;
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

    public List<List<Recenzent>> pridobiPredlogeRecenzentov(String naziv, boolean jePoddomena, int steviloMest, Set<Long> zeIzbraniRecenzenti) {
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

        // Premešamo seznam za naključni izbor
        Collections.shuffle(kandidati, new SecureRandom());

        // Preverimo, ali imamo dovolj kandidatov
        int steviloZaPredloge = steviloMest * 4;
        if (kandidati.size() < steviloZaPredloge) {
            throw new IllegalArgumentException("Ni dovolj recenzentov za " + naziv + ". Potrebnih: " + steviloZaPredloge + ", na voljo: " + kandidati.size());
        }

        // Vrnemo 4 predloge za vsako mesto
        List<List<Recenzent>> predlogiZaVsakoMesto = new ArrayList<>();
        for (int i = 0; i < steviloMest; i++) {
            predlogiZaVsakoMesto.add(kandidati.subList(i * 4, (i + 1) * 4));
        }

        return predlogiZaVsakoMesto;
    }

}
