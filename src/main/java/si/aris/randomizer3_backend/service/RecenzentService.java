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

    public List<List<Recenzent>> pridobiPredlogeRecenzentov(List<String> naziviPoddomen, boolean jePoddomena, int steviloMest, Set<Long> zeUporabljeniRecenzenti) {
        List<Recenzent> kandidati = new ArrayList<>();

        System.out.println("⚡ Iskanje recenzentov za poddomene/domena: " + naziviPoddomen);
        System.out.println("🧐 Je poddomena? " + jePoddomena);

        // 1️⃣ Če seznam poddomen ni podan, vrnemo napako in preverimo iskanje po domeni
        if (!jePoddomena) {
            System.out.println("❌ Napaka: Seznam `naziviPodomen` je prazen! Preverjam, ali gre za domeno...");

            // Iščemo po domeni!
            List<Recenzent> recenzentiPoDomeni = recenzentRepository.findByDomenaNaziv(naziviPoddomen.get(0)); // Domnevamo, da je ena domena
            System.out.println("🔎 Iskanje recenzentov za domeno: " + naziviPoddomen.get(0) + " | Najdeno: " + recenzentiPoDomeni.size());

            kandidati.addAll(recenzentiPoDomeni);
        } else {
            // 2️⃣ Če imamo poddomene, jih obdelamo
            for (String naziv : naziviPoddomen) {
                List<Recenzent> recenzentiPoPoddomeni = recenzentRepository.findByPoddomenaNaziv(naziv);
                System.out.println("🔍 Najdenih recenzentov za poddomeno " + naziv + ": " + recenzentiPoPoddomeni.size());
                kandidati.addAll(recenzentiPoPoddomeni);
            }
        }

        System.out.println("📊 Skupno število kandidatov pred filtriranjem: " + kandidati.size());

        // 3️⃣ Odstranimo že izbrane recenzente
        kandidati.removeIf(recenzent -> zeUporabljeniRecenzenti.contains(recenzent.getId()));

        System.out.println("🎯 Število kandidatov po filtriranju uporabljenih: " + kandidati.size());

        // Premešamo seznam za naključni izbor
        Collections.shuffle(kandidati, new SecureRandom());

        // 4️⃣ Če ni dovolj kandidatov, vrnemo prazen seznam
        int steviloZaPredloge = steviloMest * 8;
        if (kandidati.size() < steviloZaPredloge) {
            System.out.println("🚨 Napaka: Ni dovolj recenzentov za " + naziviPoddomen + ". Potrebnih: " + steviloZaPredloge + ", na voljo: " + kandidati.size());
            return new ArrayList<>(); // Vrnemo prazen seznam, da ne pade v out of bounds
        }

        // 5️⃣ Vrnemo 8 predlogov za vsako mesto
        List<List<Recenzent>> predlogiZaVsakoMesto = new ArrayList<>();
        for (int i = 0; i < steviloMest; i++) {
            List<Recenzent> predlogi = kandidati.subList(i * 8, (i + 1) * 8);
            for (Recenzent r : predlogi) {
                zeUporabljeniRecenzenti.add(r.getId());
            }
            predlogiZaVsakoMesto.add(predlogi);
        }

        System.out.println("✅ Uspešno vrnjeni predlogi za: " + naziviPoddomen);
        return predlogiZaVsakoMesto;
    }




}
