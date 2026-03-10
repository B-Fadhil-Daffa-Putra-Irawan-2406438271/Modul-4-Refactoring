package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherPaymentServiceTest {

    @InjectMocks
    VoucherPaymentServiceImpl voucherPaymentService; // Kita mock class khusus voucher

    @Mock
    PaymentRepository paymentRepository;

    Order order;

    @BeforeEach
    void setUp() {
        this.order = new Order("id-order", new ArrayList<>(), 0L, "WAITING_PAYMENT");
    }

    @Test
    void testAddPaymentVoucherValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678901"); // 16 char, starts with ESHOP

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = voucherPaymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testAddPaymentVoucherInvalidLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123"); // Cuma 8 char

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = voucherPaymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testAddPaymentVoucherInvalidPrefix() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "DISKO12345678901"); // 16 char, tapi depannya DISKO

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = voucherPaymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testAddPaymentVoucherMissingCode() {
        Map<String, String> paymentData = new HashMap<>();
        // Gak ada key "voucherCode"

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = voucherPaymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", order.getStatus());
    }
}