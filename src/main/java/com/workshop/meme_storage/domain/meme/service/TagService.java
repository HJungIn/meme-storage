package com.workshop.meme_storage.domain.meme.service;

import com.workshop.meme_storage.domain.meme.entity.Meme;
import com.workshop.meme_storage.domain.meme.entity.MemeTag;
import com.workshop.meme_storage.domain.meme.entity.Tag;
import com.workshop.meme_storage.domain.meme.repository.MemeRepository;
import com.workshop.meme_storage.domain.meme.repository.MemeTagRepository;
import com.workshop.meme_storage.domain.meme.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final MemeRepository memeRepository;
    private final TagRepository tagRepository;
    private final MemeTagRepository memeTagRepository;

    public void saveTags(Meme meme, List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }

        List<Tag> existTags = tagRepository.findByTagNames(tags);
        tags.removeAll(getTagNames(existTags));

        List<Tag> nonExistTags = tags.stream().map(Tag::new).collect(Collectors.toList());
        tagRepository.saveAll(nonExistTags);
        existTags.addAll(nonExistTags);

        saveMemeTags(meme, existTags);
    }

    public void saveTag(long memeId, String tagName) {
        if (StringUtils.isEmpty(tagName)) {
            return;
        }

        Meme meme = memeRepository.findById(memeId).orElseThrow(() -> new IllegalArgumentException("Meme not exist"));
        Optional<Tag> tag = tagRepository.findByTagNameAndDeleteDateIsNull(tagName);

        if (tag.isPresent()) {
            saveMemeTag(meme, tag.get());
        } else {
            Tag newTag = new Tag(tagName);
            tagRepository.save(newTag);
            saveMemeTag(meme, newTag);
        }
    }

    public void removeMemeTag(long memeTagId) {
        MemeTag memeTag = memeTagRepository.findById(memeTagId).orElseThrow(() -> new IllegalArgumentException("MemeTag not exist"));
        memeTag.setDeleteDate(LocalDateTime.now());
        if (memeTagRepository.countByTagAndDeleteDateIsNull(memeTag.getTag()) == 0) {
            memeTag.getTag().setDeleteDate(LocalDateTime.now());
        }
    }

    public void removeTags(Meme meme) {
        Set<MemeTag> memeTags = memeTagRepository.findMemeTagsByMemeAndDeleteDateIsNull(meme);
        memeTags.forEach(memeTag -> memeTag.setDeleteDate(LocalDateTime.now()));
        Set<Tag> tags = memeTags.stream().map(MemeTag::getTag).collect(Collectors.toSet());

        Set<Tag> unusedTags = memeTagRepository.findUnusedTagsByMemeTags(tags);
        unusedTags.forEach(tag -> tag.setDeleteDate(LocalDateTime.now()));
    }

    public Tag getTagByTagName(String tagName) {
        return tagRepository.findByTagNameAndDeleteDateIsNull(tagName).orElse(null);
    }

    private void saveMemeTags(Meme meme, List<Tag> existTags) {
        List<MemeTag> memeTags = existTags.stream().map(tag -> MemeTag.createMemeTag(meme, tag)).collect(Collectors.toList());
        memeTagRepository.saveAll(memeTags);
    }

    private void saveMemeTag(Meme meme, Tag tag) {
        MemeTag memeTag = MemeTag.createMemeTag(meme, tag);
        memeTagRepository.save(memeTag);
    }

    private List<String> getTagNames(List<Tag> tags) {
        return tags.stream()
                   .map(Tag::getTagName)
                   .collect(Collectors.toList());
    }

}
