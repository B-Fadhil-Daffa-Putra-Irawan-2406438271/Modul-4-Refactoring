package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {

    Map<String, String> paymentData;

    @BeforeEach
    void setUp(){
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testPaymentCreateSuccess(){
        Payment payment = new Payment("pay-001", "VOUCHER", "SUCCESS", paymentData);

        assertEquals("pay-001", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentEmpty(){
        this.paymentData.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("pay-001", "VOUCHER", "SUCCESS", paymentData);
        });
    }

    @Test
    void testPaymentStatusDoesntExist(){
        assertThrows(IllegalArgumentException.class, () -> { new Payment("pay-001", "VOUCHER", "Waiting", paymentData); } );
    }

}
