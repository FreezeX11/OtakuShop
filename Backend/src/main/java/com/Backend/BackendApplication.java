package com.Backend;

import com.Backend.Entity.Profile;
import com.Backend.Enumeration.UserProfile;
import com.Backend.Payload.Request.SignupRequest;
import com.Backend.Repository.ProfileRepository;
import com.Backend.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class BackendApplication implements CommandLineRunner {
	private ProfileRepository profileRepository;
	private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (UserProfile userProfileEnum : UserProfile.values()) {
			profileRepository.save(new Profile(userProfileEnum));
		}

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("diam@gmail.com");
		signupRequest.setProfile("ADMIN");
		signupRequest.setPassword("strongPassword123");

		userService.registerUser(signupRequest);
	}
}
