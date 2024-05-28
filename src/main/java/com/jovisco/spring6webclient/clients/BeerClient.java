package com.jovisco.spring6webclient.clients;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.jovisco.spring6webclient.model.BeerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerClient {

    Flux<String> getAll();
    @SuppressWarnings("rawtypes")
    Flux<Map> getAllAsMap();
    Flux<JsonNode> getAllAsJsonNode();
    Flux<BeerDTO> getAllBeers();
    Mono<BeerDTO> getById(String id);
    Flux<BeerDTO> getByBeerStyle(String beerStyle);
    Mono<BeerDTO> create(BeerDTO beerDTO);
    Mono<BeerDTO> update(BeerDTO beerDTO);
    Mono<Void> delete(String id);
}
