package com.workshop.meme_storage.domain.meme.service;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.workshop.meme_storage.domain.meme.dto.MemeDto;
import com.workshop.meme_storage.domain.meme.entity.Meme;
import com.workshop.meme_storage.domain.meme.entity.MemeTag;
import com.workshop.meme_storage.domain.meme.entity.Tag;
import com.workshop.meme_storage.domain.meme.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemeService {

    private final TagService tagService;
    private final MemeRepository memeRepository;

    private final HashFunction hashFunction = Hashing.murmur3_128();
    private static final String memeFilePathFormat = "%s/meme/%s/%s/%s/%s.%s";
    private static final String absoluteRootDirectoryPath = System.getProperty("user.dir");

    public void saveMeme(MultipartFile file, String memeName, String script, List<String> tags) {
        File savedFile = saveMemeImg(file);
        Meme meme = new Meme(savedFile.getPath().replace(absoluteRootDirectoryPath, ""), savedFile.length(), memeName, script);
        memeRepository.save(meme);
        tagService.saveTags(meme, tags);
    }

    public String getMemeFilePathById(long id) {
        Meme meme = memeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("File is Not Exist"));
        return absoluteRootDirectoryPath + meme.getFilePath();
    }

    public void deleteMeme(long memeId) {
        Meme meme = memeRepository.findById(memeId).orElseThrow(() -> new IllegalArgumentException("Meme is Not Exist"));
        meme.setDeleteDate(LocalDateTime.now());
        tagService.removeTags(meme);
    }

    public List<MemeDto> findMemeByScript(String script) {
        List<Meme> memeList = memeRepository.findByScriptAndDeleteDateIsNull(script);
        return memeList.stream().map(MemeDto::convert).collect(Collectors.toList());
    }


    public List<MemeDto> findMemeByTagName(String tagName) {
        Tag tag = tagService.getTagByTagName(tagName);
        List<Meme> memeList = tag.getMemeTags().stream().map(MemeTag::getMeme).collect(Collectors.toList());
        return memeList.stream().map(MemeDto::convert).collect(Collectors.toList());
    }

    // TODO : 실제 이미지 파일인지, 확장자가 제대로 설정되어있는지 검증 필요
    private File saveMemeImg(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String originalFileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String modifiedFileName = getNameByHash(originalFilename);
        LocalDate localDate = LocalDate.now();

        // 파일은 리소스 폴더 아래에 meme / 년 / 월 / 일 / 파일명으로 생성된다.
        String filePath = String.format(memeFilePathFormat, absoluteRootDirectoryPath, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), modifiedFileName, originalFileExtension);
        String saveFolderPath = filePath.substring(0, filePath.lastIndexOf("/"));
        File dest = new File(filePath);
        try {
            checkSaveFolder(saveFolderPath);
            file.transferTo(dest);
        } catch (IOException e) {
            // TODO : 추후 exception 추가 예정
            e.printStackTrace();
        }
        return dest;
    }

    // 파일이 저장 될 폴더가 존재하는지 확인하고, 미존재시 폴더 생성
    private void checkSaveFolder(String saveFolderPath) throws IOException {
        File saveFolder = new File(saveFolderPath);
        if (!saveFolder.exists()) {
            Files.createDirectories(Paths.get(saveFolderPath));
        }
    }

    // 파일 이름 + 현재 시간 데이터를 해시화 하여 파일 이름으로 저장 (겹치는 이름이 없도록 하기 위함)
    private String getNameByHash(String fileName) {
        return hashFunction.newHasher()
                           .putString(fileName, Charsets.UTF_8)
                           .putLong(System.currentTimeMillis())
                           .hash()
                           .toString();
    }

}
