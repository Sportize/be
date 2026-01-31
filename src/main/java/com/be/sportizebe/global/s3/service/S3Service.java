package com.be.sportizebe.global.s3.service;

import com.be.sportizebe.global.s3.enums.PathName;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

  // 파일 업로드
  String uploadFile(PathName pathName, MultipartFile file);

  // 파일 삭제
  void deleteFile(String fileUrl);
}
