package com.mabaya.ads;

import java.util.List;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Abstract integration test class for starting PostgreSQL.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Testcontainers
public abstract class AbstractIntegrationTest {

  @Container
  public static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("testmabaya")
          .withUsername("mabaya")
          .withPassword("mabaya")
          .withExposedPorts(5432);

  static {
    postgreSQLContainer.setPortBindings(List.of("5433:5432"));
    postgreSQLContainer.start();
  }

  @DynamicPropertySource
  static void setDataSourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
  }
}
