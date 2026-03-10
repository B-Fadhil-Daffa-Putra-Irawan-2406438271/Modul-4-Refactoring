package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    Payment addPayment(Order order, String method, Map<String, String> paymentData);

    Payment getPayment(String id);

    List<Payment> getAllPayments();

    void setStatus(Payment payment, Order order, String status);
}
