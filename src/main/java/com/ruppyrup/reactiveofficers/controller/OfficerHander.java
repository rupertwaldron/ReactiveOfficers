package com.ruppyrup.reactiveofficers.controller;

import com.ruppyrup.reactiveofficers.dao.OfficerRepository;
import com.ruppyrup.reactiveofficers.entities.Officer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Slf4j
@Component
public class OfficerHander {
    private OfficerRepository repository;

    public OfficerHander(OfficerRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getOfficer(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        log.error("Get Officer {}", id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        Mono<Officer> officerMono = repository.findById(id);
        return officerMono
                .flatMap(person -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(person)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> listOfficers(ServerRequest serverRequest) {
        log.error("List Officers");
        return ServerResponse
                .ok() // we got the response 200
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll(), Officer.class);
    }

    public Mono<ServerResponse> createOfficer(ServerRequest serverRequest) {
        Mono<Officer> officerMono = serverRequest.bodyToMono(Officer.class);
        return officerMono.flatMap(officer ->
                ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(repository.save(officer), Officer.class));
    }

    public Mono<ServerResponse> updateOfficer(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        log.error("Update Officer {}", id);

        Mono<Officer> officerMono = serverRequest.bodyToMono(Officer.class);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return officerMono
                .flatMap(existingOfficer -> {
                    existingOfficer.setRank(officer.getRank());
                    existingOfficer.setFirst(officer.getFirst());
                    existingOfficer.setLast(officer.getLast());
                    return repository.save(existingOfficer);
                })
                .map(updateOfficer -> ServerResponse.status(HttpStatus.ACCEPTED));
        return ServerResponse.status(HttpStatus.I_AM_A_TEAPOT).build();

    }
}
