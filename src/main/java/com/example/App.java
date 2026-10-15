package com.example;

import com.example.service.DataInitService;
import com.example.service.PerformanceTestService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory emmf = Persistence.createEntityManagerFactory("hibernate-cache-pu");
        EntityManager emm = emmf.createEntityManager();

        DataInitService dataInitService = new DataInitService();
        PerformanceTestService performanceTestService = new PerformanceTestService();

        dataInitService.emm = emm;
        performanceTestService.emm = emm;

        emm.getTransaction().begin();
        dataInitService.initData();
        emm.getTransaction().commit();

        System.out.println("--- Test sans cache ---");
        performanceTestService.testPerformanceSansCache();

        System.out.println("--- Test avec JOIN FETCH ---");
        performanceTestService.testPerformanceAvecJoinFetch();

        System.out.println("--- Test avec EntityGraph ---");
        performanceTestService.testPerformanceAvecEntityGraph();

        System.out.println("--- Test avec cache de second niveau ---");
        performanceTestService.testPerformanceAvecCache();

        emm.close();
        emmf.close();
    }
}
