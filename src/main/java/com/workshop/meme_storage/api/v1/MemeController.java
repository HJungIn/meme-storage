package com.workshop.meme_storage.api.v1;

import com.workshop.meme_storage.domain.meme.dto.MemeDto;
import com.workshop.meme_storage.domain.meme.service.MemeService;
import com.workshop.meme_storage.domain.meme.service.TagService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/meme")
public class MemeController {

    private final MemeService memeService;
    private final TagService tagService;

    @PostMapping("/register")
    @ApiOperation(value = "밈 파일 업로드")
    public void upload(@RequestParam("memeImg") MultipartFile multipartFile,
                       @RequestParam("name") String memeName,
                       @RequestParam("script") String script,
                       @RequestParam("tags") List<String> tags) {
        memeService.saveMeme(multipartFile, memeName, script, tags);
    }

    @GetMapping("/download/{memeId}")
    @ApiOperation(value = "밈 파일 다운로드")
    public ResponseEntity<Resource> download(@PathVariable long memeId) throws IOException {
        String filePath = memeService.getMemeFilePathById(memeId);
        File file = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                             //TODO : 추후 다른 MediaType (png, gif 등)을 지원
                             .contentType(MediaType.IMAGE_JPEG)
                             .contentLength(file.length())
                             .body(resource);
    }

    @DeleteMapping("/delete/{memeId}")
    @ApiOperation(value = " 밈 삭제")
    public void deleteMeme(@PathVariable long memeId) {
        memeService.deleteMeme(memeId);
    }

    @PostMapping("/addition/tag/{memeId}")
    @ApiOperation(value = " 밈에 태그 추가")
    public void createMemeTag(@PathVariable long memeId, @RequestParam String tagName) {
        tagService.saveTag(memeId, tagName);
    }

    @DeleteMapping("/delete/tag/{memeTagId}")
    @ApiOperation(value = " 밈의 태그 삭제")
    public void deleteMemeTag(@PathVariable long memeTagId) {
        tagService.removeMemeTag(memeTagId);
    }

    @GetMapping("/search/script")
    @ApiOperation(value = "대사로 밈 검색")
    public List<MemeDto> searchMemeByScript(@RequestParam String script) {
        return memeService.findMemeByScript(script);
    }

    @GetMapping("/search/tag")
    @ApiOperation(value = "태그명으로 밈 검색")
    public List<MemeDto> searchMemeByMemeTag(@RequestParam String tagName) {
        return memeService.findMemeByTagName(tagName);
    }

}
