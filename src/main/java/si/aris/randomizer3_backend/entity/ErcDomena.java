package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "erc_domena")
public class ErcDomena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;

    @OneToMany(mappedBy = "ercDomena", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ErcPoddomena> poddomenaList = new ArrayList<>();

    public ErcDomena() {}
    public ErcDomena(String naziv) { // Konstruktor s parametrom
        this.naziv = naziv;
    }
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public List<ErcPoddomena> getPoddomenaList() { return poddomenaList; }
    public void setPoddomenaList(List<ErcPoddomena> poddomenaList) { this.poddomenaList = poddomenaList; }
}
