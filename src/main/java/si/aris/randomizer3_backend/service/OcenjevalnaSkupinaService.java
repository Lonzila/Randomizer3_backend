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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
public class OcenjevalnaSkupinaService {
    private LocalDateTime zadnjaDodelitev;
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

    public void generirajPredlogeRecenzentov() {
        zadnjaDodelitev = LocalDateTime.now();
        List<OcenjevalnaSkupina> vseSkupine = ocenjevalnaSkupinaRepository.findAll();

        //Set za sledenje že uporabljenih recenzentov (predlagani + izbrani)
        Set<Long> zeUporabljeniRecenzenti = new HashSet<>();

        System.out.println("🚀 Začetek generiranja predlogov za ocenjevalne skupine...");

        for (OcenjevalnaSkupina skupina : vseSkupine) {
            System.out.println("🎯 Obravnavam ocenjevalno skupino: " + skupina.getNaziv());

            for (OcenjevalnaSkupinaPoddomena zahteva : skupina.getPoddomene()) {
                List<String> moznePoddomene = new ArrayList<>();

                // Logiranje poddomen in domen
                if (zahteva.getErcPoddomena() != null) {
                    moznePoddomene.add(zahteva.getErcPoddomena().getNaziv());
                    System.out.println("✅ Podana specifična poddomena: " + zahteva.getErcPoddomena().getNaziv());
                }

                if (!zahteva.getAlternativePoddomen().isEmpty()) {
                    moznePoddomene.addAll(zahteva.getAlternativePoddomen());
                    System.out.println("✅ Alternative poddomene: " + zahteva.getAlternativePoddomen());
                }

                if (moznePoddomene.isEmpty() && zahteva.getErcDomena() != null) {
                    moznePoddomene.add(zahteva.getErcDomena().getNaziv());
                    System.out.println("🏛️ Uporabljena bo domena: " + zahteva.getErcDomena().getNaziv());
                }

                // Če ni podanih poddomen ali domen, vržemo opozorilo
                if (moznePoddomene.isEmpty()) {
                    System.out.println("❌ Napaka v skupini " + skupina.getNaziv() + ": Ni podane nobene domene ali poddomene!");
                    continue; // Preskoči to iteracijo, da ne sesuje procesa
                }

                // Pridobimo predloge recenzentov
                System.out.println("🔎 Iskanje recenzentov za: " + moznePoddomene);
                List<List<Recenzent>> predlogiZaVsakoMesto = recenzentService.pridobiPredlogeRecenzentov(
                        moznePoddomene,
                        zahteva.isJeSpecifikacija(),
                        zahteva.getSteviloRecenzentov(),
                        zeUporabljeniRecenzenti
                );

                // Shrani predloge
                zahteva.setPredlogiRecenzentov(predlogiZaVsakoMesto.stream().flatMap(List::stream).toList());

                for (List<Recenzent> predlogi : predlogiZaVsakoMesto) {
                    if (!predlogi.isEmpty()) {
                        Recenzent izbranRecenzent = predlogi.get(0);
                        skupina.addRecenzent(izbranRecenzent);
                        zeUporabljeniRecenzenti.add(izbranRecenzent.getId());
                        System.out.println("🎉 Izbran recenzent: " + izbranRecenzent.getIme() + " " + izbranRecenzent.getPriimek());
                    } else {
                        System.out.println("⚠️ Opozorilo: Ni predlogov za eno mesto v skupini " + skupina.getNaziv());
                    }
                }
            }
        }

        // Shranimo spremembe v bazo
        ocenjevalnaSkupinaRepository.saveAll(vseSkupine);
        System.out.println("✅ Končano generiranje predlogov.");
    }

    public LocalDateTime getZadnjaDodelitev() {
        return zadnjaDodelitev;
    }


    public void pocistiDodelitve() {
        List<OcenjevalnaSkupina> vseSkupine = ocenjevalnaSkupinaRepository.findAll();

        for (OcenjevalnaSkupina skupina : vseSkupine) {
            skupina.getRecenzenti().clear(); // Odstrani vse dodeljene recenzente
            for (OcenjevalnaSkupinaPoddomena zahteva : skupina.getPoddomene()) {
                zahteva.pocistiPredloge(); // Počisti predloge recenzentov
            }
        }

        ocenjevalnaSkupinaRepository.saveAll(vseSkupine); // Posodobimo bazo
    }
    // Nova metoda za dodajanje ocenjevalnih skupin
    public void dodajSkupine() {
        // OS1
        OcenjevalnaSkupina os1 = new OcenjevalnaSkupina("OS1");
        os1.addPoddomena(new OcenjevalnaSkupinaPoddomena(os1, ercPoddomenaRepository.findByNaziv("LS7").orElseThrow(), null, 2, true, List.of()));
        os1.addPoddomena(new OcenjevalnaSkupinaPoddomena(os1, null, ercDomenaRepository.findByNaziv("LS").orElseThrow(), 1, true, List.of("LS2", "LS3", "LS4", "LS7")));
        os1.addPoddomena(new OcenjevalnaSkupinaPoddomena(os1, null, ercDomenaRepository.findByNaziv("LS").orElseThrow(), 1, true, List.of("LS2", "LS3", "LS4", "LS5", "LS6", "LS7")));
        ocenjevalnaSkupinaRepository.save(os1);

        // OS2
        OcenjevalnaSkupina os2 = new OcenjevalnaSkupina("OS2");
        os2.addPoddomena(new OcenjevalnaSkupinaPoddomena(os2, ercPoddomenaRepository.findByNaziv("SH6").orElseThrow(), null, 1, true, List.of()));
        os2.addPoddomena(new OcenjevalnaSkupinaPoddomena(os2, ercPoddomenaRepository.findByNaziv("SH5").orElseThrow(), null, 1, true, List.of()));
        os2.addPoddomena(new OcenjevalnaSkupinaPoddomena(os2, null, ercDomenaRepository.findByNaziv("SH").orElseThrow(), 1, true, List.of("SH4", "SH7")));
        ocenjevalnaSkupinaRepository.save(os2);

        // OS3
        OcenjevalnaSkupina os3 = new OcenjevalnaSkupina("OS3");
        os3.addPoddomena(new OcenjevalnaSkupinaPoddomena(os3, ercPoddomenaRepository.findByNaziv("SH1").orElseThrow(), null, 1, true, List.of()));
        os3.addPoddomena(new OcenjevalnaSkupinaPoddomena(os3, ercPoddomenaRepository.findByNaziv("SH2").orElseThrow(), null, 1, true, List.of()));
        os3.addPoddomena(new OcenjevalnaSkupinaPoddomena(os3, ercPoddomenaRepository.findByNaziv("SH3").orElseThrow(), null, 1, true, List.of()));
        ocenjevalnaSkupinaRepository.save(os3);

        // OS4
        OcenjevalnaSkupina os4 = new OcenjevalnaSkupina("OS4");
        os4.addPoddomena(new OcenjevalnaSkupinaPoddomena(os4, null, ercDomenaRepository.findByNaziv("PE").orElseThrow(), 1, true, List.of("PE4", "PE5")));
        os4.addPoddomena(new OcenjevalnaSkupinaPoddomena(os4, ercPoddomenaRepository.findByNaziv("LS8").orElseThrow(), null, 1, true, List.of()));
        os4.addPoddomena(new OcenjevalnaSkupinaPoddomena(os4, ercPoddomenaRepository.findByNaziv("LS9").orElseThrow(), null, 1, true, List.of()));
        ocenjevalnaSkupinaRepository.save(os4);

        // OS5
        OcenjevalnaSkupina os5 = new OcenjevalnaSkupina("OS5");
        os5.addPoddomena(new OcenjevalnaSkupinaPoddomena(os5, ercPoddomenaRepository.findByNaziv("LS4").orElseThrow(), null, 1, true, List.of()));
        os5.addPoddomena(new OcenjevalnaSkupinaPoddomena(os5, ercPoddomenaRepository.findByNaziv("LS7").orElseThrow(), null, 1, true, List.of()));
        os5.addPoddomena(new OcenjevalnaSkupinaPoddomena(os5, null, ercDomenaRepository.findByNaziv("LS").orElseThrow(), 1, true, List.of("LS6", "LS7")));
        ocenjevalnaSkupinaRepository.save(os5);

        // OS6
        OcenjevalnaSkupina os6 = new OcenjevalnaSkupina("OS6");
        os6.addPoddomena(new OcenjevalnaSkupinaPoddomena(os6, ercPoddomenaRepository.findByNaziv("PE8").orElseThrow(), null, 1, true, List.of()));
        os6.addPoddomena(new OcenjevalnaSkupinaPoddomena(os6, null, ercDomenaRepository.findByNaziv("PE").orElseThrow(), 1, true, List.of("PE1", "PE2", "PE3")));
        os6.addPoddomena(new OcenjevalnaSkupinaPoddomena(os6, ercPoddomenaRepository.findByNaziv("PE10").orElseThrow(), null, 1, true, List.of()));
        ocenjevalnaSkupinaRepository.save(os6);

        // OS7
        OcenjevalnaSkupina os7 = new OcenjevalnaSkupina("OS7");
        os7.addPoddomena(new OcenjevalnaSkupinaPoddomena(os7, null, ercDomenaRepository.findByNaziv("PE").orElseThrow(), 1, true, List.of("PE1", "PE2", "PE3", "PE4", "PE6", "PE9", "PE10")));
        os7.addPoddomena(new OcenjevalnaSkupinaPoddomena(os7, null, ercDomenaRepository.findByNaziv("LS").orElseThrow(), 1, true, List.of("LS4", "LS6", "LS7", "LS8", "LS9")));
        os7.addPoddomena(new OcenjevalnaSkupinaPoddomena(os7, null, ercDomenaRepository.findByNaziv("SH").orElseThrow(), 2, true, List.of("SH3", "SH4", "SH5", "SH6", "SH7")));
        ocenjevalnaSkupinaRepository.save(os7);

        // OS8
        OcenjevalnaSkupina os8 = new OcenjevalnaSkupina("OS8");
        os8.addPoddomena(new OcenjevalnaSkupinaPoddomena(os8, null, ercDomenaRepository.findByNaziv("PE").orElseThrow(), 1, false, List.of()));
        os8.addPoddomena(new OcenjevalnaSkupinaPoddomena(os8, null, ercDomenaRepository.findByNaziv("LS").orElseThrow(), 1, false, List.of()));
        os8.addPoddomena(new OcenjevalnaSkupinaPoddomena(os8, null, ercDomenaRepository.findByNaziv("SH").orElseThrow(), 1, false, List.of()));
        os8.addPoddomena(new OcenjevalnaSkupinaPoddomena(os8, null, ercDomenaRepository.findByNaziv("PE").orElseThrow(), 1, true, List.of("PE2", "PE3", "PE4", "PE5", "PE6", "PE11")));
        ocenjevalnaSkupinaRepository.save(os8);
    }

    public List<OcenjevalnaSkupina> getAllSkupine() {
        return ocenjevalnaSkupinaRepository.findAll();
    }

    public File generirajExcelPorocilo() throws Exception {
        List<OcenjevalnaSkupina> skupine = ocenjevalnaSkupinaRepository.findAll();
        LocalDateTime casDodelitve = getZadnjaDodelitev(); // Pridobimo čas generiranja
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Dodelitev Recenzentov");

        // **Dodamo timestamp v prvo vrstico**
        Row timestampRow = sheet.createRow(0);
        timestampRow.createCell(0).setCellValue("Čas generiranja predlogov:");
        timestampRow.createCell(1).setCellValue(casDodelitve.format(formatter));

        // **Dodamo naslovno vrstico**
        Row headerRow = sheet.createRow(1);
        String[] stolpci = {"Ocenjevalna Skupina", "Zahtevana Poddomena/Domena", "Alternative Poddomene", "Št. potrebnih recenzentov", "Šifra Recenzenta", "Ime", "Priimek", "Status"};

        for (int i = 0; i < stolpci.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(stolpci[i]);
            cell.setCellStyle(nastaviHeaderStyle(workbook));
        }

        int rowNum = 2; // Začnemo na tretji vrstici (po timestamp in naslovih)

        for (OcenjevalnaSkupina skupina : skupine) {
            for (OcenjevalnaSkupinaPoddomena zahteva : skupina.getPoddomene()) {
                // Pravilno določimo naziv domene ali poddomene
                String zahtevanaVrednost;
                if (zahteva.getErcPoddomena() != null) {
                    zahtevanaVrednost = zahteva.getErcPoddomena().getNaziv();
                } else if (zahteva.getErcDomena() != null) {
                    zahtevanaVrednost = zahteva.getErcDomena().getNaziv();
                } else {
                    zahtevanaVrednost = "N/A"; // V primeru, da ni določeno
                }

                // Alternativne poddomene kot string
                String alternativnePoddomene = zahteva.getAlternativePoddomen().isEmpty()
                        ? "Ni alternativ"
                        : String.join(", ", zahteva.getAlternativePoddomen());

                for (Recenzent predlaganRecenzent : zahteva.getPredlogiRecenzentov()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(skupina.getNaziv());
                    row.createCell(1).setCellValue(zahtevanaVrednost);
                    row.createCell(2).setCellValue(alternativnePoddomene);
                    row.createCell(3).setCellValue(zahteva.getSteviloRecenzentov());
                    row.createCell(4).setCellValue(predlaganRecenzent.getSifraRecenzenta());
                    row.createCell(5).setCellValue(predlaganRecenzent.getIme());
                    row.createCell(6).setCellValue(predlaganRecenzent.getPriimek());
                    row.createCell(7).setCellValue(skupina.getRecenzenti().contains(predlaganRecenzent) ? "Izbran" : "Predlagan");
                }
            }
        }

        // **Shrani datoteko na disk**
        String filePath = "dodelitev_recenzentov.xlsx";
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

        return new File(filePath);
    }

    // **Metoda za oblikovanje naslovne vrstice (bold)**
    private CellStyle nastaviHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

}
