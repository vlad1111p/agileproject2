package com.learngrouptu.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VorlesungRepository extends JpaRepository<Vorlesung, Integer> {
    Vorlesung findVorlesungByKursnr(String kursnr);
    List<Vorlesung> findVorlesungsByKursnr(String kursnr);
}
