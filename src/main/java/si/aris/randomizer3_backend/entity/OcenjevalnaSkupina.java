package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ocenjevalne_skupine")
public class OcenjevalnaSkupina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imeSkupine;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "skupina_id")
    private List<Recenzent> recenzenti = new ArrayList<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImeSkupine() {
        return imeSkupine;
    }

    public void setImeSkupine(String imeSkupine) {
        this.imeSkupine = imeSkupine;
    }

    public List<Recenzent> getRecenzenti() {
        return recenzenti;
    }

    public void setRecenzenti(List<Recenzent> recenzenti) {
        this.recenzenti = recenzenti;
    }
}
