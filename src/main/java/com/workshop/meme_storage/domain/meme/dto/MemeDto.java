package com.workshop.meme_storage.domain.meme.dto;

import com.workshop.meme_storage.domain.meme.entity.Meme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MemeDto {
    private long id;
    private String name;
    private String script;
    private String filePath;
    private long fileSize;
    private List<TagDto> tags;

    public static MemeDto convert(Meme meme) {
        return MemeDto.builder()
                      .id(meme.getId())
                      .name(meme.getName())
                      .script(meme.getScript())
                      .filePath(meme.getFilePath())
                      .fileSize(meme.getFileSize())
                      .tags(meme.getMemeTags().stream().map(TagDto::convert).collect(Collectors.toList()))
                      .build();
    }

}
