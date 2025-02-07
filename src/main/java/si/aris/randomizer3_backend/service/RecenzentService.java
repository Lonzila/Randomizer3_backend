package si.aris.randomizer3_backend.service;

import org.springframework.stereotype.Service;
import si.aris.randomizer3_backend.entity.OcenjevalnaSkupina;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.repository.RecenzentRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    public List<Recenzent> getRandomRecenzenti(int count) {
        List<Recenzent> allRecenzenti = recenzentRepository.findAll();
        return allRecenzenti.stream().limit(count).toList();
    }

    public List<OcenjevalnaSkupina> razporediRecenzente(int steviloSkupin, int steviloRecenzentovNaSkupino) {
        List<Recenzent> vsiRecenzenti = recenzentRepository.findAll();
        Collections.shuffle(vsiRecenzenti); // Naključno premešamo seznam recenzentov

        List<OcenjevalnaSkupina> skupine = new ArrayList<>();

        int indeks = 0;
        for (int i = 0; i < steviloSkupin; i++) {
            OcenjevalnaSkupina skupina = new OcenjevalnaSkupina();
            skupina.setImeSkupine("Skupina " + (i + 1));

            List<Recenzent> dodeljeniRecenzenti = new ArrayList<>();
            for (int j = 0; j < steviloRecenzentovNaSkupino && indeks < vsiRecenzenti.size(); j++) {
                dodeljeniRecenzenti.add(vsiRecenzenti.get(indeks));
                indeks++;
            }

            skupina.setRecenzenti(dodeljeniRecenzenti);
            skupine.add(skupina);
        }

        return skupine;
    }
}
