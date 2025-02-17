package si.aris.randomizer3_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.aris.randomizer3_backend.service.ExcelUpdateService;

import java.io.File;
import java.nio.file.Files;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelUpdateService excelUpdaterService;

    @GetMapping("/update")
    public ResponseEntity<byte[]> updateExcelFile() {
        try {
            String inputFilePath = "dodelitev_recenzentov_produkcija_12.02.2025.xlsx";
            String outputFilePath = "posodobljeni_recenzenti.xlsx";

            // Kliče funkcijo za posodobitev Excela
            excelUpdaterService.posodobiExcel(inputFilePath, outputFilePath);

            // Prebere datoteko in jo vrne kot odgovor
            File file = new File(outputFilePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=posodobljeni_recenzenti.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Napaka pri posodobitvi Excela: " + e.getMessage()).getBytes());
        }
    }
}

