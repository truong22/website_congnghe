package com.devteria.hello_spring_boot.Service.Image;

import com.devteria.hello_spring_boot.Entity.Products;
import com.devteria.hello_spring_boot.Entity.image;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Respository.imageRepository;
import com.devteria.hello_spring_boot.Respository.productRepository;
import com.devteria.hello_spring_boot.Service.Products.IProductService;
import com.devteria.hello_spring_boot.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class imageService implements IimageService{
    private final IProductService productService;
    private final imageRepository imageRepository;
    @Override
    public image getImageById(Long id) {
        return  imageRepository.findById(id).orElseThrow(()->new ResoureeNotFoundException("not found"));
    }

    @Override
    public void deleteImage(Long id) {

      imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()->new  ResoureeNotFoundException("not fuond"));
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Products products= productService.getProductById(productId);
        List<ImageDto> savedImage=new ArrayList<>();
         for (MultipartFile file:files){
             try   {
                 image image=new image();
                 image.setFileName(file.getOriginalFilename());
                 image.setFileType(file.getContentType());
                 image.setImage(new SerialBlob(file.getBytes()));
                 image.setProducts(products);
                 image saveImages=imageRepository.save(image);


                 ImageDto imageDto = new ImageDto();
                 imageDto.setId(saveImages.getId());
                 imageDto.setFileName(saveImages.getFileName());
                 imageDto.setDownloadUrl("/api/v1/images/image/downloadUrl/"+saveImages.getId());
                 savedImage.add(imageDto);
             }catch (IOException| SQLException e){
                 throw new RuntimeException("Error saving image"+e.getMessage());
             }

        }

        return savedImage;
    }

    @Override
    public void UpdateImage(MultipartFile file, Long imageId) {
        image image=getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);

        }catch (IOException| SQLException e){
            throw new RuntimeException(e.getMessage());
        }


    }
}
