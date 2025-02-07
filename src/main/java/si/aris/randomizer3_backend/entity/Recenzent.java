package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recenzenti")
public class Recenzent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String sifraRecenzenta;

    @Column(columnDefinition = "TEXT")
    private String ime;

    @Column(columnDefinition = "TEXT")
    private String priimek;

    @Column(columnDefinition = "TEXT")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String organizacija;

    @Column(columnDefinition = "TEXT")
    private String drzava;

    @OneToMany(mappedBy = "recenzent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecenzentDomena> recenzentDomene = new ArrayList<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSifraRecenzenta() { return sifraRecenzenta; }
    public void setSifraRecenzenta(String sifraRecenzenta) { this.sifraRecenzenta = sifraRecenzenta; }
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getPriimek() { return priimek; }
    public void setPriimek(String priimek) { this.priimek = priimek; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOrganizacija() { return organizacija; }
    public void setOrganizacija(String organizacija) { this.organizacija = organizacija; }
    public String getDrzava() { return drzava; }
    public void setDrzava(String drzava) { this.drzava = drzava; }
    public List<RecenzentDomena> getRecenzentDomene() { return recenzentDomene; }
    public void setRecenzentDomene(List<RecenzentDomena> recenzentDomene) { this.recenzentDomene = recenzentDomene; }
}