import React,{useState} from "react";
import axios from "axios";
import UserContext from "../Context/UserContext";
import { Link, useNavigate,useParams} from "react-router-dom";
import {faCheckCircle ,faClock ,faExclamationTriangle} from "@fortawesome/free-solid-svg-icons";
import "react-multi-date-picker/styles/colors/teal.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const StatusBooking = () => {
      const {reservaId} = useParams()
      const navigate = useNavigate()
      const { user } = React.useContext(UserContext);
      const [reservation,setReservation] = useState(null)
      const token = localStorage.getItem("token");
  
      React.useEffect(() => {
          if (user && user.id && token) {
            const getReservationById = async (_) => {
              const url =
                "http://localhost:8080/bookings";
      
              try {
                // First getting active reservations
                const getReservation = await axios.get(
                  `${url}/${reservaId}`,
                  { headers: { Authorization: `Bearer ${token}` } }
                );
                if (getReservation?.data) {
                  setReservation(getReservation.data);
            
                }
    
              } catch (error) {
                if (error.response && error.response.status && error.response.status === 401) {
                  alert("Hubo un error, porfavor intenta nuevamente")
                  navigate('/login')
                } else {
                  console.log("Error", error);
                  setReservation("Error")
                }
              }
            };
        
            getReservationById();
          }
         
        }, [user, reservaId, token, navigate]);
      
  
  return !reservation ? (
  <div id="contenedor">
  <div class="contenedor-loader">
    <div class="loader"></div>
  </div>
  <div class="cargando">Procesando reserva...</div>
</div>

  ) : reservaId && reservation.estado === "Confirmada" ? (
    <div style={{height: 'calc(100vh - 140px)', display: 'flex', alignItems: 'center', justifyContent: 'center'}} className="bookingCard-container">
      <div className="booking-card">
        <FontAwesomeIcon icon={faCheckCircle} color={'green'} style={{ height: '74px', width: 'auto', objectFit: 'contain'}}/>
        <h1 style={{marginBottom: 0, color: "#1DBEB4"}}>¡Muchas Gracias!</h1>
        <h2 style={{marginTop: 0, color: "#383B58"}}>Su reserva se ha realizado con éxito</h2>
        <Link to={"/"}>
          <button style={{maxHeight: '54px'}} className="successfulBooking-button">OK</button>
        </Link>
      </div>
    </div>
  ): reservaId && reservation.estado === "Pendiente" ? (
      <div style={{height: 'calc(100vh - 140px)', display: 'flex', alignItems: 'center', justifyContent: 'center'}} className="bookingCard-container">
      <div className="booking-card">
        <FontAwesomeIcon icon={faClock} color={'orange'} style={{ height: '74px', width: 'auto', objectFit: 'contain'}}/>
        <h1 style={{marginBottom: 0, color: "#1DBEB4"}}>¡Muchas Gracias!</h1>
        <h2 style={{marginTop: 0, color: "#383B58"}}>Su reserva se encuentra pendiente, por favor verifique los detalles en 'Mis reservas'</h2>
        <Link to={"/"}>
          <button style={{maxHeight: '54px'}} className="successfulBooking-button">OK</button>
        </Link>
      </div>
    </div>
    ): (
      <div style={{height: 'calc(100vh - 140px)', display: 'flex', alignItems: 'center', justifyContent: 'center'}} className="bookingCard-container">
      <div className="booking-card">
        <FontAwesomeIcon icon={faExclamationTriangle} color={'gray'} style={{ height: '74px', width: 'auto', objectFit: 'contain'}}/>
        <h1 style={{marginBottom: 0, color: "#1DBEB4"}}>¡Lo sentimos mucho!</h1>
        <h2 style={{marginTop: 0, color: "#383B58"}}>Su reserva no se ha podido completar con éxito, por favor verifique los detalles en 'Mis reservas'</h2>
        <Link to={"/"}>
          <button style={{maxHeight: '54px'}} className="successfulBooking-button">OK</button>
        </Link>
      </div>
    </div>
    );
};

export default StatusBooking;
