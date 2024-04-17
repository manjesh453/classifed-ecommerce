package com.esewashopping.product;

import com.esewashopping.category.Category;
import com.esewashopping.category.CategoryRepo;
import com.esewashopping.customer.Customer;
import com.esewashopping.customer.CustomerRepo;
import com.esewashopping.exception.ResourceNotFoundException;
import com.esewashopping.product.file.FileService;
import com.esewashopping.product.file.FileServiceImpl;
import com.esewashopping.shared.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FileServiceImpl fileService;

    @Test
    void addProduct_When_New_Product_Is_Added() throws IOException {
        when(customerRepo.findById(1)).thenReturn(java.util.Optional.of(dummyCustomer()));
        when(categoryRepo.findById(1)).thenReturn(java.util.Optional.of(dummyCategory()));
        when(productRepo.findByNameForCustomer(anyString(), any(Customer.class))).thenReturn(null);
        when(modelMapper.map(dummyProductDto(), Product.class)).thenReturn(dummyProduct());
        when(productRepo.save(any())).thenReturn(dummyProduct());
        String expectes = "Successfully added Product";
        String actual = productService.addProduct(dummyProductDto(), 1, 1, "images");
        assertEquals(expectes, actual);

    }
    @Test
    void addProduct_When_Existing_Product_Is_Added() throws IOException {
        when(customerRepo.findById(1)).thenReturn(java.util.Optional.of(dummyCustomer()));
        when(modelMapper.map(dummyProductDto(),Product.class)).thenReturn(dummyProduct());
        when(productRepo.findByNameForCustomer(anyString(), any(Customer.class))).thenReturn(dummyProduct());
        when(productRepo.save(any())).thenReturn(dummyProduct());
        String expected="Product already exist but Quantity increase";
        String actual=productService.addProduct(dummyProductDto(),1,1,"images");
        assertEquals(expected,actual);
    }

    @Test
    void updateProduct() {
        when(productRepo.findById(1)).thenReturn(java.util.Optional.of(dummyProduct()));
        when(modelMapper.map(dummyNewProductDto(), Product.class)).thenReturn(dummyNewProduct());
        when(productRepo.save(any())).thenReturn(dummyNewProduct());
        when(modelMapper.map(dummyNewProduct(), ProductDto.class)).thenReturn(dummyNewProductDto());
        ProductDto actual = productService.updateProduct(dummyNewProductDto(), 1);
        assertEquals(dummyNewProductDto(), actual);

    }

    @Test
    void deleteProduct() {
        when(productRepo.findById(1)).thenReturn(java.util.Optional.of(dummyProduct()));
        doNothing().when(productRepo).delete(dummyProduct());
        productService.deleteProduct(1);
        verify(productRepo).delete(dummyProduct());
    }

    @Test
    void getById() {
        when(productRepo.findById(1)).thenReturn(java.util.Optional.of(dummyProduct()));
        when(modelMapper.map(dummyProduct(), ProductDto.class)).thenReturn(dummyProductDto());
        ProductDto actual = productService.getById(1);
        assertEquals(dummyProductDto(), actual);
    }

    @Test
    void getAllProduct() {
        List<Product> products = new ArrayList<>();
        products.add(dummyProduct());
        List<ProductResponseDTO> productDtos = new ArrayList<>();
        productDtos.add(dummyProductResponseDto());
        when(productRepo.findProductByStatus(Status.UNVERIFIED)).thenReturn(products);
        when(modelMapper.map(products.get(0), ProductResponseDTO.class)).thenReturn(productDtos.get(0));
        List<ProductResponseDTO> result = productService.getAllProduct();
        assertEquals(productDtos.get(0), result.get(0));
    }

    @Test
    void productByCategory() {
        List<Product> products = Arrays.asList(dummyProduct());
        List<ProductDto> productDtos = Arrays.asList(dummyProductDto());
        when(categoryRepo.findById(1)).thenReturn(java.util.Optional.of(dummyCategory()));
        when(productRepo.findByCategory(dummyCategory())).thenReturn(products);
        when(modelMapper.map(products.get(0), ProductDto.class)).thenReturn(dummyProductDto());
        List<ProductDto> actual = productService.productByCategory(1);
        assertEquals(productDtos.get(0), actual.get(0));
    }

    @Test
    void productByName() {
        List<Product> products = Arrays.asList(dummyProduct());
        List<ProductDto> productDtos = Arrays.asList(dummyProductDto());
        when(productRepo.findByNameContains("Apple")).thenReturn(products);
        when(modelMapper.map(products.get(0), ProductDto.class)).thenReturn(dummyProductDto());
        List<ProductDto> actual = productService.productByName("Apple");
        assertEquals(productDtos.get(0), actual.get(0));
    }

    @Test
    void productaddByCustomer() {
        List<Product> products = Arrays.asList(dummyProduct());
        List<ProductDto> productDtos = Arrays.asList(dummyProductDto());
        when(customerRepo.findById(1)).thenReturn(java.util.Optional.of(dummyCustomer()));
        when(productRepo.findByCustomer(dummyCustomer())).thenReturn(products);
        when(modelMapper.map(products.get(0), ProductDto.class)).thenReturn(dummyProductDto());
        List<ProductDto> actual = productService.productaddByCustomer(1);
        assertEquals(productDtos.get(0), actual.get(0));
    }

    @Test
    void productByStatus() {
        List<Product> products = Arrays.asList(dummyProduct());
        List<ProductDto> productDtos = Arrays.asList(dummyProductDto());
        when(productRepo.findProductByStatus(Status.ACTIVE)).thenReturn(products);
        when(modelMapper.map(products.get(0), ProductDto.class)).thenReturn(dummyProductDto());
        List<ProductDto> actual = productService.productByStatus(Status.ACTIVE);
        assertEquals(productDtos.get(0), actual.get(0));
    }

    @Test
    void changeProductStatus() {
        when(productRepo.findById(1)).thenReturn(java.util.Optional.of(dummyProduct()));
        when(productRepo.save(any())).thenReturn(dummyProduct());
        productService.changeProductStatus(1);
        verify(productRepo).findById(1);
    }

    @Test
    void resourceNotFoundException() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getById(1);
        });
        String expected = "Product not found with ProductId : 1";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }


    private static Product dummyProduct() {
        Product product = new Product();
        product.setPid(1);
        product.setName("Apple");
        product.setProductImage("images");
        product.setDescription("My Apple Description");
        product.setPrice(143 % 1f);
        product.setStatus(Status.ACTIVE);
        product.setQuantity(50);
        return product;
    }

    private static Customer dummyCustomer() {
        return Customer.builder()
                .cId(1)
                .fullName("Old Name")
                .email("oldemail@example.com")
                .password("hdwfuewfuefre")
                .address("Old Address")
                .contact("Old Contact")
                .status(Status.ACTIVE)
                .build();
    }

    private static Category dummyCategory() {
        return new Category(1, "fruits");
    }

    private static ProductDto dummyProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Apple");
        productDto.setProductImage("images");
        productDto.setDescription("My Apple Description");
        productDto.setPrice(143 % 1f);
        productDto.setQuantity(50);
        return productDto;
    }

    private static Product dummyNewProduct() {
        Product product = new Product();
        product.setPid(1);
        product.setName("Mango");
        product.setProductImage("mangoimages");
        product.setDescription("My Mango Description");
        product.setPrice(153 % 1f);
        product.setStatus(Status.ACTIVE);
        product.setQuantity(60);
        return product;
    }

    private static ProductDto dummyNewProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Mango");
        productDto.setProductImage("mangoimages");
        productDto.setDescription("My Mango Description");
        productDto.setPrice(153 % 1f);
        productDto.setQuantity(60);
        return productDto;
    }

    private static ProductResponseDTO dummyProductResponseDto() {
        ProductResponseDTO productDto = new ProductResponseDTO();
        productDto.setPid(1);
        productDto.setName("Apple");
        productDto.setProductImage("images");
        productDto.setDescription("My Apple Description");
        productDto.setPrice(143 % 1f);
        productDto.setQuantity(50);
        return productDto;
    }

}