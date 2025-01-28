package com.jwt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.DTO.LoginRequestDTO;
import com.jwt.DTO.LoginResponseDTO;
import com.jwt.DTO.RequestMeta;
import com.jwt.DTO.SignUpRequestDTO;
import com.jwt.model.UserModel;
import com.jwt.repository.UserRepository;
import com.jwt.services.UserService;
import com.jwt.utils.JwtUtils;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RequestMeta requestMeta;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		HashMap<String, Object> response = new HashMap<>();
		if (signUpRequestDTO == null) {
			response.put("code", "400");
			response.put("message", "Invalid request");
			return ResponseEntity.badRequest().body(response);
		}

		try {
			userService.signup(signUpRequestDTO);
			response.put("code", "200");
			response.put("message", "Signup successful");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("code", "500");
			response.put("message", "Signup failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
		Map<String, Object> response = new HashMap<>();

		if (loginRequest == null || loginRequest.getEmailId() == null || loginRequest.getPassword() == null) {
			response.put("code", "400");
			response.put("message", "Invalid login credentials");
			return ResponseEntity.badRequest().body(response);
		}

		Optional<LoginResponseDTO> token = userService.login(loginRequest);

		if (token.isPresent()) {
			response.put("code", "200");
			response.put("message", "Login successful");
			response.put("accesToken", token.get());
			return ResponseEntity.ok(response);
		} else {
			// If login fails
			response.put("code", "404");
			response.put("message", "Login failed: User not found or invalid credentials");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@GetMapping("/privateApi")
	public ResponseEntity<?> privateApi(@RequestHeader(value = "authorization", defaultValue = "") String auth)
			throws Exception {

		jwtUtils.verify(auth);

		Map<String, Object> response = new HashMap<>();

		response.put("data", "this is private Api for Testing");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/allUser")
	public List<UserModel> getAllUsers() {

		List<UserModel> user = userRepository.findAll();

		System.out.println(requestMeta.getName());

		return user;
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
		String refreshToken = body.get("refreshToken");
		logger.trace("Trace level logging - entering process method");
		logger.debug("Debug level logging - initializing process");
		logger.info("Info level logging - processing started");

		try {
			Claims claims = jwtUtils.verifyRefreshToken(refreshToken);

			// Re-generate a new access token using the claims from the refresh token
			UserModel user = new UserModel();
			user.setId(Long.valueOf(claims.getIssuer()));
			user.setName(claims.get("name").toString());
			user.setEmailId(claims.get("emailId").toString());
			user.setUserType(claims.get("type").toString());

			String newAccessToken = jwtUtils.generateJwt(user);

			Map<String, String> response = new HashMap<>();
			response.put("accessToken", newAccessToken);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			  logger.warn("Warning: Something unexpected happened: {}", e.getMessage());
	          logger.error("Error: Process failed", e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
		}
	}
	
		

}
