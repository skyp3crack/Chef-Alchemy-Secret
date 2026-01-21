package com.ChefsAlchemy.backend.service;

import com.ChefsAlchemy.backend.model.Tag;
import com.ChefsAlchemy.backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service

public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Transactional
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Transactional
    public Tag createTag(String name) {
        if (tagRepository.existsByName(name)) {
            throw new IllegalArgumentException("Tag with name '" + name + "' already exists");
        }
        Tag tag = new Tag(name); // create a new tag
        return tagRepository.save(tag); // save the tag
    }

    @Transactional
    public Tag updateTag(Long id, String newName) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + id));
        if (tagRepository.existsByName(newName) && !tag.getName().equals(newName)) {
            throw new IllegalArgumentException("Tag with name '" + newName + "' already exists.");
        }
        tag.setName(newName);
        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }
}
