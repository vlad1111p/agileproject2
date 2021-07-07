package com.learngrouptu.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Integer> {
    List<Annonce> deleteByAnnonceId(Integer annonceId);
    List<Annonce> findAllByVorlName(String vorlName);
    Annonce findAnnonceByAnnonceId(Integer annonceId);
}

