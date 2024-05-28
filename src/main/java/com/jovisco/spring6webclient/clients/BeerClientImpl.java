package com.jovisco.spring6webclient.clients;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.jovisco.spring6webclient.model.BeerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEER_PATH = "/api/v3/beers";
    public static final String BEER_ID_PATH = BEER_PATH + "/{id}";

    private final WebClient webClient;
    
    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Flux<String> getAll() {
        return webClient
            .get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(String.class);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Flux<Map> getAllAsMap() {
        return webClient
            .get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> getAllAsJsonNode() {
        return webClient
            .get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> getAllBeers() {
        return webClient
            .get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getById(String id) {
        return webClient
            .get()
            .uri(BEER_ID_PATH, id)
            .retrieve()
            .bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> getByBeerStyle(String beerStyle) {
        return webClient
            .get()
            .uri(uriBuilder -> uriBuilder.path(BEER_PATH).queryParam("beerStyle", beerStyle).build())
            .retrieve()
            .bodyToFlux(BeerDTO.class);

    }

    @Override
    public Mono<BeerDTO> create(BeerDTO beerDTO) {
        return webClient
            .post()
            .uri(BEER_PATH)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .retrieve()
            .toBodilessEntity()
            .flatMap(voidResponseEntity -> Mono.just(voidResponseEntity.getHeaders().get("Location").getFirst()))
            .map(path -> path.split("/")[path.split("/").length -1])
            .flatMap(this::getById);

    }

    @Override
    public Mono<BeerDTO> update(BeerDTO beerDTO) {
        return webClient
            .put()
            .uri(BEER_ID_PATH, beerDTO.getId())
            .body(Mono.just(beerDTO), BeerDTO.class)
            .retrieve()
            .toBodilessEntity()
            .flatMap(voidResponseEntity -> getById(beerDTO.getId()));
    }

    @Override
    public Mono<Void> delete(String id) {
        return webClient.delete()
            .uri(BEER_ID_PATH, id)
            .retrieve()
            .toBodilessEntity()
            .then();
    }

}
