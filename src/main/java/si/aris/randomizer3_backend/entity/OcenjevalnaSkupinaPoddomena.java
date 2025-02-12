package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ocenjevalna_skupina_poddomena")
public class OcenjevalnaSkupinaPoddomena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ocenjevalna_skupina_id", nullable = false)
    private OcenjevalnaSkupina ocenjevalnaSkupina;

    @ManyToOne
    @JoinColumn(name = "erc_poddomena_id")
    private ErcPoddomena ercPoddomena;

    @ManyToOne
    @JoinColumn(name = "erc_domena_id")
    private ErcDomena ercDomena;

    private int steviloRecenzentov;
    private boolean jeSpecifikacija;
    @ElementCollection
    private List<String> alternativePoddomen = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "predlogi_recenzentov",
            joinColumns = @JoinColumn(name = "poddomena_id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent_id")
    )
    private List<Recenzent> predlogiRecenzentov = new ArrayList<>();

    public OcenjevalnaSkupinaPoddomena() {}

    public OcenjevalnaSkupinaPoddomena(OcenjevalnaSkupina ocenjevalnaSkupina, ErcPoddomena ercPoddomena,
                                       ErcDomena ercDomena, int steviloRecenzentov, boolean jeSpecifikacija, List<String> alternativePoddomen) {
        this.ocenjevalnaSkupina = ocenjevalnaSkupina;
        this.ercPoddomena = ercPoddomena;
        this.ercDomena = ercDomena;
        this.steviloRecenzentov = steviloRecenzentov;
        this.jeSpecifikacija = jeSpecifikacija;
        this.alternativePoddomen = alternativePoddomen;
    }

    public List<String> getAlternativePoddomen() {
        return alternativePoddomen;
    }

    public List<Recenzent> getPredlogiRecenzentov() {
        return predlogiRecenzentov;
    }

    public void setPredlogiRecenzentov(List<Recenzent> predlogiRecenzentov) {
        if (predlogiRecenzentov == null) {
            this.predlogiRecenzentov = new ArrayList<>();
        } else {
            this.predlogiRecenzentov = new ArrayList<>(predlogiRecenzentov); // Preprečimo ImmutableList napako
        }
    }

    public void pocistiPredloge() {
        this.predlogiRecenzentov.clear();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public OcenjevalnaSkupina getOcenjevalnaSkupina() { return ocenjevalnaSkupina; }
    public void setOcenjevalnaSkupina(OcenjevalnaSkupina ocenjevalnaSkupina) { this.ocenjevalnaSkupina = ocenjevalnaSkupina; }
    public ErcPoddomena getErcPoddomena() { return ercPoddomena; }
    public void setErcPoddomena(ErcPoddomena ercPoddomena) { this.ercPoddomena = ercPoddomena; }
    public ErcDomena getErcDomena() { return ercDomena; }
    public void setErcDomena(ErcDomena ercDomena) { this.ercDomena = ercDomena; }
    public int getSteviloRecenzentov() { return steviloRecenzentov; }
    public void setSteviloRecenzentov(int steviloRecenzentov) { this.steviloRecenzentov = steviloRecenzentov; }
    public boolean isJeSpecifikacija() { return jeSpecifikacija; }
    public void setJeSpecifikacija(boolean jeSpecifikacija) { this.jeSpecifikacija = jeSpecifikacija; }
}

