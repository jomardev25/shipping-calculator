package com.jomardev25.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jomardev25.entity.ShippingRate;
import com.jomardev25.entity.ShippingRule;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {

    Optional<ShippingRate> findByRuleName(ShippingRule shippingRule);



}
