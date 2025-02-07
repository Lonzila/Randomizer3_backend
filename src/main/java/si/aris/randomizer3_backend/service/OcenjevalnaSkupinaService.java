package si.aris.randomizer3_backend.service;

import org.springframework.stereotype.Service;
import si.aris.randomizer3_backend.entity.OcenjevalnaSkupina;
import si.aris.randomizer3_backend.entity.OcenjevalnaSkupinaPoddomena;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.repository.ErcDomenaRepository;
import si.aris.randomizer3_backend.repository.ErcPoddomenaRepository;
import si.aris.randomizer3_backend.repository.OcenjevalnaSkupinaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OcenjevalnaSkupinaService {

    private final RecenzentService recenzentService;
    private final OcenjevalnaSkupinaRepository ocenjevalnaSkupinaRepository;
    private final ErcDomenaRepository ercDomenaRepository;
    private final ErcPoddomenaRepository ercPoddomenaRepository;

    public OcenjevalnaSkupinaService(RecenzentService recenzentService,
                                     OcenjevalnaSkupinaRepository ocenjevalnaSkupinaRepository,
                                     ErcDomenaRepository ercDomenaRepository,
                                     ErcPoddomenaRepository ercPoddomenaRepository) {
        this.recenzentService = recenzentService;
        this.ocenjevalnaSkupinaRepository = ocenjevalnaSkupinaRepository;
        this.ercDomenaRepository = ercDomenaRepository;
        this.ercPoddomenaRepository = ercPoddomenaRepository;
    }

    // Obstoječa metoda
    public void dodeliRecenzenteSkupinam(List<OcenjevalnaSkupinaPoddomena> zahteveSkupin) {
        Set<Long> zeIzbraniRecenzenti = new HashSet<>(); // Beleži že izbrane recenzente

        for (OcenjevalnaSkupinaPoddomena zahteva : zahteveSkupin) {
            // Naključno izberi recenzente za to zahtevo
            List<Recenzent> predlogi = recenzentService.nakljucniIzborRecenzentov(
                    zahteva.isJeSpecifikacija() ? zahteva.getErcPoddomena().getNaziv() : zahteva.getErcDomena().getNaziv(),
                    zahteva.isJeSpecifikacija(),
                    zahteva.getSteviloRecenzentov(),
                    zeIzbraniRecenzenti
            );

            // Dodaj recenzente v ocenjevalno skupino in posodobi seznam že izbranih
            for (Recenzent predlog : predlogi) {
                zahteva.getOcenjevalnaSkupina().getRecenzenti().add(predlog);
                zeIzbraniRecenzenti.add(predlog.getId());
            }
        }

        // Shrani vse skupine in njihove povezave v bazo
        ocenjevalnaSkupinaRepository.saveAll(
                zahteveSkupin.stream()
                        .map(OcenjevalnaSkupinaPoddomena::getOcenjevalnaSkupina)
                        .distinct()
                        .toList()
        );
    }

    // Nova metoda za dodajanje ocenjevalnih skupin
    public void dodajSkupine() {
        // OS1: 3 recenzenti LS7
        OcenjevalnaSkupina os1 = new OcenjevalnaSkupina();
        os1.setNaziv("OS1");
        os1.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os1,
                ercPoddomenaRepository.findByNaziv("LS7").orElseThrow(() -> new IllegalArgumentException("Poddomena LS7 ne obstaja")),
                null,
                3,
                true
        ));
        ocenjevalnaSkupinaRepository.save(os1);

        // OS2: 3 recenzenti SH3
        OcenjevalnaSkupina os2 = new OcenjevalnaSkupina();
        os2.setNaziv("OS2");
        os2.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os2,
                ercPoddomenaRepository.findByNaziv("SH3").orElseThrow(() -> new IllegalArgumentException("Poddomena SH3 ne obstaja")),
                null,
                3,
                true
        ));
        ocenjevalnaSkupinaRepository.save(os2);

        // OS3: 1 SH1, 1 SH2, 1 SH1 ali SH2
        OcenjevalnaSkupina os3 = new OcenjevalnaSkupina();
        os3.setNaziv("OS3");
        os3.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os3,
                ercPoddomenaRepository.findByNaziv("SH1").orElseThrow(() -> new IllegalArgumentException("Poddomena SH1 ne obstaja")),
                null,
                1,
                true
        ));
        os3.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os3,
                ercPoddomenaRepository.findByNaziv("SH2").orElseThrow(() -> new IllegalArgumentException("Poddomena SH2 ne obstaja")),
                null,
                1,
                true
        ));
        os3.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os3,
                ercPoddomenaRepository.findByNaziv("SH1").orElseThrow(() -> new IllegalArgumentException("Poddomena SH1 ne obstaja")),
                null,
                1,
                true
        ));
        ocenjevalnaSkupinaRepository.save(os3);

        // OS4: 1 PE, 1 SH, 1 LS, 1 PE6
        OcenjevalnaSkupina os4 = new OcenjevalnaSkupina();
        os4.setNaziv("OS4");
        os4.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os4,
                ercPoddomenaRepository.findByNaziv("PE6").orElseThrow(() -> new IllegalArgumentException("Poddomena PE6 ne obstaja")),
                null,
                1,
                true
        ));
        os4.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os4,
                null,
                ercDomenaRepository.findByNaziv("PE").orElseThrow(() -> new IllegalArgumentException("Domena PE ne obstaja")),
                1,
                false
        ));
        os4.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os4,
                null,
                ercDomenaRepository.findByNaziv("SH").orElseThrow(() -> new IllegalArgumentException("Domena SH ne obstaja")),
                1,
                false
        ));
        os4.addPoddomena(new OcenjevalnaSkupinaPoddomena(
                os4,
                null,
                ercDomenaRepository.findByNaziv("LS").orElseThrow(() -> new IllegalArgumentException("Domena LS ne obstaja")),
                1,
                false
        ));
        ocenjevalnaSkupinaRepository.save(os4);
    }

}
