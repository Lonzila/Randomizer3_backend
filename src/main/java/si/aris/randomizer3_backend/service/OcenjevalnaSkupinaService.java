package si.aris.randomizer3_backend.service;

import org.springframework.stereotype.Service;
import si.aris.randomizer3_backend.entity.OcenjevalnaSkupina;
import si.aris.randomizer3_backend.entity.OcenjevalnaSkupinaPoddomena;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.repository.ErcDomenaRepository;
import si.aris.randomizer3_backend.repository.ErcPoddomenaRepository;
import si.aris.randomizer3_backend.repository.OcenjevalnaSkupinaRepository;
import si.aris.randomizer3_backend.repository.RecenzentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
public class OcenjevalnaSkupinaService {

    private final RecenzentService recenzentService;
    private final OcenjevalnaSkupinaRepository ocenjevalnaSkupinaRepository;
    private final ErcDomenaRepository ercDomenaRepository;
    private final ErcPoddomenaRepository ercPoddomenaRepository;
    private final RecenzentRepository recenzentRepository;
    public OcenjevalnaSkupinaService(RecenzentService recenzentService,
                                     OcenjevalnaSkupinaRepository ocenjevalnaSkupinaRepository,
                                     ErcDomenaRepository ercDomenaRepository,
                                     ErcPoddomenaRepository ercPoddomenaRepository, RecenzentRepository recenzentRepository) {
        this.recenzentService = recenzentService;
        this.ocenjevalnaSkupinaRepository = ocenjevalnaSkupinaRepository;
        this.ercDomenaRepository = ercDomenaRepository;
        this.ercPoddomenaRepository = ercPoddomenaRepository;
        this.recenzentRepository = recenzentRepository;
    }

    public void nakljucnoDodeliRecenzente() {
        List<OcenjevalnaSkupina> vseSkupine = ocenjevalnaSkupinaRepository.findAll();
        Set<Long> zeIzbraniRecenzenti = new HashSet<>();

        for (OcenjevalnaSkupina skupina : vseSkupine) {
            for (OcenjevalnaSkupinaPoddomena zahteva : skupina.getPoddomene()) {
                // Pridobi predloge za vsako mesto
                List<List<Recenzent>> predlogiZaVsakoMesto = recenzentService.pridobiPredlogeRecenzentov(
                        zahteva.isJeSpecifikacija() ? zahteva.getErcPoddomena().getNaziv() : zahteva.getErcDomena().getNaziv(),
                        zahteva.isJeSpecifikacija(),
                        zahteva.getSteviloRecenzentov(),
                        zeIzbraniRecenzenti
                );

                // Shrani predloge in izberi prvega za vsako mesto
                for (List<Recenzent> predlogi : predlogiZaVsakoMesto) {
                    zahteva.getPredlogiRecenzentov().addAll(predlogi); // Shrani predloge
                    Recenzent izbranRecenzent = predlogi.get(0); // Izberi prvega
                    skupina.getRecenzenti().add(izbranRecenzent); // Dodaj izbranega v skupino
                    predlogi.forEach(recenzent -> zeIzbraniRecenzenti.add(recenzent.getId())); // Označi kot izbran
                }
            }

            ocenjevalnaSkupinaRepository.save(skupina); // Shrani skupino
        }
    }


    public void pocistiDodelitve() {
        List<OcenjevalnaSkupina> vseSkupine = ocenjevalnaSkupinaRepository.findAll();

        for (OcenjevalnaSkupina skupina : vseSkupine) {
            skupina.getRecenzenti().clear(); // Odstrani vse dodeljene recenzente iz skupine
        }

        ocenjevalnaSkupinaRepository.saveAll(vseSkupine); // Posodobi skupine v bazi
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

    public List<OcenjevalnaSkupina> getAllSkupine() {
        return ocenjevalnaSkupinaRepository.findAll();
    }

    public File generirajExcelPorocilo() throws Exception {
        List<OcenjevalnaSkupina> skupine = ocenjevalnaSkupinaRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Dodelitev Recenzentov");

        // Ustvari naslovno vrstico
        Row headerRow = sheet.createRow(0);
        String[] stolpci = {"Ocenjevalna Skupina", "Zahtevana Poddomena/Domena", "Št. potrebnih recenzentov", "Šifra Recenzenta", "Ime", "Priimek"};

        for (int i = 0; i < stolpci.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(stolpci[i]);
            cell.setCellStyle(nastaviHeaderStyle(workbook));
        }

        int rowNum = 1;

        for (OcenjevalnaSkupina skupina : skupine) {
            for (OcenjevalnaSkupinaPoddomena zahteva : skupina.getPoddomene()) {
                for (Recenzent predlaganRecenzent : zahteva.getPredlogiRecenzentov()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(skupina.getNaziv());
                    row.createCell(1).setCellValue(zahteva.isJeSpecifikacija() ? zahteva.getErcPoddomena().getNaziv() : zahteva.getErcDomena().getNaziv());
                    row.createCell(2).setCellValue(zahteva.getSteviloRecenzentov());
                    row.createCell(3).setCellValue(predlaganRecenzent.getSifraRecenzenta());
                    row.createCell(4).setCellValue(predlaganRecenzent.getIme());
                    row.createCell(5).setCellValue(predlaganRecenzent.getPriimek());
                    row.createCell(6).setCellValue(skupina.getRecenzenti().contains(predlaganRecenzent) ? "Izbran" : "Predlagan");
                }
            }
        }

        // Shranimo datoteko na disk
        String filePath = "dodelitev_recenzentov.xlsx";
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

        return new File(filePath);
    }

    // Metoda za oblikovanje naslovne vrstice (bold)
    private CellStyle nastaviHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

}
