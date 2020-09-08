package com.workshop.meme_storage.domain.meme.repository;

import com.workshop.meme_storage.domain.meme.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t where t.tagName in :tagNames and t.deleteDate is null")
    List<Tag> findByTagNames(@Param("tagNames") List<String> tagNames);

    Optional<Tag> findByTagNameAndDeleteDateIsNull(String tagName);

}
