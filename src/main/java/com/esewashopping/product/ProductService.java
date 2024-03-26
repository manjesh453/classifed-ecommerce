package com.esewashopping.product;

import com.esewashopping.shared.Status;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    String addProduct(ProductDto productDto,Integer catId,Integer cId ,String fileName) throws IOException;

    ProductDto updateProduct(ProductDto productDto,Integer pid);

    void deleteProduct(Integer pid);

    ProductDto getById(Integer pid);

    List<ProductResponseDTO> getAllProduct();

    List<ProductDto> productByCategory(Integer catId);

    List<ProductDto>productByName(String name);

    List<ProductDto>productaddByCustomer(Integer cId);

    List<ProductDto> productByStatus(Status status);

    String changeProductStatus(Integer pId);

}
