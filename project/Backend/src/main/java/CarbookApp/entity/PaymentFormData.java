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


         public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public Identification getIdentification(){
        return identification;
    }
    public void setIdentification(Identification identification){
        this.identification = identification;
    }

    }

    @Data
    public static class Identification {
        private String type;
        private String number;


        public String getType(){
            return type;
        }
        public String getNumber(){
            return number;
        }
        public void setType(String type){
            this.type = type;
        }
        public void setNumber(String number){
            this.number = number;
        }
    }

    public BigDecimal getTransaction_amount(){
        return transaction_amount;
    }
    public void setTransaction_amount(BigDecimal transaction_amount){
        this.transaction_amount = transaction_amount;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }
    
    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }
   
    
}


