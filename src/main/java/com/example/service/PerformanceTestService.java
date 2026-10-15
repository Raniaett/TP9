package com.example.service;

import com.example.model.Auteur;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PerformanceTestService {

    public EntityManager emm;

    public void testPerformanceSansCache() {
        long start = System.currentTimeMillis();

        TypedQuery<Auteur> query = emm.createQuery("SELECT a FROM Auteur a", Auteur.class);
        List<Auteur> auteurs = query.getResultList();

        for (Auteur auteur : auteurs) {
            auteur.getLivres().size();
        }

        long end = System.currentTimeMillis();
        System.out.println("Durée sans cache : " + (end - start) + " ms");
    }

    public void testPerformanceAvecJoinFetch() {
        long start = System.currentTimeMillis();

        TypedQuery<Auteur> query = emm.createQuery("SELECT a FROM Auteur a JOIN FETCH a.livres", Auteur.class);
        List<Auteur> auteurs = query.getResultList();

        for (Auteur auteur : auteurs) {
            auteur.getLivres().size();
        }

        long end = System.currentTimeMillis();
        System.out.println("Durée avec JOIN FETCH : " + (end - start) + " ms");
    }

    public void testPerformanceAvecEntityGraph() {
        long start = System.currentTimeMillis();

        EntityGraph<?> graph = emm.getEntityGraph("Auteur.livres");
        TypedQuery<Auteur> query = emm.createQuery("SELECT a FROM Auteur a", Auteur.class);
        query.setHint("javax.persistence.fetchgraph", graph);
        List<Auteur> auteurs = query.getResultList();

        for (Auteur auteur : auteurs) {
            auteur.getLivres().size();
        }

        long end = System.currentTimeMillis();
        System.out.println("Durée avec EntityGraph : " + (end - start) + " ms");
    }

    public void testPerformanceAvecCache() {
        testPerformanceAvecJoinFetch();

        long start = System.currentTimeMillis();
        TypedQuery<Auteur> query = emm.createQuery("SELECT a FROM Auteur a JOIN FETCH a.livres", Auteur.class);
        query.setHint("org.hibernate.cacheable", true);
        List<Auteur> auteurs = query.getResultList();

        for (Auteur auteur : auteurs) {
            auteur.getLivres().size();
        }

        long end = System.currentTimeMillis();
        System.out.println("Durée avec cache (lecture 2) : " + (end - start) + " ms");
    }
}
