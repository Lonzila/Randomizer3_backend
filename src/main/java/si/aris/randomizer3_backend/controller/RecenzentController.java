package si.aris.randomizer3_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.aris.randomizer3_backend.entity.Recenzent;
import si.aris.randomizer3_backend.service.RecenzentService;

import java.util.List;

@RestController
@RequestMapping("/api/recenzenti")
public class RecenzentController {

    private final RecenzentService recenzentService;

    public RecenzentController(RecenzentService recenzentService) {
        this.recenzentService = recenzentService;
    }

    @PostMapping
    public ResponseEntity<Recenzent> addRecenzent(@RequestBody Recenzent recenzent) {
        return ResponseEntity.ok(recenzentService.saveRecenzent(recenzent));
    }

    @GetMapping
    public ResponseEntity<List<Recenzent>> getAllRecenzenti() {
        return ResponseEntity.ok(recenzentService.getAllRecenzenti());
    }
}
