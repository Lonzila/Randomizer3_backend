package si.aris.randomizer3_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import si.aris.randomizer3_backend.service.DataImportService;

@RestController
@RequestMapping("/api/import")
public class DataImportController {

    private final DataImportService dataImportService;

    public DataImportController(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }

    @PostMapping
    public ResponseEntity<String> importData(@RequestParam("file") MultipartFile file) {
        try {
            dataImportService.importData(file);
            return ResponseEntity.ok("Podatki uspešno uvoženi!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Napaka pri uvozu podatkov: " + e.getMessage());
        }
    }
}

