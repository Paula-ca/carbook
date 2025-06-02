import React, { useEffect } from 'react';
import { useParams } from "react-router-dom";

const mercadoApiKey = process.env.REACT_APP_MERCADO_PAGO_API_KEY

const StatusScreenBrick = (_) => {
  const { reservaId, paymentId , amount} = useParams();

  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://sdk.mercadopago.com/js/v2';
    script.onload = () => {
      const mp = new window.MercadoPago(mercadoApiKey, {
        locale: 'es',
      });
      const bricksBuilder = mp.bricks();

      const renderStatusScreenBrick = async () => {
        const settings = {
          initialization: {
            paymentId: paymentId, 
          },
          customization: {
            visual: {
              hideStatusDetails: true,
              hideTransactionDate: true,
              style: {
                theme: 'default',
              },
            },
            backUrls: {
              error: `https://localhost:3000/reservation/${reservaId}/payment/${amount}`,
              return: `https://localhost:3000/statusBooking/${reservaId}`
             
            }
          },
          callbacks: {
            onReady: () => {
            
            },
            onError: (error) => {
              console.error('Error en StatusScreenBrick:', error);
            },
          },
        };

        window.statusScreenBrickController = await bricksBuilder.create(
          'statusScreen',
          'statusScreenBrick_container',
          settings
        );
      };

      renderStatusScreenBrick();
    };

    document.body.appendChild(script);
  },[amount,paymentId,reservaId]);

  return ( 
  <div>
  <div id="statusScreenBrick_container" style={{ marginTop: '20px', marginBottom:'20px',marginLeft:'100px', marginRight:'100px'}}></div>
</div>
  );
};

export default StatusScreenBrick;
