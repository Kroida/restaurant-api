package com.luis.restaurantapi;

import com.luis.restaurantapi.config.PostgresTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresTestConfiguration.class)
class RestaurantApiApplicationTests {

	@Test
	void contextLoads() {
	}
}