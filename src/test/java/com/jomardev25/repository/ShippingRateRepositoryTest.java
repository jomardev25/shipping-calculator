package com.jomardev25.repository;

import com.jomardev25.entity.ShippingRate;
import com.jomardev25.entity.ShippingRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ShippingRateRepositoryTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.37"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired
    ShippingRateRepository shippingRateRepository;

    @Test
    void findByRuleName() {
        var shippingRateInsertData = ShippingRate.builder()
                .id(1)
                .ruleName(ShippingRule.HEAVY_PARCEL)
                .rate(20.0)
                .condition("Weight exceeds 10kg")
                .build();

        shippingRateRepository.save(shippingRateInsertData);
        var shippingRate = shippingRateRepository.findByRuleName(ShippingRule.HEAVY_PARCEL);
        assertThat(shippingRate).isPresent();
    }
}