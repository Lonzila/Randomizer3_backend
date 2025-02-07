package si.aris.randomizer3_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.aris.randomizer3_backend.service.OcenjevalnaSkupinaService;

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
}
