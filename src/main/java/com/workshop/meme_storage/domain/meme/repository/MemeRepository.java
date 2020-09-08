package com.workshop.meme_storage.domain.meme.repository;

import com.workshop.meme_storage.domain.meme.entity.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemeRepository extends JpaRepository<Meme, Long> {

    @Query("select meme from Meme meme where meme.script like concat('%',:script,'%') and meme.deleteDate is null order by meme.createdDate desc")
    List<Meme> findByScriptAndDeleteDateIsNull(@Param("script") String script);

}
