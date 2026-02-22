package com.project.shopapp.Controller;

import com.project.shopapp.DTO.request.CategoryDTO;
import com.project.shopapp.Model.Category;
import com.project.shopapp.Service.impl.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
//@Validated
//Validate path variable / request param
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result)
    {
        if(result.hasErrors())
        {
            var errorMessage = result.getFieldErrors().stream().map(error-> error.getDefaultMessage()).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        categoryService.createCategory(categoryDTO);
        log.info("Controller + insertCategories");

        return ResponseEntity.ok("insertCategories");
    }
    @GetMapping("")
    public ResponseEntity<?> getAllCategories()
    {
        log.info("Controller + getALLCategories");
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PutMapping("{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO)
    {
        log.info("Controller + updateCategory + userId {}",id);
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("updateCategory");
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id)
    {
        categoryService.deleteCategory(id);
        log.info("Controller + deleteCategory + userId {}",id);
        return ResponseEntity.ok("deleteCategory");
    }
}
