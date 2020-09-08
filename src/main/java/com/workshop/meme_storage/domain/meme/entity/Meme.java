package com.workshop.meme_storage.domain.meme.entity;

import com.workshop.meme_storage.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Meme extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String script;

    @Column
    private String filePath;

    @Column
    private long fileSize;

    // TODO : 밈 파일에 대한 타입. 논의 필요.
    // TODO : 업로더에 대한 정보

    @OneToMany(mappedBy = "meme")
    private Set<MemeTag> memeTags = new HashSet<>();

    public Meme(String filePath, long fileSize, String name, String script) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.name = name;
        this.script = script;
    }

}
