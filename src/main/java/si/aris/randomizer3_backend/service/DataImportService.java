package si.aris.randomizer3_backend.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.aris.randomizer3_backend.entity.ErcDomena;
import si.aris.randomizer3_backend.entity.ErcPoddomena;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.entity.RecenzentDomena;
import si.aris.randomizer3_backend.repository.ErcDomenaRepository;
import si.aris.randomizer3_backend.repository.ErcPoddomenaRepository;
import si.aris.randomizer3_backend.repository.RecenzentDomenaRepository;
import si.aris.randomizer3_backend.repository.RecenzentRepository;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataImportService {

    private final RecenzentRepository recenzentRepository;
    private final ErcDomenaRepository ercDomenaRepository;
    private final ErcPoddomenaRepository ercPoddomenaRepository;
    private final RecenzentDomenaRepository recenzentDomenaRepository;

    public DataImportService(
            RecenzentRepository recenzentRepository,
            ErcDomenaRepository ercDomenaRepository,
            ErcPoddomenaRepository ercPoddomenaRepository,
            RecenzentDomenaRepository recenzentDomenaRepository) {
        this.recenzentRepository = recenzentRepository;
        this.ercDomenaRepository = ercDomenaRepository;
        this.ercPoddomenaRepository = ercPoddomenaRepository;
        this.recenzentDomenaRepository = recenzentDomenaRepository;
    }

    public void importData(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Map<String, ErcDomena> domeneCache = new HashMap<>();
        Map<String, ErcPoddomena> poddomenaCache = new HashMap<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Preskoči glavo

            // Preberemo recenzenta
            Recenzent recenzent = new Recenzent();
            recenzent.setSifraRecenzenta(row.getCell(0).getStringCellValue());
            recenzent.setIme(row.getCell(7).getStringCellValue());
            recenzent.setPriimek(row.getCell(8).getStringCellValue());
            recenzent.setEmail(row.getCell(2).getStringCellValue());
            recenzent.setOrganizacija(row.getCell(3).getStringCellValue());
            recenzent.setDrzava(row.getCell(4).getStringCellValue());
            recenzentRepository.save(recenzent);

            // Preberemo glavne domene iz "ERC Domena" (stolpec 6)
            String ercDomenaVrednost = row.getCell(12).getStringCellValue();
            String[] ercDomene = ercDomenaVrednost.split("\\s*\\+\\s*"); // Razdelimo po '+'

            for (String domenaNaziv : ercDomene) { // Iteriramo po glavnih domenah
                ErcDomena domena = domeneCache.computeIfAbsent(domenaNaziv, naziv ->
                        ercDomenaRepository.findByNaziv(naziv)
                                .orElseGet(() -> ercDomenaRepository.save(new ErcDomena(naziv)))
                );

                // Preberemo poddomene iz stolpcev "P ERC 1" (N) do "SH7" (AN)
                for (int i = 13; i <= 39; i++) { // Stolpci za poddomene
                    Cell poddomenaCell = row.getCell(i);
                    if (poddomenaCell != null && poddomenaCell.getCellType() == CellType.STRING) {
                        String poddomenaNaziv = poddomenaCell.getStringCellValue().trim();
                        if (!poddomenaNaziv.isEmpty()) {
                            // Preverimo, če poddomena pripada trenutni domeni (po prefiksu)
                            if (poddomenaNaziv.startsWith(domenaNaziv)) {
                                ErcPoddomena poddomena = poddomenaCache.computeIfAbsent(poddomenaNaziv, naziv ->
                                        ercPoddomenaRepository.findByNaziv(naziv)
                                                .orElseGet(() -> {
                                                    ErcPoddomena novaPoddomena = new ErcPoddomena();
                                                    novaPoddomena.setNaziv(naziv);
                                                    novaPoddomena.setErcDomena(domena);
                                                    System.out.println("Domena: " + domena.getNaziv() +
                                                            " -> Povezana poddomena: " + naziv);
                                                    return ercPoddomenaRepository.save(novaPoddomena);
                                                })
                                );

                                // Povežemo recenzenta s poddomeno
                                RecenzentDomena recenzentDomena = new RecenzentDomena();
                                recenzentDomena.setRecenzent(recenzent);
                                recenzentDomena.setErcPoddomena(poddomena);
                                recenzentDomenaRepository.save(recenzentDomena);
                            } else {
                                System.out.println("Poddomena " + poddomenaNaziv + " ne pripada domeni " + domenaNaziv);
                            }
                        }
                    }
                }
            }
}
        workbook.close();
        inputStream.close();
    }
}
