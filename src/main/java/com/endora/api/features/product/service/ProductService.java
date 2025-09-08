package com.endora.api.features.product.service;

import com.endora.api.features.product.dto.ProductCreateRequest;
import com.endora.api.features.product.dto.ProductResponse;
import com.endora.api.features.product.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductService {
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> searchProducts(String name, Pageable pageable);
    ProductResponse getProductById(Integer id);
    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(Integer id, ProductUpdateRequest request);
    void deleteProduct(Integer id);
    Map<String, Object> getDatabaseInfo();
    void resetToDefaultData();
}