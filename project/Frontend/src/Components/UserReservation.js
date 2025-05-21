import React  from "react";
import axios from "axios";
import moment from "moment";
import { Navigate } from "react-router-dom";
import UserContext from "../Context/UserContext";
import { Link, useNavigate } from "react-router-dom";
import Switch from "react-switch";

import { faCircleXmark ,faCheckCircle ,faTimesCircle ,faClock,faExclamationTriangle} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";


const NotLoggedIn = (_) => {
  alert("Por favor, iniciá sersión para ver tus reservas");
  return <Navigate to="/login" />;
};

const NoReservations = () => {
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
          Aún no has efectuado ninguna reserva.
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

const ReservationError = () => {
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
            Ocurrió un error al obtener tus reservas.
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

const ReservationCards = ({ reservationsData, navigate }) => {

  const hanldeBookDetail = (reservationId) =>{
     navigate(`/bookingDetail/${reservationId}`)
  }
  const getEstadoIcon = (reservationStatus) => {

    if(reservationStatus===null){
      return { icon: faExclamationTriangle, color: "gray" };
    }
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

  return (
    <div className="reservationWrapper" style={{minHeight: "calc(100vh - 250px)"}}>
      {reservationsData.map((reservation) => {
          const estadoInfo = getEstadoIcon(reservation.estado);
        return (
          <div key={reservation.id} className="reservationCard">
            <div className="reservation-info">
              <p>
                <FontAwesomeIcon icon={estadoInfo.icon} color={estadoInfo.color} />{" "}
                {reservation.estado}
              </p>     
              <p>Vehículo: {reservation.producto.titulo}</p>
              <p>Ciudad: {reservation.producto.ciudad.titulo}</p>
              <button style={{ width: '80%', height: '32px', backgroundColor: "#1DBEB4", color: "white", borderRadius: '5px', border: '0', cursor: 'pointer' }}
                onClick={_ =>hanldeBookDetail(reservation.id)}>Ver reserva</button> 
              
            </div>
            <div className="reservation-car">
              <img
                style={{ width: "100%"}}
                src={reservation.producto.imagenes[0].url}
                alt={reservation.producto.imagenes[0].titulo}
              />
            </div>
          </div>
        );
    })}
  
    </div>
  )
}
  

const Reservations = ({ userReservations, userCanceledReservations, navigate, logout }) => {
  const [showCanceledReservations, setShowCanceledReservations] = React.useState(!userReservations || !userReservations.length ? true : false)

  return (
    <div>
      {
        (
          !userReservations || !userReservations.length) && (!userCanceledReservations || !userCanceledReservations.length) ?
          <NoReservations />
          : (
              <>
                {
                  userReservations && userReservations.length && userCanceledReservations && userCanceledReservations.length ? (
                    <div style={{ padding: '20px' }}>
                      <h2 style={{ margin: '10px 0px', color: '#383B58' }}>Mostrar reservas canceladas</h2>
                      <Switch checked={showCanceledReservations} onChange={_ => setShowCanceledReservations(!showCanceledReservations)}/>
                    </div>
                  ) : <></>
                }
                  
                <ReservationCards 
                   reservationsData={(userReservations && userReservations.length && userCanceledReservations && userCanceledReservations.length) ? showCanceledReservations ? userCanceledReservations : userReservations : (userReservations && userReservations.length) ? userReservations : userCanceledReservations} reservationsAreCanceled={showCanceledReservations} navigate={navigate} logout={logout} />
                  
                
              </>
            )
      }
    </div>
  );
};


const UserReservation = (_) => {
  const navigate = useNavigate()
  const { logout } = React.useContext(UserContext)
  const [userReservations, setUserReservations] = React.useState();
  const [userCanceledReservations, setUserCanceledReservations] = React.useState();
  const { user } = React.useContext(UserContext);

  React.useEffect(() => {
    const token = localStorage.getItem("token");

    if (user && user.id && token) {
      const getReservationsFromUser = async (_) => {
        const url =
          "http://localhost:8080/bookings/user";

        try {
          // First getting active reservations
          const getUserReservationsResponse = await axios.get(
            `${url}/${user.id}/false`,
            { headers: { Authorization: `Bearer ${token}` } }
          );
          if (getUserReservationsResponse && getUserReservationsResponse.data) {
            setUserReservations(
              getUserReservationsResponse.data.sort(
                (a, b) =>
                  moment(a.fecha_ingreso).format("YYYYMMDD") -
                  moment(b.fecha_ingreso).format("YYYYMMDD")
              )
            );
          }

          // Then get the canceled reservations
          const getUserCanceledReservationsResponse = await axios.get(
            `${url}/${user.id}/true`,
            { headers: { Authorization: `Bearer ${token}` } }
          );

          if (getUserCanceledReservationsResponse && getUserCanceledReservationsResponse.data) {
            setUserCanceledReservations(
              getUserCanceledReservationsResponse.data.sort(
                (a, b) =>
                  moment(a.fecha_ingreso).format("YYYYMMDD") -
                  moment(b.fecha_ingreso).format("YYYYMMDD")
              )
            );
          }
        } catch (error) {
          if (error.response && error.response.status && error.response.status === 401) {
            logout()
            alert("Tu sesión caducó, por favor iniciá sesión nuevamente para ver tus reservas")
            navigate('/login')
          } else {
            console.log("Error", error);
            setUserReservations("Error")
          }
        }
      };
  
      getReservationsFromUser();
    }
  }, [logout,navigate,user]);

  return !user ? (
    <NotLoggedIn />
  ) : !userReservations || !userCanceledReservations ? (
<div id="contenedor">
  <div class="contenedor-loader">
    <div class="loader"></div>
  </div>
  <div class="cargando">Cargando reservas...</div>
</div>
  ) : userReservations === "Error" ? <ReservationError/> : (
    <Reservations userReservations={userReservations} userCanceledReservations={userCanceledReservations} navigate={navigate} logout={logout} />
  );
};

export default UserReservation;
