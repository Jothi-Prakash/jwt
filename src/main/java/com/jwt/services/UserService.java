package com.jwt.services;

import java.util.List;
import java.util.Optional;

import com.jwt.DTO.LoginRequestDTO;
import com.jwt.DTO.LoginResponseDTO;
import com.jwt.DTO.SignUpRequestDTO;
import com.jwt.model.UserModel;

public interface UserService {

	void signup(SignUpRequestDTO signUpRequestDTO);

	public Optional<LoginResponseDTO> login(LoginRequestDTO loginRequest);

	List<UserModel> findAllUser();

}
