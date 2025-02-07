package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;

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
    private ErcPoddomena ercPoddomena; // Specifična poddomena

    @ManyToOne
    @JoinColumn(name = "erc_domena_id")
    private ErcDomena ercDomena; // Splošna domena

    private int steviloRecenzentov; // Število recenzentov za zahtevo
    private boolean jeSpecifikacija; // Ali gre za specifično poddomeno (true) ali splošno domeno (false)

    public OcenjevalnaSkupinaPoddomena(OcenjevalnaSkupina ocenjevalnaSkupina, ErcPoddomena ercPoddomena,
                                       ErcDomena ercDomena, int steviloRecenzentov, boolean jeSpecifikacija) {
        this.ocenjevalnaSkupina = ocenjevalnaSkupina;
        this.ercPoddomena = ercPoddomena;
        this.ercDomena = ercDomena;
        this.steviloRecenzentov = steviloRecenzentov;
        this.jeSpecifikacija = jeSpecifikacija;
    }

    public OcenjevalnaSkupinaPoddomena() {

    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OcenjevalnaSkupina getOcenjevalnaSkupina() {
        return ocenjevalnaSkupina;
    }

    public void setOcenjevalnaSkupina(OcenjevalnaSkupina ocenjevalnaSkupina) {
        this.ocenjevalnaSkupina = ocenjevalnaSkupina;
    }

    public ErcPoddomena getErcPoddomena() {
        return ercPoddomena;
    }

    public void setErcPoddomena(ErcPoddomena ercPoddomena) {
        this.ercPoddomena = ercPoddomena;
    }

    public ErcDomena getErcDomena() {
        return ercDomena;
    }

    public void setErcDomena(ErcDomena ercDomena) {
        this.ercDomena = ercDomena;
    }

    public int getSteviloRecenzentov() {
        return steviloRecenzentov;
    }

    public void setSteviloRecenzentov(int steviloRecenzentov) {
        this.steviloRecenzentov = steviloRecenzentov;
    }

    public boolean isJeSpecifikacija() {
        return jeSpecifikacija;
    }

    public void setJeSpecifikacija(boolean jeSpecifikacija) {
        this.jeSpecifikacija = jeSpecifikacija;
    }
}
