package com.ChefsAlchemy.backend.controller;

import com.ChefsAlchemy.backend.model.Tag;
import com.ChefsAlchemy.backend.service.TagService;
import com.ChefsAlchemy.backend.dto.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags().stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can create tags
    public ResponseEntity<Tag> createTag(@RequestBody String name) {
        try {
            Tag newTag = tagService.createTag(name);
            return new ResponseEntity<>(newTag, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict if name exists
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        return tagService.getTagById(id)
                .map(tag -> new ResponseEntity<>(tag, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can update tags
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody String name) {
        try {
            Tag updatedTag = tagService.updateTag(id, name);
            return new ResponseEntity<>(updatedTag, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete tags
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        try {
            tagService.deleteTag(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
