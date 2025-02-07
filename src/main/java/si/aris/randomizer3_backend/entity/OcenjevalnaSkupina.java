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

    private String naziv; // Ime skupine, npr. "OS1", "OS2"

    @ManyToMany
    @JoinTable(
            name = "ocenjevalna_skupina_recenzenti",
            joinColumns = @JoinColumn(name = "ocenjevalna_skupina_id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent_id")
    )
    private List<Recenzent> recenzenti = new ArrayList<>();

    @OneToMany(mappedBy = "ocenjevalnaSkupina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OcenjevalnaSkupinaPoddomena> poddomene = new ArrayList<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<Recenzent> getRecenzenti() {
        return recenzenti;
    }

    public List<OcenjevalnaSkupinaPoddomena> getPoddomene() {
        return poddomene;
    }

    public void addPoddomena(OcenjevalnaSkupinaPoddomena poddomena) {
        poddomene.add(poddomena);
        poddomena.setOcenjevalnaSkupina(this); // Povežemo z ocenjevalno skupino
    }

    public void addRecenzent(Recenzent recenzent) {
        if (!recenzenti.contains(recenzent)) {
            recenzenti.add(recenzent);
        }
    }

}