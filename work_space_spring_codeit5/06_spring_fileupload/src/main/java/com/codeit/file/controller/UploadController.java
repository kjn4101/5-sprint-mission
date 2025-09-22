package com.codeit.file.controller;


import com.codeit.file.config.FileConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final FileConfig fileConfig;

    // 업로드 폼
    @GetMapping("/upload")
    public String upload(){
        return "file/upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return "파일이 비었습니다";
        }
        String uploadDir = fileConfig.getUploadDir();
        String filePath = uploadDir + File.separator + file.getOriginalFilename();
        file.transferTo(Path.of(filePath));
        return "업로드 성공! " + file.getOriginalFilename();
    }


    @PostMapping("/uploadMultiple")
    @ResponseBody
    public String uploadMultiple(@RequestParam("files") List<MultipartFile> files) throws IOException {
        String uploadDir = fileConfig.getUploadDir();
        StringBuilder sb = new StringBuilder();
        for(MultipartFile file : files){
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(Path.of(filePath));
            sb.append(file.getOriginalFilename() +"\n");
        }
        return sb.toString();
    }

    @PostMapping("/uploadWithRename")
    @ResponseBody
    public String uploadWithRename(@RequestParam MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return "파일이 비었습니다.";
        }

        String uploadDir = fileConfig.getUploadDir();
        // 날짜 + 랜던값 + 확장 조합 rename 로직
        String originalFileName = file.getOriginalFilename();
        String ext = ""; // 확장자, .jpg, .png, pdf ...
        if(originalFileName != null && originalFileName.contains(".")){
            ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String random = UUID.randomUUID().toString().substring(0, 8); // 짧은 랜덤값
        String savedName = date + "_" + random+ "." + ext;

        String filePath = uploadDir + File.separator + savedName;
        file.transferTo(Path.of(filePath));
        return "savedName : " + savedName +"\n" + "originName : " + file.getOriginalFilename();
    }


}



















