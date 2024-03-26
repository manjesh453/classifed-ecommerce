package com.esewashopping.product.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public interface FileService {

     String uploadImage(String path, MultipartFile file) throws IOException;

     InputStream getResource(String path, String filename) throws FileNotFoundException;

     void deleteImage(String path,String filename) throws IOException;
}
