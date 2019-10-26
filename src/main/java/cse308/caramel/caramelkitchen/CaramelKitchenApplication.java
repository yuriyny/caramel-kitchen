package cse308.caramel.caramelkitchen;

import cse308.caramel.caramelkitchen.user.Role;
import cse308.caramel.caramelkitchen.user.storage.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CaramelKitchenApplication {
	public static void main(String[] args) {
		SpringApplication.run(CaramelKitchenApplication.class, args);
	}
	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {
		return args -> {
			Role adminRole = roleRepository.findByRole("ADMIN");
			if (adminRole == null) {
				Role admin = new Role();
				admin.setId((long)0);
				admin.setRole("ADMIN");
				roleRepository.save(admin);
			}
			Role userRole = roleRepository.findByRole("USER");
			if (userRole == null) {
				Role user = new Role();
				user.setId((long)1);
				user.setRole("USER");
				roleRepository.save(user);
			}
		};
	}
}
