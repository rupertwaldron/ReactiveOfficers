package com.ruppyrup.reactiveofficers.controller;


import com.ruppyrup.reactiveofficers.dao.OfficerRepository;
import com.ruppyrup.reactiveofficers.entities.Officer;
import com.ruppyrup.reactiveofficers.entities.Rank;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfficerHandlerTest {
    @Autowired
    private WebTestClient client;

    @Autowired
    private OfficerRepository repository;

    private List<Officer> officers = Arrays.asList(
            new Officer(Rank.CAPTAIN, "James T", "Kirk"),
            new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(Rank.CAPTAIN, "JBen", "Sisko"),
            new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
            new Officer(Rank.CAPTAIN, "Jonathan", "Archer")
    );

    @Before
    public void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(repository::save)
                .doOnNext(System.out::println)
                .then()
                .block();
    }

    @Test
    public void testGetAllOfficers() {
        client.get().uri("/route")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Officer.class)
                .hasSize(5)
                .consumeWith(System.out::println);
    }

    @Test
    public void testGetOfficer() {
        Officer officer = repository.save(new Officer(Rank.ENSIGN, "Wesley", "Crusher")).block();

        client.get().uri("/route/{id}", officer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Officer.class)
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testCreateOfficer() {
        Officer officer = new Officer(Rank.LIEUTENTANT, "Hikaru", "Sulu");

        client.post().uri("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(officer), Officer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.first").isEqualTo("Hikaru")
                .jsonPath("$.last").isEqualTo("Sulu")
                .consumeWith(System.out::println);
    }

    @Test
    public void testPutOfficer() {
        Officer officer = new Officer(Rank.ADMIRAL, "James Tibirius", "Kirk");

        client.put().uri("/route/{id}", officers.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(officer), Officer.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.rank").isEqualTo("ADMIRAL")
                .jsonPath("$.first").isEqualTo("James Tibirius")
                .jsonPath("$.last").isEqualTo("Kirk")
                .consumeWith(System.out::println);
    }

}
