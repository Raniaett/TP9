package com.example.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auteurs")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedEntityGraph(
    name = "Auteur.livres",
    attributeNodes = @NamedAttributeNode("livres")
)
public class Auteur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomm;

    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Livre> livres = new ArrayList<>();

    public Auteur() {
    }

    public Auteur(String nomm) {
        this.nomm = nomm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomm() {
        return nomm;
    }

    public void setNomm(String nomm) {
        this.nomm = nomm;
    }

    public List<Livre> getLivres() {
        return livres;
    }

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        livre.setAuteur(this);
    }

    public void retirerLivre(Livre livre) {
        livres.remove(livre);
        livre.setAuteur(null);
    }
}
