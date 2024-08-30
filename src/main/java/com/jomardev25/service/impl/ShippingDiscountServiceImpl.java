package com.jomardev25.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.jomardev25.api.ShippingDiscountApi;
import com.jomardev25.exception.ShippingDiscountException;
import com.jomardev25.service.ShippingDiscountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShippingDiscountServiceImpl implements ShippingDiscountService {

    private final ShippingDiscountApi shippingDiscountApi;

    @Override
    public double getShippingDiscount(String voucherCode) {

        var shippingDiscountDTO = shippingDiscountApi.get(voucherCode).getBody();

        assert shippingDiscountDTO != null;
        if (shippingDiscountDTO.getDiscount() == 0 && shippingDiscountDTO.getCode() == null && shippingDiscountDTO.getExpiry() == null)
            throw new ShippingDiscountException("Voucher service is not available at this time. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        var sdf = new SimpleDateFormat("yyyy-MM-dd");
        var currentDate = new Date();
        Date expiryDate;

        try {
            currentDate = sdf.parse(sdf.format(new Date()));
            expiryDate = sdf.parse(shippingDiscountDTO.getExpiry());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new ShippingDiscountException("Invalid expiry date.", HttpStatus.BAD_REQUEST);
        }

        if (shippingDiscountDTO.getDiscount() < 0)
            throw new ShippingDiscountException("Invalid shipping discount percentage.", HttpStatus.BAD_REQUEST);

        if (expiryDate.before(currentDate) && !expiryDate.equals(currentDate))
            throw new ShippingDiscountException("Shipping discount voucher already expired.", HttpStatus.BAD_REQUEST);

        return shippingDiscountDTO.getDiscount();
    }

}
