package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recenzenti")
public class Recenzent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;
    private String priimek;
    private String sifraRecenzenta;
    private String drzava;
    private String ustanova;
    private String epošta;
    private String domenaErc;
    private String podrocjeErc;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getPriimek() { return priimek; }
    public void setPriimek(String priimek) { this.priimek = priimek; }
    public String getSifraRecenzenta() { return sifraRecenzenta; }
    public void setSifraRecenzenta(String sifraRecenzenta) { this.sifraRecenzenta = sifraRecenzenta; }
    public String getDrzava() { return drzava; }
    public void setDrzava(String drzava) { this.drzava = drzava; }
    public String getUstanova() { return ustanova; }
    public void setUstanova(String ustanova) { this.ustanova = ustanova; }
    public String getEposta() { return epošta; }
    public void setEposta(String epošta) { this.epošta = epošta; }
    public String getDomenaErc() { return domenaErc; }
    public void setDomenaErc(String domenaErc) { this.domenaErc = domenaErc; }
    public String getPodrocjeErc() { return podrocjeErc; }
    public void setPodrocjeErc(String podrocjeErc) { this.podrocjeErc = podrocjeErc; }
}