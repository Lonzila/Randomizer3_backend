package si.aris.randomizer3_backend.controller;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.aris.randomizer3_backend.entity.OcenjevalnaSkupina;
import si.aris.randomizer3_backend.service.OcenjevalnaSkupinaService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/skupine")
public class OcenjevalnaSkupinaController {

    private final OcenjevalnaSkupinaService ocenjevalnaSkupinaService;

    public OcenjevalnaSkupinaController(OcenjevalnaSkupinaService ocenjevalnaSkupinaService) {
        this.ocenjevalnaSkupinaService = ocenjevalnaSkupinaService;
    }

    @PostMapping("/dodaj")
    public ResponseEntity<String> dodajSkupine() {
        ocenjevalnaSkupinaService.dodajSkupine();
        return ResponseEntity.ok("Ocenjevalne skupine so bile uspešno dodane.");
    }
    @GetMapping
    public ResponseEntity<List<OcenjevalnaSkupina>> getAllSkupine() {
        return ResponseEntity.ok(ocenjevalnaSkupinaService.getAllSkupine());
    }
    @PostMapping("/razporedi")
    public ResponseEntity<String> nakljucnoRazporediRecenzente() {
        ocenjevalnaSkupinaService.nakljucnoDodeliRecenzente();
        return ResponseEntity.ok("Recenzenti so bili uspešno naključno dodeljeni ocenjevalnim skupinam.");
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportExcel() throws Exception {
        File file = ocenjevalnaSkupinaService.generirajExcelPorocilo();
        Path path = Paths.get(file.getAbsolutePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
    @DeleteMapping("/pocisti-dodelitve")
    public ResponseEntity<String> pocistiDodelitve() {
        ocenjevalnaSkupinaService.pocistiDodelitve();
        return ResponseEntity.ok("Dodelitve so bile uspešno izbrisane.");
    }
}
