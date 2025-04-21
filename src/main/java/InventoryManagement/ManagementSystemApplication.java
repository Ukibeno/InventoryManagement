package InventoryManagement;

import InventoryManagement.auth.AuthenticationResponse;
import InventoryManagement.auth.AuthenticationService;
import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.CategoryCreationRequestDto;
import InventoryManagement.dto.CategoryDto;
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
	@Profile("!test")
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
				AdminSignupRequestDto admin = AdminSignupRequestDto.builder()
						.firstName("Admin")
						.lastName("Admin")
						.email("admin@mail.com")
						.password("password")
						.contact("09012345678")
						.address("no 244")
						.status(Status.ACTIVE)
						.role(ADMIN)
						.build();
				AuthenticationResponse adminResponse = authenticationService.registerAdmin(admin);
				System.out.println("Admin token: " + adminResponse.getAccessToken());
			}

			// Manager
			if (!authenticationService.emailExists("manager@mail.com")) {
				AdminSignupRequestDto manager = AdminSignupRequestDto.builder()
						.firstName("Manager")
						.lastName("Manager")
						.email("manager@mail.com")
						.password("password")
						.contact("09087654321")
						.address("Manager Address")
						.status(Status.ACTIVE)
						.role(MANAGER)
						.build();
				AuthenticationResponse managerResponse = authenticationService.registerAdmin(manager);
				System.out.println("Manager token: " + managerResponse.getAccessToken());
			}

			// Supplier
			if (!authenticationService.emailExists("kaykey@mail.com")) {
				SupplierSignupRequestDto supplier = SupplierSignupRequestDto.builder()
						.firstName("kayKey")
						.lastName("Enterprise")
						.email("kaykey@mail.com")
						.password("password")
						.contact("09065971889")
						.address("No 40 Wale Street")
						.status(Status.ACTIVE)
						.categoryCreationRequestDto(CategoryCreationRequestDto.builder()
								.id(electronicsCategory.getId())
								.name(electronicsCategory.getName())
								.description(electronicsCategory.getDescription())
								.build())
						.build();
				AuthenticationResponse supplierResponse = authenticationService.registerSupplier(supplier);
				System.out.println("Supplier token: " + supplierResponse.getAccessToken());
			}
		};
	}

}
