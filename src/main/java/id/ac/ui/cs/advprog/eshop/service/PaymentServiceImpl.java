package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        if (order == null) throw new IllegalArgumentException();

        String status = paymentData.getOrDefault("status", "REJECTED");
        Payment payment = new Payment(UUID.randomUUID().toString(), method, status, paymentData);
        this.setStatus(payment, order, status);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllPayments();
    }

    @Override
    public void setStatus(Payment payment, Order order, String status) {
        if (payment == null || order == null || status == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        payment.setStatus(status);

        if (status.equals("SUCCESS")) {
            order.setStatus("SUCCESS");
        } else if (status.equals("REJECTED")) {
            order.setStatus("FAILED");
        }
    }
}