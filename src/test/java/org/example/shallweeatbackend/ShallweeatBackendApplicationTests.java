package org.example.shallweeatbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ShallweeatBackendApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

//	@Test
//	void contextLoads() {
//	}
	@Test
	public void testDatabaseConnection() {
		Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
		assertThat(result).isEqualTo(1);
	}

}
