package InventoryManagement;

import InventoryManagement.auth.AuthenticationResponse;
import InventoryManagement.auth.AuthenticationService;
import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.CategoryDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static InventoryManagement.model.Role.*;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService authenticationService) {
		return args -> {
			// Create Admin user
			AdminSignupRequestDto admin = AdminSignupRequestDto.builder()
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

			// Create Manager user
			AdminSignupRequestDto manager = AdminSignupRequestDto.builder()
					.firstName("Manager")
					.lastName("Manager")
					.email("manager88@mail.com")
					.password("password")
					.contact("09087654321")
					.address("Manager Address")
					.role(MANAGER)
					.build();
			AuthenticationResponse managerResponse = authenticationService.registerAdmin(manager);
			System.out.println("Manager token: " + managerResponse.getAccessToken());

			// Create Supplier user
			SupplierSignupRequestDto supplier = SupplierSignupRequestDto.builder()
					.firstName("kay-Key")
					.lastName("Enterprise")
					.email("kaykey@mail.com")
					.password("password")
					.contact("09065971889") // Corrected numeric contact
					.address("No 40 Wale Street")
					.categoryDto(CategoryDto.builder()
							.id(1L)
							.name("Electronics")
							.build()) // Removed description here
					.build();
			AuthenticationResponse supplierResponse = authenticationService.registerSupplier(supplier);
			System.out.println("Supplier token: " + supplierResponse.getAccessToken());
		};
	}
}
