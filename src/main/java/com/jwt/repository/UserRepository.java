package com.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

	Optional<UserModel> findByEmailId(String emailId);

}
