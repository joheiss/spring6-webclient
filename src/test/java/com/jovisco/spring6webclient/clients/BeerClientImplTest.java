package com.jovisco.spring6webclient.clients;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jovisco.spring6webclient.model.BeerDTO;

import static org.awaitility.Awaitility.await;

/**
 * 
 * IMPORTANT: for those tests to run, the app spring6-ractive-mongo must run on http://localhost:8080
 * 
 */
@SpringBootTest
public class BeerClientImplTest {

    @Autowired
    BeerClient client;

    @Test
    void testGetAll() {

        var atomicBoolean = new AtomicBoolean(false);

        client.getAll().subscribe(res -> {
            System.out.println(res);
            System.out.flush();
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);

    }
    @Test
    void testGetAllAsMap() {

        var atomicBoolean = new AtomicBoolean(false);

        client.getAllAsMap().subscribe(res -> {
            System.out.println(res);
            System.out.flush();
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetAllAsJsonNode() {

        var atomicBoolean = new AtomicBoolean(false);

        client.getAllAsJsonNode().subscribe(res -> {
            System.out.println(res.toPrettyString());
            System.out.flush();
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetAllBeers() {

        var atomicBoolean = new AtomicBoolean(false);

        client.getAllBeers().subscribe(res -> {
            System.out.println(res);
            System.out.flush();
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetById() {

        var atomicBoolean = new AtomicBoolean(false);

        client.getAllBeers()
            .take(1)
            .flatMap(dto -> client.getById(dto.getId()))
            .subscribe(res -> {
                System.out.println(res);
                System.out.flush();
                atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);

    }

    @Test
    void testGetByBeerStyle() {

        var atomicBoolean = new AtomicBoolean(false);

        client.getAllBeers()
            .take(1)
            .flatMap(dto -> {
                System.out.println(dto.getBeerStyle());
                System.out.flush();
                return client.getByBeerStyle(dto.getBeerStyle());
            })
            .subscribe(res -> {
                System.out.println(res);
                System.out.flush();
                atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testCreateBeer() {

        var atomicBoolean = new AtomicBoolean(false);

        var testBeer = BeerDTO.builder()
            .beerName("Hunzenberger Mild")
            .beerStyle("WHEAT")
            .price(new BigDecimal("4.56"))
            .quantityOnHand(24)
            .upc("0815")
            .build();

        client.create(testBeer)
            .subscribe(res -> {
                System.out.println(res);
                System.out.flush();
                atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);

    }

    @Test
    void testUpdateBeer() {

        final String BEER_NAME = "Deppenbräu Dünn";

        var atomicBoolean = new AtomicBoolean(false);

        client.getAllBeers()
            .next()
            .doOnNext(dto -> dto.setBeerName(BEER_NAME))
            .flatMap(dto -> client.update(dto))
            .subscribe(res -> {
                System.out.println(res);
                System.out.flush();
                atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);

    }

    @Test
    void testDeleteBeer() {

        var atomicBoolean = new AtomicBoolean(false);

        client.getAllBeers()
            .next()
            .flatMap(dto -> client.delete(dto.getId()))
            .doOnSuccess(x -> atomicBoolean.set(true))
            .subscribe();
        
        await().untilTrue(atomicBoolean);

    }

}
