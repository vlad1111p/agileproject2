package com.learngrouptu.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnonceRepository extends CrudRepository<Annonce, Integer> {

}
