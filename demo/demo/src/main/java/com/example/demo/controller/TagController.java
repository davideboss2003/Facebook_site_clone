package com.example.demo.controller;

import com.example.demo.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.TagService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/getAll")
    @ResponseBody
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping("/createTag")
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(tag));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();  // dam 204 no content daca sterge, daca nu 404 not found
    }
}
