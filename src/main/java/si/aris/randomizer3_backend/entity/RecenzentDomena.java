package si.aris.randomizer3_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recenzent_domena")
public class RecenzentDomena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recenzent_id", nullable = false)
    private Recenzent recenzent;

    @ManyToOne
    @JoinColumn(name = "erc_poddomena_id", nullable = false)
    private ErcPoddomena ercPoddomena;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Recenzent getRecenzent() { return recenzent; }
    public void setRecenzent(Recenzent recenzent) { this.recenzent = recenzent; }
    public ErcPoddomena getErcPoddomena() { return ercPoddomena; }
    public void setErcPoddomena(ErcPoddomena ercPoddomena) { this.ercPoddomena = ercPoddomena; }
}
