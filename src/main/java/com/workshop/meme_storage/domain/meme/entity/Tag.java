package com.workshop.meme_storage.domain.meme.entity;

import com.workshop.meme_storage.domain.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode(of = {"id", "tagName"}, callSuper = false)
@NoArgsConstructor
public class Tag extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tagName;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemeTag> memeTags = new ArrayList<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }

}
