package com.ruppyrup.reactiveofficers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.ruppyrup.reactiveofficers.dao.OfficerRepository;
import reactor.core.publisher.Flux;

/**
 * Copyright (c)2019 DFS Services LLC
 * All rights reserved.
 *
 * @author rwaldro
 */
@Component
public class OfficerInit implements ApplicationRunner {
  private OfficerRepository dao;

  @Autowired
  public OfficerInit(OfficerRepository dao) {
    this.dao = dao;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    dao.deleteAll()
      .thenMany(Flux.just(
          new Officer(Rank.CAPTAIN, "James T", "Kirk"),
          new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
          new Officer(Rank.CAPTAIN, "JBen", "Sisko"),
          new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
          new Officer(Rank.CAPTAIN, "Jonathan", "Archer")))
      .flatMap(dao::save)
      .then()
      .doOnEach(System.out::println)
      .block();
  }
}
