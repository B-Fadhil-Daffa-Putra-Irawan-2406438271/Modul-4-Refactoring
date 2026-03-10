package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;
    Map<String, String> validPaymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-001");
        product.setProductName("Bakso");
        product.setProductQuantity(10);
        products.add(product);

        this.order = new Order("id-order", products, 0L, "WAITING_PAYMENT");

        validPaymentData = new HashMap<>();
        validPaymentData.put("voucherCode", "ESHOP12345678901"); // 16 Karakter + Awalan ESHOP
    }

    @Test
    void testSetStatusPaymentAndOrderSuccess() {
        Payment payment = new Payment("pay-001", "VOUCHER", "REJECTED", validPaymentData);

        paymentService.setStatus(payment, order, "SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusPaymentRejectedUpdatesOrderToFailed() {
        Payment payment = new Payment("pay-001", "VOUCHER", "SUCCESS", validPaymentData);

        paymentService.setStatus(payment, order, "REJECTED");

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testFindPaymentByIdHappyPath() {
        Payment payment = new Payment("pay-001", "VOUCHER", "SUCCESS", validPaymentData);

        doReturn(payment).when(paymentRepository).findById("pay-001");

        Payment result = paymentService.getPayment("pay-001");

        assertNotNull(result);
        assertEquals("pay-001", result.getId());
        verify(paymentRepository, times(1)).findById("pay-001");
    }

    @Test
    void testFindPaymentByIdNotFound() {
        doReturn(null).when(paymentRepository).findById("invalid-id");

        Payment result = paymentService.getPayment("invalid-id");

        assertNull(result);
        verify(paymentRepository, times(1)).findById("invalid-id");
    }

    @Test
    void testGetAllPaymentsHappyPath() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("pay-1", "VOUCHER", "SUCCESS", validPaymentData));
        payments.add(new Payment("pay-2", "VOUCHER", "REJECTED", validPaymentData));

        doReturn(payments).when(paymentRepository).findAllPayments();

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAllPayments();
    }

    @Test
    void testSetStatusWithNullArgumentsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.setStatus(null, null, "SUCCESS");
        });
    }

    @Test
    void testAddPaymentWithNullOrderThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.addPayment(null, "VOUCHER", validPaymentData); // Biar tetep konsisten
        });
    }
}