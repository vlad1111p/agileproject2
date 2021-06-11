package com.learngrouptu.services;

import com.learngrouptu.models.Annonce;
import com.learngrouptu.models.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public List<Annonce> list() {
        return annonceRepository.findAll();
    }

}
