package InventoryManagement;

import InventoryManagement.auth.AuthenticationResponse;
import InventoryManagement.auth.AuthenticationService;
import InventoryManagement.dto.UserSignupRequestDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.model.Category;
import InventoryManagement.model.Status;
import InventoryManagement.repository.category.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static InventoryManagement.model.Role.*;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService authenticationService, CategoryRepository categoryRepository) {
		return args -> {
			// Create and save default category (if not exists)
			Category electronicsCategory = categoryRepository.findByName("Electronics")
					.orElseGet(() -> categoryRepository.save(
							Category.builder()
									.description("Home items for electronics")
									.name("Electronics")
									.build()
					));

			// Admin
			if (!authenticationService.emailExists("admin@mail.com")) {
				UserSignupRequestDto admin = UserSignupRequestDto.builder()
						.firstName("Admin")
						.lastName("Admin")
						.email("admin@mail.com")
						.password("password")
						.contact("09012345678")
						.address("no 244")
						.role(ADMIN)
						.build();
				AuthenticationResponse adminResponse = authenticationService.registerAdmin(admin);
				System.out.println("Admin token: " + adminResponse.getAccessToken());
			}

			// Manager
			if (!authenticationService.emailExists("manager@mail.com")) {
				UserSignupRequestDto manager = UserSignupRequestDto.builder()
						.firstName("Manager")
						.lastName("Manager")
						.email("manager@mail.com")
						.password("password")
						.contact("09087654321")
						.address("Manager Address")
						.role(MANAGER)
						.build();
				// Assuming registerManager exists, otherwise replace with correct method
				AuthenticationResponse managerResponse = authenticationService.registerAdmin(manager);
				System.out.println("Manager token: " + managerResponse.getAccessToken());
			}

			// Supplier
			if (!authenticationService.emailExists("kaykey@mail.com")) {
				SupplierSignupRequestDto supplier = SupplierSignupRequestDto.builder()
						.firstName("KayKey")
						.lastName("Enterprise")
						.email("kaykey@mail.com")
						.password("password")
						.contact("09065971889")
						.address("No 40 Wale Street")
						.categoryId(electronicsCategory.getId())  // use categoryId only
						.build();
				AuthenticationResponse supplierResponse = authenticationService.registerSupplier(supplier);
				System.out.println("Supplier token: " + supplierResponse.getAccessToken());
			}
		};
	}

}
