import React,{useState} from "react";
import axios from "axios";
import UserContext from "../Context/UserContext";
import { Link, useNavigate,useParams} from "react-router-dom";
import { faCircleXmark , faMapMarkerAlt,faCalendarDays,faPerson,faBookmark,faCheckCircle ,faTimesCircle ,faClock,faExclamationTriangle, faAutomobile,faCreditCard,faStar} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const capitalizeFirstLetter = (word) => word.charAt(0).toUpperCase() + word.slice(1)


const ReservationDetail = (props) =>{
  const {reservation} = props
  const { logout } = React.useContext(UserContext)
  const navigate = useNavigate()


   const handleDelete = (reservaId) => {
     const confirm = window.confirm("¿Estás seguro de que deseas eliminar esta reserva?");
      if (confirm) {
    cancelReservation(reservaId)
      }
  };
         
  const cancelReservation = (reservationId) => { 
              if (reservationId) {
                const confirmation = true
                const token = localStorage.getItem('token')
                if (confirmation && token) {
                  const cancelReservation = async _ => {
                    const cancelBookingBaseUrl = 'http://localhost:8080/bookings/cancel'
                    try {
                      const cancelApiResponse = await axios.delete(`${cancelBookingBaseUrl}/${reservationId}`, { headers: { 'Authorization': `Bearer ${token}` } })
                      if (cancelApiResponse && cancelApiResponse.data) {
                        const url = 'http://localhost:8080/bookings/update'
                                const reservationPayload = {
                                  id: reservation.id,
                                  estado: 'Cancelada',
                                
                                }
                                
                                const reservationHeaders = {
                                  headers: {
                                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                                  }
                                }
                          
                                try {
                                  const reservationResponse = await axios.put(url, reservationPayload, reservationHeaders)
                                  
                                  if (reservationResponse && reservationResponse.data) {
                                    console.log("el estado de la reserva fue seteado exitosamente.")
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
                    } catch (error) {
                      if (error.response && error.response.status && error.response.status === 401) {
                      
                        alert("Tu sesión caducó, por favor iniciá sesión nuevamente para poder realizar una cancelación")
                        navigate('/login')
                      } else {
                        alert("Ocurrió un error al cancelar su reserva")
                        console.log(error);
                      }
                    } finally {
                      window.location.reload()
                    }
                  }
                  cancelReservation()
                }
              } 
            }
            const RatingToStars = props => {
                const toReturn = []
                const { rating } = props
            
                for (let index = 0; index < rating; index++) {
                    toReturn.push(<FontAwesomeIcon key={index} className="fa-solid recomendation-star" icon={faStar} />)    
                }
                
                return (
                    <>
                        {toReturn.map(component => component)}
                    </>
                )
            }
const getEstadoIcon = (reservationStatus) => {
  switch (reservationStatus.toLowerCase()) {
    case "confirmada":
      return { icon: faCheckCircle, color: "green" };
    case "cancelada":
      return { icon: faTimesCircle, color: "red" };
    case "pendiente":
      return { icon: faClock, color: "orange" };
    default:
      return { icon: faExclamationTriangle, color: "gray" };
  }
};

const estadoInfo = getEstadoIcon(reservation.estado);
        
  return (
  <div  className="reservation-detail-container" key={reservation.id}> 

    {/* Encabezado */}
    <div>
      <h2   className="reservation-title" style={{ margin: 0, color: "#383B58" }}>Detalle de la reserva</h2>
      <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
      <div style={{ color: "#383B58" }}>
      <h4><FontAwesomeIcon icon={faBookmark} />{" "}Código de reserva : {reservation.id}</h4>
    </div>
    
     {/* Titulo y Categoria del producto*/}
    <div>
      <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
        <h2>{capitalizeFirstLetter(reservation.producto.titulo)} {<RatingToStars rating={reservation.producto.rating}/>}</h2>
      <FontAwesomeIcon icon={faAutomobile} />{"  "}<Link
                  style={{
                  cursor: "pointer",
                  margin: 0,                 
                  color: "#383B58",
                  width: "50%",
                  textDecoration: 'underline',                 
                  alignItems: 'left',
                  justifyContent: 'center',
                  }}
                    to={`/product/${reservation.producto.id}`}
                >
                  Ver detalle del producto
                </Link>
                <h4 style={{width: "50%", color: "#545776", margin: 0 , alignItems: 'right'}}>
          </h4>
   </div>
    {/* Fechas*/}
    <div>
      <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
         <h4>Fecha de entrega:</h4>
         <FontAwesomeIcon icon={faCalendarDays} />{" "}{reservation.fecha_ingreso.split("-").reverse().join("/")} - Desde: {reservation.hora_comienzo} hs
      <h4>Fecha de devolución:</h4>
          <FontAwesomeIcon icon={faCalendarDays} />{" "}{reservation.fecha_final.split("-").reverse().join("/")} - Hasta: 18:00:00 hs
          <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
    </div>

   {/* Ubicacion*/}
      
    <div>
      <h4>¿Dónde?</h4>
              <FontAwesomeIcon icon={faMapMarkerAlt} />{" "}
              {`${reservation.producto.ubicacion}, ${reservation.producto.ciudad.titulo}`}
            </div>
                  
    </div>
    {/* Usuario*/}
    <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
    <div>
        <p>Reserva a nombre de: </p>
        <h4><FontAwesomeIcon icon={faPerson} />{" "} {reservation.usuario.nombre} {reservation.usuario.apellido}</h4>
            <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
    </div>
    {/* Estado */}
    <div>
        <h3>Estado de la reserva </h3>
        <h4>
        <FontAwesomeIcon icon={estadoInfo.icon} color={estadoInfo.color} />{" "}
        {reservation.estado}
        </h4>
          <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
    </div>
    {/* Pago */}
    <div>
      <div style={{display: "column"}}>
        <h2>Total: </h2>
        <h2 style={{width: "50%", color: "#545776", margin: 0 , alignItems: 'right'}}>ARS${reservation.precio}</h2>
        </div>
        <div style={{display: "column"}}>
            <Link
                  style={{
                  cursor: "pointer",
                  borderRadius: "5px",
                  color: "white",
                  backgroundColor: "#1DBEB4",
                  display: "flex",
                  width: "25%",
                
                  textDecoration: 'none',
                  marginTop:"10px",
                  alignItems: 'center',
                  justifyContent: 'center',
                  boxShadow: "0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19)",
                  }}
                    to={`/reservation/${reservation.id}/statusPayment/${reservation.estado_pago}/${reservation.pago_id}/${reservation.precio}`}
                >
                  <p><FontAwesomeIcon icon={faCreditCard} />{" "}Ver detalle del pago</p>
                </Link>
        </div>
          
       {/* Botón de cancelación */}
      <div>
      {reservation.estado !== 'Cancelada'&& (
        <button className="reservation-cancel-button"
          onClick={() => handleDelete(reservation.id)}
        >
          Cancelar reserva
        </button>
      )}
      </div>
    </div>
    
 </div> 
          
    );

}
  const ReservationDetailError = () => {
    return (
      <div
        style={{
          height: "calc(100vh - 140px)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
        className="bookingCard-container"
      >
        <div style={{ padding: "24px 0 36px 0" }} className="booking-card">
          <FontAwesomeIcon
            icon={faCircleXmark}
            style={{ fontSize: "64px", color: "#b81111" }}
          />
          <h2 style={{ marginTop: 0, color: "#383B58" }}>
            Ocurrió un error al obtener el detalle de tu reserva.
          </h2>
          <Link to={"/"}>
            <button
              style={{ maxHeight: "54px" }}
              className="successfulBooking-button"
            >
              Volver al inicio
            </button>
          </Link>
        </div>
      </div>
    );
  };

const UserReservationDetail = () => {
    const {bookingId} = useParams()
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
              const getReservationDetailResponse = await axios.get(
                `${url}/${bookingId}`,
                { headers: { Authorization: `Bearer ${token}` } }
              );
              if (getReservationDetailResponse?.data) {
                setReservation(getReservationDetailResponse.data);
          
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
       
      }, [user, bookingId, token, navigate]);

      
      return  !reservation ? (
        <div id="contenedor">
  <div class="contenedor-loader">
    <div class="loader"></div>
  </div>
  <div class="cargando">Cargando reserva...</div>
</div>
      ) : reservation === "Error" ? <ReservationDetailError/> : (
        <ReservationDetail reservation={reservation} />
      );
    
}  

export default UserReservationDetail;