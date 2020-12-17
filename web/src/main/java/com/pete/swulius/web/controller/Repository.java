package com.pete.swulius.web.controller;

import com.pete.swulius.web.model.CityState;
import com.pete.swulius.web.model.CityStateLog;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

@org.springframework.stereotype.Repository
public class Repository {
    public Flux<CityState> findAllCityStates()
    {
        WebClient webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8081")  // device microservice
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();


        Flux<CityState> result = webClient
                .get()
                .uri("/api/v1/citystate")
                .retrieve()
                .bodyToFlux(CityState.class);


        return result;
    }

    public List<CityStateLog> findLogsForCityState( String aCity, String aState)
    {
        WebClient webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8081")  // device microservice
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();


        List<CityStateLog> result = webClient
                .get()
                .uri("/api/v1/citystatelog/" + aCity + "/" + aState)
                .retrieve()
                .toEntityList(CityStateLog.class)
                .block()
                .getBody();

        return result;
    }
}
