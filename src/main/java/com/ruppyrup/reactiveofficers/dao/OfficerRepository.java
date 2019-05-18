package com.ruppyrup.reactiveofficers.dao;

import com.ruppyrup.reactiveofficers.entities.Officer;
import com.ruppyrup.reactiveofficers.entities.Rank;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface OfficerRepository extends ReactiveMongoRepository<Officer, String> {
    Flux<Officer> findByRank(Rank rank);
    Flux<Officer> findByLast(String last);
}
