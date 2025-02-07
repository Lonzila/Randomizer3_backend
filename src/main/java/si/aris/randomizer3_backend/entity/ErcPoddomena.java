package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "erc_poddomena")
public class ErcPoddomena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private String opis;

    @ManyToOne
    @JoinColumn(name = "erc_domena_id", nullable = false)
    private ErcDomena ercDomena;

    @OneToMany(mappedBy = "ercPoddomena", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecenzentDomena> recenzentDomenaList = new ArrayList<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    public ErcDomena getErcDomena() { return ercDomena; }
    public void setErcDomena(ErcDomena ercDomena) { this.ercDomena = ercDomena; }
    public List<RecenzentDomena> getRecenzentDomenaList() { return recenzentDomenaList; }
    public void setRecenzentDomenaList(List<RecenzentDomena> recenzentDomenaList) { this.recenzentDomenaList = recenzentDomenaList; }
}

