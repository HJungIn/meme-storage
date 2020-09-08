package com.workshop.meme_storage.domain.meme.repository;

import com.workshop.meme_storage.domain.meme.entity.Meme;
import com.workshop.meme_storage.domain.meme.entity.MemeTag;
import com.workshop.meme_storage.domain.meme.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MemeTagRepository extends JpaRepository<MemeTag, Long> {

    @Query("select mt from MemeTag mt where mt.meme = :meme and mt.deleteDate is null")
    Set<MemeTag> findMemeTagsByMemeAndDeleteDateIsNull(@Param("meme") Meme meme);

    long countByTagAndDeleteDateIsNull(Tag tag);

    //현재 밈에 사용되지 않는 태그들을 리턴
    @Query("select mt.tag from MemeTag mt where mt.tag in :tags group by mt.tag having count(mt.deleteDate) = count(mt.tag)")
    Set<Tag> findUnusedTagsByMemeTags(@Param("tags") Set<Tag> tags);

}
