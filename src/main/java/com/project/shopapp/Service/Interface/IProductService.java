package com.project.shopapp.Service.Interface;

import com.project.shopapp.DTO.request.ProductDTO;
import com.project.shopapp.DTO.request.ProductImageDTO;
import com.project.shopapp.DTO.response.ProductResponse;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Exception.InvalidParamException;
import com.project.shopapp.Model.Product;
import com.project.shopapp.Model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    public Product getProductById(Long id) throws Exception;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    void deleteProduct(Long id) throws Exception;
    boolean existsByName(String name);
    Product updateProduct(Long id,ProductDTO productDTO) throws Exception;
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
}
