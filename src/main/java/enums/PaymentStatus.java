package enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    REJECTED("REJECTED");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getValue().equals(value)) {
                return true;
            }
        }

        return false;
    }
}