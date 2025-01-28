package com.jwt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMeta {
	
	private String name;
	private String emailId;
	private String password;
	private String phoneNumber;

}
