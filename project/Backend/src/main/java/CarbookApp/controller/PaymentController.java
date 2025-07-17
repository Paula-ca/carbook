package CarbookApp.controller;

import com.mercadopago.*;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.HttpStatus;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import CarbookApp.entity.PaymentFormData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@CrossOrigin
@RestController
public class PaymentController {
    public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentController.class);
    @Value("${mercado.api.token}")
    private String token;

    @PostMapping("/process_payment")
    public ResponseEntity<?> processPayment(@RequestBody PaymentFormData request) {

        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("x-idempotency-key", UUID.randomUUID().toString());

        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .customHeaders(customHeaders)
                .build();

        try {
            logger.info("Payment Form Data: " + request);
            MercadoPagoConfig.setAccessToken(token);

            PaymentClient client = new PaymentClient();

            PaymentCreateRequest paymentCreateRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(request.getTransaction_amount())
                            .token(request.getToken())
                            .description("Pago con Payment Brick")
                            .installments(request.getInstallments())
                            .paymentMethodId(request.getPayment_method_id())
                            .payer(
                                    PaymentPayerRequest.builder()
                                            .email(request.getPayer().getEmail())
                                            .firstName(request.getPayer().getFirstName())
                                            .identification(
                                                    IdentificationRequest.builder()
                                                            .type(request.getPayer().getIdentification().getType())
                                                            .number(request.getPayer().getIdentification().getNumber())
                                                            .build())
                                            .build())
                            .build();

           Payment payment =  client.create(paymentCreateRequest, requestOptions);

            return ResponseEntity.ok(payment);

        } catch (MPException | MPApiException e) {

            if (e instanceof MPApiException) {
                MPApiException apiEx = (MPApiException) e;
                MPResponse apiResponse = apiEx.getApiResponse();

                logger.error("HTTP Status: " + apiResponse.getStatusCode());
                logger.error("Response body: " + apiResponse.getContent());  // <-- Aquí está el detalle del error real
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

