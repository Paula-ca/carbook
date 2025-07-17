package CarbookApp.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentFormData {
    private BigDecimal transaction_amount;
    private String token;
    private int installments;
    private String payment_method_id;
    private Payer payer;

    @Data
    public static class Payer {
        private String email;
        private String firstName ;
        private Identification identification;
    }

    @Data
    public static class Identification {
        private String type;
        private String number;
    }
}

