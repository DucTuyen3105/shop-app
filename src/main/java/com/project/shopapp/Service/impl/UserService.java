package com.project.shopapp.Service.impl;

import com.project.shopapp.Components.JwtTokenUtil;
import com.project.shopapp.DTO.request.UserDTO;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Exception.InvalidParamException;
import com.project.shopapp.Exception.PermissionDeniedException;
import com.project.shopapp.Model.Role;
import com.project.shopapp.Model.User;
import com.project.shopapp.Repository.RoleRepository;
import com.project.shopapp.Repository.UserRepository;
import com.project.shopapp.Service.Interface.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber()))
        {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role =  roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN))
        {
            throw new PermissionDeniedException("You cannot register an admin account");
        }
            User newUser = User.builder().
                    fullName(userDTO.getFullName())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .password(userDTO.getPassword())
                    .address(userDTO.getAddress())
                    .dateOfBirth(userDTO.getDateOfBirth())
                    .facebookAccountId(userDTO.getFacebookAccountId())
                    .googleAccountId(userDTO.getGoogleAccountId()).build();
            newUser.setRoles(role);
            if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0)
            {
                String password = userDTO.getPassword();
                String encodedPassword = passwordEncoder.encode(password);
                newUser.setPassword(encodedPassword);
            }
            return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException {
        Optional<User> optionalUser =  userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty())
        {
            throw new DataNotFoundException("Invalid username / password");
        }
        User existingUser = optionalUser.get();
        if(existingUser.getGoogleAccountId() == 0 && existingUser.getFacebookAccountId() == 0)
        {
            if(!passwordEncoder.matches(password,existingUser.getPassword()))
            {
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(phoneNumber,password,existingUser.getAuthorities());
        System.out.println(existingUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
