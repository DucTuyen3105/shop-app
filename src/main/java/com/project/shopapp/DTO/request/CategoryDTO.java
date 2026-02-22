package com.project.shopapp.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Data
@Getter
@Setter
@ToString
public class CategoryDTO {
    @NotEmpty(message = "name can not be empty")
    String name;
}
