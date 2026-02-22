package com.project.shopapp.Controller;

import com.github.javafaker.Faker;
import com.project.shopapp.DTO.request.ProductDTO;
import com.project.shopapp.DTO.request.ProductImageDTO;
import com.project.shopapp.DTO.response.ProductListResponse;
import com.project.shopapp.DTO.response.ProductResponse;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Model.Product;
import com.project.shopapp.Model.ProductImage;
import com.project.shopapp.Service.Interface.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final IProductService productService;
    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result)
//                                           ,@ModelAttribute("files")List<MultipartFile> files)
//                                           @RequestPart("file")MultipartFile file)
    {
        log.info("Controller + createProduct");
        System.out.println(productDTO.getCategoryId());
        try{
            if(result.hasErrors())
            {
                List<String> errorMessage = result.getFieldErrors().stream().map(error-> error.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessage);

            }

            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e)
        {
            System.out.println(1);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,@ModelAttribute("files")List<MultipartFile> files)  {
        try{
            if(files == null) files = new ArrayList<>();
            if(files.size() == ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) return  ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            Product existingProduct = productService.getProductById(productId);
            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file : files) {
                if (file != null) {
                    if (file.getSize() == 0) continue;
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("file is too large");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                    }
                    String fileName = storeFile(file);
                    ProductImage productImage = productService.createProductImage(
                            existingProduct.getId(),
                            ProductImageDTO.builder()
                                    .productId(existingProduct.getId())
                                    .imageUrl(fileName).build());
                    productImages.add(productImage);
                }

            }
            return ResponseEntity.ok().body(productImages);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    )
    {
        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage= productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id){
        try{
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(ProductResponse.fromProduct(product));
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id)
    {
        try{
            productService.deleteProduct(id);
            return ResponseEntity.ok("delete product successfully");
        }catch(Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException{
        if(!isImageFile(file) || file.getOriginalFilename() == null)
        {
            throw new IOException("Invalid image file format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() + " " + fileName;
        java.nio.file.Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir))
        {
            Files.createDirectories(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(),uniqueFilename);
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
    private boolean isImageFile(MultipartFile file)
    {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    @PostMapping("/generateFakeProducts")
    public ResponseEntity<?> generateFakeProducts() throws DataNotFoundException {
        Faker faker = new Faker();
        for(int i = 0 ; i < 100000 ; i++)
        {
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)) continue;
            ProductDTO  productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10,90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(2,5))
                    .build();
            try{
                productService.createProduct(productDTO);
            }catch (Exception e)
            {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Products created successfully");
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,@RequestBody ProductDTO productDTO)  {
        try{
            Product product = productService.updateProduct(id,productDTO);
            System.out.println(ProductResponse.fromProduct(product));
            return ResponseEntity.ok().body(ProductResponse.fromProduct(product));
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
