package com.workshop.meme_storage.domain.meme.entity;

import com.workshop.meme_storage.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class MemeTag extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "meme_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Meme meme;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    public static MemeTag createMemeTag(Meme meme, Tag tag) {
        MemeTag memeTag = new MemeTag();
        memeTag.setMemeFile(meme);
        memeTag.setTag(tag);

        return memeTag;
    }

    private void setMemeFile(Meme meme) {
        this.meme = meme;
        meme.getMemeTags().add(this);
    }

    private void setTag(Tag tag) {
        this.tag = tag;
        tag.getMemeTags().add(this);
    }

}
