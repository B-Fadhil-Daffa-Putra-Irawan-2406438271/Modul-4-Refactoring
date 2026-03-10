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
public class VoucherPaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        if (order == null || paymentData == null) {
            throw new IllegalArgumentException("Order and Payment Data cannot be null");
        }

        String status = "REJECTED";
        String voucherCode = paymentData.get("voucherCode");

        if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
            status = "SUCCESS";
        }

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

        if ("SUCCESS".equals(status)) {
            order.setStatus("SUCCESS");
        } else if ("REJECTED".equals(status)) {
            order.setStatus("FAILED");
        }
    }
}