package com.endora.api.features.product.service.impl;

import com.endora.api.common.exception.BusinessException;
import com.endora.api.common.exception.ResourceNotFoundException;
import com.endora.api.features.product.dto.ProductCreateRequest;
import com.endora.api.features.product.dto.ProductResponse;
import com.endora.api.features.product.dto.ProductUpdateRequest;
import com.endora.api.features.product.model.Product;
import com.endora.api.features.product.repository.ProductRepository;
import com.endora.api.features.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private static final int MAX_PRODUCTS = 100;
    private static final int DEFAULT_PRODUCTS_COUNT = 15;

    private final ProductRepository productRepository;

    @PostConstruct
    public void initializeDefaultData() {
        if (productRepository.count() == 0) {
            initializeDefaultProducts();
        }
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.countProducts() >= MAX_PRODUCTS) {
            throw new BusinessException("Maximum number of products (100) reached. Cannot add more products.");
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Created new product with id: {}", savedProduct.getId());
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        Product updatedProduct = productRepository.save(product);
        log.info("Updated product with id: {}", updatedProduct.getId());
        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        log.info("Deleted product with id: {}", id);
    }

    @Override
    public Map<String, Object> getDatabaseInfo() {
        long count = productRepository.count();
        return Map.of(
                "totalProducts", count,
                "maxProducts", MAX_PRODUCTS,
                "remainingSlots", MAX_PRODUCTS - count,
                "resetInfo", "Database resets daily at midnight to " + DEFAULT_PRODUCTS_COUNT + " default products"
        );
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetToDefaultData() {
        productRepository.deleteAllProducts();
        initializeDefaultProducts();
        log.info("Database reset to default {} products at: {}", DEFAULT_PRODUCTS_COUNT,
                java.time.LocalDateTime.now());
    }

    @Transactional
    protected void initializeDefaultProducts() {
        List<Product> defaultProducts = List.of(
                Product.builder().name("Laptop").description("High-performance laptop for professional use")
                        .price(new BigDecimal("999.99")).quantity(50).build(),
                Product.builder().name("Smartphone").description("Latest smartphone with advanced features")
                        .price(new BigDecimal("699.99")).quantity(100).build(),
                Product.builder().name("Tablet").description("Lightweight tablet perfect for reading and browsing")
                        .price(new BigDecimal("299.99")).quantity(75).build(),
                Product.builder().name("Headphones").description("Wireless noise-canceling headphones")
                        .price(new BigDecimal("199.99")).quantity(200).build(),
                Product.builder().name("Keyboard").description("Mechanical gaming keyboard with RGB lighting")
                        .price(new BigDecimal("149.99")).quantity(150).build(),
                Product.builder().name("Mouse").description("Ergonomic wireless mouse")
                        .price(new BigDecimal("79.99")).quantity(300).build(),
                Product.builder().name("Monitor").description("4K Ultra HD monitor 27 inch")
                        .price(new BigDecimal("399.99")).quantity(40).build(),
                Product.builder().name("Webcam").description("HD webcam with built-in microphone")
                        .price(new BigDecimal("89.99")).quantity(120).build(),
                Product.builder().name("Speakers").description("Bluetooth portable speakers")
                        .price(new BigDecimal("129.99")).quantity(80).build(),
                Product.builder().name("Power Bank").description("10000mAh portable charger")
                        .price(new BigDecimal("49.99")).quantity(250).build(),
                Product.builder().name("USB Cable").description("High-speed USB-C cable")
                        .price(new BigDecimal("19.99")).quantity(500).build(),
                Product.builder().name("Phone Case").description("Protective phone case with screen protector")
                        .price(new BigDecimal("29.99")).quantity(400).build(),
                Product.builder().name("Desk Lamp").description("LED desk lamp with adjustable brightness")
                        .price(new BigDecimal("69.99")).quantity(90).build(),
                Product.builder().name("Router").description("High-speed WiFi 6 router")
                        .price(new BigDecimal("179.99")).quantity(60).build(),
                Product.builder().name("External HDD").description("1TB external hard drive")
                        .price(new BigDecimal("79.99")).quantity(110).build()
        );

        productRepository.saveAll(defaultProducts);
        log.info("Initialized {} default products", defaultProducts.size());
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
