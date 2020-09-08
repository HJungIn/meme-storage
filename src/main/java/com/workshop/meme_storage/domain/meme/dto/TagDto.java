package com.workshop.meme_storage.domain.meme.dto;

import com.workshop.meme_storage.domain.meme.entity.MemeTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class TagDto {
    private long memeTagId;
    private long tagId;
    private String tagName;
    private LocalDateTime createdDate;

    public static TagDto convert(MemeTag memeTag) {
        return TagDto.builder()
                     .memeTagId(memeTag.getId())
                     .tagId(memeTag.getTag().getId())
                     .tagName(memeTag.getTag().getTagName())
                     .createdDate(memeTag.getCreatedDate())
                     .build();
    }
}
