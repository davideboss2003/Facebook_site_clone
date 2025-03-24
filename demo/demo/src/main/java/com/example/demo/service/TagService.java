package com.example.demo.service;

import com.example.demo.entity.Tag;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {     //read
        return tagRepository.findAll();
    }

    public Tag createTag(Tag tag) {    //create
        if (tagRepository.existsByName(tag.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag with this name already exists!");
        }
        return tagRepository.save(tag);
    }

    public void deleteById(Long id) {     //delete
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            tagRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag with id " + id + " not found");
        }
    }
    //update nu cred ca avem nevoie
}
