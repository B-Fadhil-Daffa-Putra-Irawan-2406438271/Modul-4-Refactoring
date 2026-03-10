package id.ac.ui.cs.advprog.eshop.model;

import enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;

        if (paymentData.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.paymentData = paymentData;
        }

        this.setStatus(status);
    }

    public void setStatus(String status) throws IllegalArgumentException{
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
