package com.project.shopapp.Service.Interface;

import com.project.shopapp.DTO.request.UserDTO;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Exception.InvalidParamException;
import com.project.shopapp.Model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber,String password) throws DataNotFoundException, InvalidParamException;
}
