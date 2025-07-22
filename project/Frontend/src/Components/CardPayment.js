import React, { useEffect } from 'react';
const mercadoApiKey = process.env.REACT_APP_MERCADO_PAGO_API_KEY
const CardPaymentBrick = () => {
  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://sdk.mercadopago.com/js/v2';
    script.onload = () => {
      const mp = new window.MercadoPago(mercadoApiKey, {
        locale: 'en',
      });

      const bricksBuilder = mp.bricks();

      const renderCardPaymentBrick = async () => {
        const settings = {
          initialization: {
            amount: 100,
            payer: {
              email: '',
            },
          },
          customization: {
            visual: {
              style: {
                theme: 'default',
                customVariables: {},
              },
            },
            paymentMethods: {
              maxInstallments: 1,
            },
          },
          callbacks: {
            onReady: () => {
              // Brick listo
            },
            onSubmit: (cardFormData) => {
              return new Promise((resolve, reject) => {
                fetch('/process_payment', {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/json',
                  },
                  body: JSON.stringify(cardFormData),
                })
                  .then((response) => {
                    resolve();
                  })
                  .catch((error) => {
                    reject();
                  });
              });
            },
            onError: (error) => {
              console.error('MercadoPago Brick Error:', error);
            },
          },
        };

        window.cardPaymentBrickController = await bricksBuilder.create(
          'cardPayment',
          'cardPaymentBrick_container',
          settings
        );
      };

      renderCardPaymentBrick();
    };

    document.body.appendChild(script);
  }, []);

  return <div id="cardPaymentBrick_container"></div>;
};

export default CardPaymentBrick;
