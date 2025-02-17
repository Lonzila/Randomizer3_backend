package si.aris.randomizer3_backend.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.repository.RecenzentRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelUpdateService {

    @Autowired
    private RecenzentRepository recenzentRepository; // Repository za dostop do baze

    public void posodobiExcel(String inputFilePath, String outputFilePath) throws Exception {
        System.out.println("📂 Nalagam Excel: " + inputFilePath);

        // Preberi obstoječ Excel
        FileInputStream file = new FileInputStream(new File(inputFilePath));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        // Ustvari mapo za hitrejši dostop do recenzentov iz baze
        Map<String, Recenzent> recenzentiMapa = recenzentRepository.findAll().stream()
                .collect(Collectors.toMap(Recenzent::getSifraRecenzenta, r -> r));

        System.out.println("📊 Število recenzentov v bazi: " + recenzentiMapa.size());

        // Dodaj nove stolpce v naslovno vrstico
        Row headerRow = sheet.getRow(1);
        int newColumnIndex = headerRow.getLastCellNum();
        headerRow.createCell(newColumnIndex).setCellValue("Email");
        headerRow.createCell(newColumnIndex + 1).setCellValue("Organizacija");
        headerRow.createCell(newColumnIndex + 2).setCellValue("Država");

        // Prehodi čez vrstice in dodaj podatke
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell sifraCell = row.getCell(4); // Stolpec s šifro recenzenta
            if (sifraCell == null) continue;

            String sifraRecenzenta = getCellValue(sifraCell).trim(); // Preveri tip celice in pridobi vrednost

            System.out.println("🔎 Obdelujem recenzenta s šifro: " + sifraRecenzenta);

            Recenzent recenzent = recenzentiMapa.get(sifraRecenzenta);

            if (recenzent != null) {
                System.out.println("✅ Najden v bazi: " + recenzent.getIme() + " " + recenzent.getPriimek());

                row.createCell(newColumnIndex).setCellValue(recenzent.getEmail());
                row.createCell(newColumnIndex + 1).setCellValue(recenzent.getOrganizacija());
                row.createCell(newColumnIndex + 2).setCellValue(recenzent.getDrzava());

                System.out.println("📌 Zapisano v Excel: " + recenzent.getEmail() + " | " + recenzent.getOrganizacija() + " | " + recenzent.getDrzava());
            } else {
                System.out.println("⚠️ Recenzent s šifro " + sifraRecenzenta + " ni bil najden v bazi!");
            }
        }

        // Shrani posodobljen Excel
        FileOutputStream fileOut = new FileOutputStream(outputFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

        System.out.println("✅ Posodobljena datoteka shranjena kot: " + outputFilePath);
    }

    // ✅ Nova metoda za varno branje vrednosti iz celic
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue()); // Pretvori številko v String
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
