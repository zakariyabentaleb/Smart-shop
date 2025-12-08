package com.smartshopp.service;

import static org.junit.jupiter.api.Assertions.*;
import com.smartshopp.dto.ProductDTO;
import com.smartshopp.mapper.ProductMapper;
import com.smartshopp.model.Product;
import com.smartshopp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productRequest;
    private ProductDTO productResponse;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setNom("Test Product");
        product.setPrixUnitaire(100);
        product.setStockDisponible(50);

        productRequest = new ProductDTO();
        productRequest.setNom("Test Product");
        productRequest.setPrixUnitaire(100);
        productRequest.setStockDisponible(50);

        productResponse = new ProductDTO();
        productResponse.setId(1L);
        productResponse.setNom("Test Product");
    }

    @Test
    void testCreateProduct() {
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productResponse);

        ProductDTO response = productService.createProduct(productRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toDTO(product)).thenReturn(productResponse);

        Page<ProductDTO> result = productService.getAllProducts(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(productResponse);

        ProductDTO result = productService.getProductById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(99L));
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productResponse);

        ProductDTO result = productService.updateProduct(1L, productRequest);

        assertThat(result).isNotNull();
        assertThat(product.getNom()).isEqualTo(productRequest.getNom());
        assertThat(product.getPrixUnitaire()).isEqualTo(productRequest.getPrixUnitaire());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.updateProduct(99L, productRequest));
    }

    @Test
    void testDeleteProduct_WhenProductExists_ShouldDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(99L));
    }
}