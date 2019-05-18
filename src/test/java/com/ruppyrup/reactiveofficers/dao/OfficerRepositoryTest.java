package com.ruppyrup.reactiveofficers.dao;

import com.ruppyrup.reactiveofficers.entities.Officer;
import com.ruppyrup.reactiveofficers.entities.Rank;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

// Mongo wont run without these annotations
@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficerRepositoryTest {
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
    public void setUp() throws Exception {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(repository::save)
                .then()
                .block();
    }

    @Test
    public void save() {
        Officer lorca = new Officer(Rank.CAPTAIN, "Gabriel", "Lorca");
        StepVerifier.create(repository.save(lorca))
                .expectNextMatches(officer -> !officer.getId().equals(""))
                .verifyComplete();
    }


    @Test
    public void findAll() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void findById() {
        officers.stream()
                .map(Officer::getId)
                .forEach(id -> StepVerifier.create(repository.findById(id))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void findByIdNotExist() {
        StepVerifier.create(repository.findById("xyz"))
                .verifyComplete();
    }

    @Test
    public void count() {
        StepVerifier.create(repository.count())
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    public void findByRank() {
        StepVerifier.create(
                repository.findByRank(Rank.CAPTAIN)
                        .map((Officer::getRank))
                        .distinct())
                .expectNextCount(1) // only one Captain rank
                .verifyComplete();

        StepVerifier.create(
                repository.findByRank(Rank.ENSIGN)
                        .map((Officer::getRank))
                        .distinct()) // no ensigns
                .verifyComplete();
    }

    @Test
    public void findByLast() {
        officers.stream()
                .map(Officer::getLast)
                .forEach(lastName ->
                        StepVerifier.create(repository.findByLast(lastName))
                                .expectNextMatches(officer -> officer.getLast().equals(lastName))
                                .verifyComplete());
    }
}