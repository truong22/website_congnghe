package com.devteria.hello_spring_boot.controller;

import com.devteria.hello_spring_boot.Entity.image;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Response.ApiResponse;
import com.devteria.hello_spring_boot.Service.Image.IimageService;
import com.devteria.hello_spring_boot.dto.ImageDto;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
@Controller
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images/")
public class imageController {
    private  final IimageService iimageService;
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id){
        try{
            image image=iimageService.getImageById(id);
            if(image !=null){
                iimageService.deleteImage(id);
            }
            return ResponseEntity.ok(new ApiResponse("Ok",null));
        }catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e ){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete feu",null));
        }
    }
    @PutMapping("image/update/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam("file")MultipartFile file){
        try{
            image image=iimageService.getImageById(imageId);
            if(image !=null){
                iimageService.UpdateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("update success!",null));
            }
            return ResponseEntity.ok(new ApiResponse("Ok",null));
        }catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e ){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed",INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("image/upload")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam Long id, @RequestParam List<MultipartFile> file){
        try{
            List<ImageDto> image=iimageService.saveImages(id,file);
                return ResponseEntity.ok(new ApiResponse("upload success!",image));

        }catch (Exception e ){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/image/downloadUrl/{id}") //Resource tra ve file
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException {
        try {
            // Lấy image từ database
            image image = iimageService.getImageById(id);

            // Kiểm tra tồn tại
            if (image == null) {
                throw new ResoureeNotFoundException("Không tìm thấy hình ảnh với id: " + id);
            }

            // Lấy dữ liệu Blob từ database
            Blob imageBlob = image.getImage();
            byte[] bytes = imageBlob.getBytes(1, (int) imageBlob.length());

            // Tạo resource
            ByteArrayResource resource = new ByteArrayResource(bytes);

            // Trả về response
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                    .body(resource);

        } catch (ResoureeNotFoundException e) {
            throw e; // Hoặc xử lý theo cách khác
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tải hình ảnh: " + e.getMessage(), e);
        }
    }
}
