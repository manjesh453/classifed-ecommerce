package com.esewashopping.product;

import com.esewashopping.product.file.FileService;
import com.esewashopping.shared.ApiResponse;
import com.esewashopping.shared.Status;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/add-product/{catId}/{cId}")
    public String addProduct(@RequestParam("image") MultipartFile image, @RequestPart ProductDto productDto, @PathVariable Integer catId, @PathVariable Integer cId) throws IOException {
        String fileName = this.fileService.uploadImage(path, image);
        return productService.addProduct(productDto, catId, cId, fileName);
    }

    @PostMapping("/update/{pid}")
    public ProductDto updateProduct(@RequestBody ProductDto productDto, @PathVariable Integer pid) {
        return productService.updateProduct(productDto, pid);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{pid}")
    public ApiResponse deleteProduct(@PathVariable Integer pid) {
        productService.deleteProduct(pid);
        return new ApiResponse("Product Deleted successfully", false);
    }

    @GetMapping("/product-by-id/{pid}")
    public ProductDto productById(@PathVariable Integer pid) {
        return productService.getById(pid);
    }

    @GetMapping("/all-product")
    public List<ProductResponseDTO> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping("/product-by-category/{catId}")
    public List<ProductDto> productByCategory(@PathVariable Integer catId) {
        return productService.productByCategory(catId);
    }

    @GetMapping("/product-by-name/{name}")
    public List<ProductDto> productByName(@PathVariable String name) {
        return productService.productByName(name);
    }

    @GetMapping("/products-add-by-customer/{cId}")
    public List<ProductDto> productsAddByCustomer(@PathVariable Integer cId) {
        return productService.productaddByCustomer(cId);
    }


    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    @GetMapping("/product-by-status/{status}")
    public List<ProductDto> productByStatus(@PathVariable String status){
        return productService.productByStatus(Status.valueOf(status));
    }

    @GetMapping("/change-status/{pid}")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeProductStatus(@PathVariable Integer pid){
        return productService.changeProductStatus(pid);
    }

}
