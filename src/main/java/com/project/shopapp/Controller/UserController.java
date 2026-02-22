package com.project.shopapp.Controller;

import com.project.shopapp.DTO.request.UserDTO;
import com.project.shopapp.DTO.request.UserLoginDTO;
import com.project.shopapp.DTO.response.TokenResponse;
import com.project.shopapp.Model.User;
import com.project.shopapp.Service.Interface.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDto, BindingResult result)
    {
        log.info("Controller + createUser");
        try{
            if(result.hasErrors())
            {
                List<String> errorMessage = result.getFieldErrors().stream().map(error-> error.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessage);

            }
            if(!userDto.getPassword().equals(userDto.getRetypePassword()))
            {
                return ResponseEntity.badRequest().body("password does not match");
            }
            User user = userService.createUser(userDto);
            return ResponseEntity.ok(user);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO,BindingResult result)
    {
        try{
            if(result.hasErrors())
            {
                List<String> errorMessage = result.getFieldErrors().stream().map(error-> error.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessage);

            }
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(TokenResponse.builder().token(token).build());
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
