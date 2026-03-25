package com.devteria.hello_spring_boot.Service.Image;

import com.devteria.hello_spring_boot.Entity.image;
import com.devteria.hello_spring_boot.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IimageService {
    image getImageById(Long id);
    void deleteImage(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile>files);
    void UpdateImage(MultipartFile file,Long imageId);
}
