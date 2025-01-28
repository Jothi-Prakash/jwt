package com.jwt.services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.DTO.LoginRequestDTO;
import com.jwt.DTO.LoginResponseDTO;
import com.jwt.DTO.SignUpRequestDTO;
import com.jwt.model.UserModel;
import com.jwt.repository.UserRepository;
import com.jwt.utils.JwtUtils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	JwtUtils jwtUtils;

	@Override
	public void signup(SignUpRequestDTO signUpRequestDTO) {
	   
	    Optional<UserModel> existingUser = userRepository.findByEmailId(signUpRequestDTO.getEmailId());
	    if (existingUser.isPresent()) {
	        throw new RuntimeException("Email already exists");
	    }

	    String encryptedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());
	    
	    UserModel newUser = new UserModel();
	    newUser.setName(signUpRequestDTO.getName());
	    newUser.setEmailId(signUpRequestDTO.getEmailId());
	    newUser.setPassword(encryptedPassword);
	    newUser.setPhoneNumber(signUpRequestDTO.getPhoneNumber());
	    
	    userRepository.save(newUser);
	}

	@Override
	 public Optional<LoginResponseDTO> login(LoginRequestDTO loginRequest) {

        Optional<UserModel> user = userRepository.findByEmailId(loginRequest.getEmailId());

        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            
            String accessToken = jwtUtils.generateJwt(user.get());

            String refreshToken = jwtUtils.generateRefreshToken(user.get());

            return Optional.of(new LoginResponseDTO(accessToken, refreshToken));
        }
        
        return Optional.empty();
    }

	@Override
	public List<UserModel> findAllUser() {
		
		
		return userRepository.findAll();
		
	}



}
