package com.project.shopapp.Service.impl;

import com.project.shopapp.DTO.request.ProductDTO;
import com.project.shopapp.DTO.request.ProductImageDTO;
import com.project.shopapp.DTO.response.ProductResponse;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Exception.InvalidParamException;
import com.project.shopapp.Model.Category;
import com.project.shopapp.Model.Product;
import com.project.shopapp.Model.ProductImage;
import com.project.shopapp.Repository.CategoryRepository;
import com.project.shopapp.Repository.ProductImageRepository;
import com.project.shopapp.Repository.ProductRepository;
import com.project.shopapp.Service.Interface.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new DataNotFoundException("can not find category with id"));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .thumbnail("")
                .category(existingCategory).build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find product"));
    }
//    ProductResponse.builder()
//            .name(product.getName())
//            .price(product.getPrice())
//            .thumbnail(product.getThumbnail())
//            .description(product.getDescription())
//            .categoryId(product.getCategory().getId())
//            .build();
    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest)
                .map(ProductResponse::fromProduct);
    }

    @Override
    public void deleteProduct(Long id) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Product updateProduct(Long id,ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if(existingProduct != null)
        {
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new DataNotFoundException("can not find category with id"));
            System.out.println(existingCategory);
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException("cannot find product with id"));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        Integer size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT)
        {
            throw new InvalidParamException("Number of image must be <= " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT );
        }
        return productImageRepository.save(newProductImage);
    }
}
