package com.jomardev25.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jomardev25.api.ShippingDiscountApi;
import com.jomardev25.dto.ShippingDiscountDTO;
import com.jomardev25.dto.ShippingRequestDTO;
import com.jomardev25.dto.ShippingResponseDTO;
import com.jomardev25.exception.ShippingRuleException;
import com.jomardev25.service.impl.ShippingCalculatorServiceImpl;
import com.jomardev25.service.impl.ShippingDiscountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

@WebMvcTest(ShippingCostCalculatorController.class)
public class ShippingCostCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShippingCalculatorServiceImpl shippingService;

    @Mock
    private ShippingDiscountServiceImpl shippingDiscountService;

    @Mock
    private ShippingDiscountApi shippingDiscountApi;

    @Mock
    private RestTemplate restTemplate;

    private final String API_URL = "/api/v1/shipping";

    @Test
    @DisplayName("Heavy Parcel: Weight exceeds 10kg, Rate: PHP 20")
    public void testHeavyParcelShippingCost() throws Exception {
        var request = ShippingRequestDTO.builder()
                .weight(50)
                .height(50)
                .width(5)
                .length(4)
                .build();

        var expectedCost = new BigDecimal("1000");
        when(shippingService.calculateShippingCost(request.getWeight(), request.getHeight(), request.getWidth(),
                request.getLength(), request.getVoucher())).thenReturn(expectedCost);

        var response = """
                {
                    "cost":1000
                }
                """;

        var results = mockMvc.perform(
                        post(API_URL)
                                .content(asJsonString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cost", is(1000)));

        JSONAssert.assertEquals(response, results.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Small Parcel: Volume is less than 1500 cm3, Rate: PHP 0.03")
    public void testSmallParcelShippingCost() throws Exception {
        var weight = 5d;
        var height = 50d;
        var width = 5.996;
        var length = 5d;
        var shippingCost = new BigDecimal("44.97");

        when(shippingService.calculateShippingCost(weight, height, width, length, null)).thenReturn(shippingCost);

        var requestParams = """
                    {
                         "weight": 5,
                         "height": 50,
                         "width": 5.996,
                         "length": 5
                    }
                """;

        var response = """
                {
                    "cost": 44.97
                }
                """;

        var results = mockMvc.perform(
                        post(API_URL)
                                .content(requestParams)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        JSONAssert.assertEquals(response, results.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Medium Parcel: Volume is less than 2500 cm3, Rate: PHP 0.04")
    public void testMediumParcelShippingCost() throws Exception {
        var weight = 5d;
        var height = 50d;
        var width = 5d;
        var length = 9.9d;
        var shippingCost = new BigDecimal("99");

        when(shippingService.calculateShippingCost(weight, height, width, length, null)).thenReturn(shippingCost);

        var requestParams = """
                    {
                         "weight": 5,
                         "height": 50,
                         "width": 5,
                         "length": 9.9
                    }
                """;

        var response = """
                {
                    "cost": 99
                }
                """;

        var results = mockMvc.perform(
                        post(API_URL)
                                .content(requestParams)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        JSONAssert.assertEquals(response, results.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Large Parcel: Volume is greater than 2500 cm3, Rate: PHP 0.05")
    public void testLargeParcelShippingCost() throws Exception {
        var weight = 5d;
        var height = 50d;
        var width = 5d;
        var length = 10d;
        var shippingCost = new BigDecimal("125");

        when(shippingService.calculateShippingCost(weight, height, width, length, null)).thenReturn(shippingCost);

        var requestParams = """
                    {
                         "weight": 5,
                         "height": 50,
                         "width": 5,
                         "length": 10
                     }
                """;

        var response = """
                {
                    "cost": 125
                }
                """;

        mockMvc.perform(
                        post(API_URL)
                                .content(requestParams)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test // Reject: Weight exceeds 50kg
    public void testRejectParcel() throws Exception {
        var weight = 51d;
        var height = 50d;
        var width = 5d;
        var length = 4d;
        var shippingCost = new BigDecimal("1000");

        when(shippingService.calculateShippingCost(weight, height, width, length, null)).thenThrow(ShippingRuleException.class);

        var requestParams = """
                    {
                         "weight": 51,
                         "height": 50,
                         "width": 5,
                         "length": 4
                    }
                """;
        var response = """
                {
                   "weight": "Weight must be less than or equal to 50kg"
                }
                """;

        var results = mockMvc.perform(
                        post(API_URL)
                                .content(requestParams)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));

        JSONAssert.assertEquals(response, results.andReturn().getResponse().getContentAsString(), false);
    }

    @Test // Validation request
    public void testShippingCalculationFailWhenWeightNotGiven() throws Exception {
        var request = ShippingRequestDTO.builder()
                .height(50)
                .width(5)
                .length(4)
                .build();

        var expectedCost = new BigDecimal("1000");

        when(shippingService.calculateShippingCost(request.getWeight(), request.getHeight(), request.getWidth(), request.getLength(), null)).thenReturn(expectedCost);

        var response = """
                {
                   "weight": "Weight must be greater than or equal to 1kg"
                }
                """;

        var results = mockMvc.perform(
                        post(API_URL)
                                .content(asJsonString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("Parcel with voucher or discount")
    public void testParcelWithVoucherDiscountShippingCost() throws Exception {
        var weight = 50d;
        var height = 50d;
        var width = 5d;
        var length = 4d;
        var voucherCode = "AAA";
        var discountPercentage = 0.25d;
        var expiryDate = "2025-07-01";
        var shippingCost = new BigDecimal(750);
        var shippingDiscountDto = ShippingDiscountDTO.builder()
                .code(voucherCode)
                .discount(discountPercentage)
                .expiry(expiryDate)
                .build();

        ResponseEntity<ShippingDiscountDTO> discount = ResponseEntity.ok().body(shippingDiscountDto);

        when(shippingDiscountApi.get(voucherCode)).thenReturn(discount);

        when(shippingDiscountService.getShippingDiscount(voucherCode)).thenReturn(discountPercentage);

        when(shippingService.calculateShippingCost(weight, height, width, length, voucherCode)).thenReturn(shippingCost);

        var requestParams = """
                    {
                       "weight": 50,
                       "height": 1,
                       "width": 1,
                       "length": 1,
                       "voucher": "AAA"
                    }
                """;
        var response = """
                {
                    "cost": 750
                }
                """;

        var results = mockMvc.perform(
                        post(API_URL)
                                .content(requestParams)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));
        
        JSONAssert.assertEquals(response, results.andReturn().getResponse().getContentAsString(), false);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}