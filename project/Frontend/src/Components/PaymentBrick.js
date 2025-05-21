import React,{ useEffect, useState} from 'react';
import { initMercadoPago} from '@mercadopago/sdk-react';
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import UserContext from "../Context/UserContext";
initMercadoPago(process.env.REACT_APP_MERCADO_PAGO_API_KEY);
const mercadoApiKey = process.env.REACT_APP_MERCADO_PAGO_API_KEY

const PaymentBrick = () => {
  const { logout } = React.useContext(UserContext)
  const navigate = useNavigate()
  const { reservaId, amount } = useParams();

  
  useEffect(() => {
    // Cargar el SDK de Mercado Pago si aún no está cargado
    const scriptId = 'mercado-pago-sdk';
    if (!document.getElementById(scriptId)) {
      const script = document.createElement('script');
      script.src = 'https://sdk.mercadopago.com/js/v2';
      script.id = scriptId;
      script.onload = () => initializeBrick();
      document.body.appendChild(script);
    } else {
      initializeBrick();
    }

    async function initializeBrick() {
      const mp = new window.MercadoPago(mercadoApiKey, {
        locale: 'es-AR'
      });

      const bricksBuilder = mp.bricks();

      const settings = {
        initialization: {
          amount: amount,
          payer: {
            email: '',
          },
        },
        customization: {
          visual: {
            style: {
              theme: 'default',
              customVariables: {}
            }
          },
          paymentMethods: {
            maxInstallments: 1,
          }
        },
        callbacks: {
          onReady: () => {
            console.log('Brick listo');
            //setPaymentLoading(false)
          },
          onSubmit: (cardFormData) => {
            return new Promise((resolve, reject) => {
              fetch('http://localhost:8080/process_payment', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                },
                body: JSON.stringify(cardFormData)
              })
              .then((response) => response.json())
              .then((data) => {
                console.log(data)
                const paymentId = data.id;
                const statusPayment = data.status
                  paymentStatusUpdate(statusPayment,paymentId)
                  navigate(`/reservation/${reservaId}/statusPayment/${statusPayment}/${paymentId}/${amount}`);
                  resolve()
                  
                })
                .catch(error => {
                  // manejar error del backend
                  reject();
                });
            });
          },
          onError: (error) => {
            console.error('Error en el Brick:', error);
          }
        }
      };

      await bricksBuilder.create('cardPayment', 'cardPaymentBrick_container', settings);
    }
  }, []);

    const paymentStatusUpdate = async (statusPayment,paymentId) => {
       let newBookingStatus = 'Pendiente';
        if (statusPayment === 'approved') {
           newBookingStatus = 'Confirmada';
        } else if(statusPayment==='in_process'){
         newBookingStatus = 'Pendiente';
         }else{
          newBookingStatus = 'Rechazada'
         }

      const url = 'http://localhost:8080/bookings/update'
        const reservationPayload = {
          id: reservaId,
          estado_pago: statusPayment,
          estado: newBookingStatus,
          pago_id: paymentId
        }
        
        const reservationHeaders = {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
  
        try {
          const reservationResponse = await axios.put(url, reservationPayload, reservationHeaders)
          
          if (reservationResponse && reservationResponse.data) {
            console.log("el estado del pago fue seteado exitosamente.")
          }
        } catch (error) {
          if (error.response && error.response.status && error.response.status === 401) {
            logout()
            alert("Tu sesión caducó, por favor iniciá sesión nuevamente para ver tus reservas")
            navigate('/login')
          } else {
            alert('Ocurrió un error al reservar el producto, por favor vuelva a intentarlo')
            console.log(error)
          }
        }
    }
      
  return (
    <div>
      <div id="cardPaymentBrick_container" 
    style={{ marginTop: '20px', marginBottom:'20px',marginLeft:'100px', marginRight:'100px'}} />
    </div>
  );
};

export default PaymentBrick;
  

                 
             
                    
                 