import React from "react";
import axios from "axios";
import moment from 'moment'
import { useParams, Navigate, Link, useNavigate } from "react-router-dom";

import {
  faAngleLeft,
  faStar,
  faMapMarkerAlt,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { Calendar } from "react-multi-date-picker";
import "react-multi-date-picker/styles/colors/teal.css";
import UserContext from "../Context/UserContext";


const capitalizeFirstLetter = (word) =>
  word.charAt(0).toUpperCase() + word.slice(1);

const RatingToStars = (props) => {
  const toReturn = [];
  const { rating } = props;

  for (let index = 0; index < rating; index++) {
    toReturn.push(
      <FontAwesomeIcon
        key={index}
        className="fa-solid recomendation-star"
        icon={faStar}
      />
    );
  }

  return <>{toReturn.map((component) => component)}</>;
};

const ProductDetailTop = (props) => {
  const { product } = props;

  return (
    <div
      style={{
        display: "flex",
        width: "100%",
        justifyContent: "space-between",
        marginBottom: "5px",
      }}
      className="product-detail-top"
    >
      <div>
        <h3 style={{ color: "#545776", margin: 0 }}>
          {capitalizeFirstLetter(product.categoria.titulo)}
        </h3>
        <h2 style={{ margin: 0 }}>{capitalizeFirstLetter(product.titulo)}</h2>
      </div>
      <div style={{ display: "flex", alignItems: "center" }}>
        <Link to={"/"}>
          <FontAwesomeIcon
            style={{ color: "#545776" }}
            className="fa-solid"
            icon={faAngleLeft}
            size="3x"
          />
        </Link>
      </div>
    </div>
  );
};

const gregorian_en = {
  name: "gregorian_es",
  months: [
    ["Enero", "ene"],
    ["Febrero", "feb"],
    ["Marzo", "mar"],
    ["Abril", "abr"],
    ["Mayo", "may"],
    ["Junio", "jun"],
    ["Julio", "jul"],
    ["Agosto", "ago"],
    ["Septiembre", "sep"],
    ["Octubre", "oct"],
    ["Noviembre", "nov"],
    ["Diciembre", "dic"],
  ],
  weekDays: [
    ["sábado", "s"],
    ["domingo", "d"],
    ["lunes", "l"],
    ["martes", "m"],
    ["miércoles", "x"],
    ["jueves", "j"],
    ["viernes", "v"],
  ],
  digits: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"],
  meridiems: [
    ["AM", "am"],
    ["PM", "pm"],
  ],
};

const BookingCalendar = (props) => {
  const { setFromDate, setToDate, productId } = props
  const minDate = new Date()
  const [disabledDates, setDisabledDates] = React.useState('loading')

  const handleChange = (inputDate) => {
    const [from, to] = inputDate;
    if (from && to) {
      setFromDate(`${from.day}/${from.month.number}/${from.year}`);
      setToDate(`${to.day}/${to.month.number}/${to.year}`);
    } else {
      setFromDate(null);
      setToDate(null);
    }
  };

  React.useEffect(() => {
    const getDisabledDates = async _ => {
      const getDisabledDatesUrl = `http://localhost:8080/bookings/product/${productId}/false`
      try {
        const disabledDatesResponse = await axios.get(getDisabledDatesUrl)
        if (disabledDatesResponse && disabledDatesResponse.data) {
          await setDisabledDates(disabledDatesResponse.data.map(dates => { return { from: moment(dates.fecha_ingreso).utcOffset(0).set({ hour: 0, minute: 0, second: 0}).toDate(), to: moment(dates.fecha_final).utcOffset(0).set({ hour: 23, minute: 59, second: 59}).toDate() }}))
        }
      } catch (error) {
          // Si es 404, simplemente no hay reservas → fechas 100% disponibles
        if (error.response && error.response.status === 404) {
         setDisabledDates([]); // No hay fechas deshabilitadas
        } else {
          console.error("Error al obtener fechas deshabilitadas", error);
        }
      }
    }
    getDisabledDates()
  }, [productId])
  
  return disabledDates === 'loading' ? <></> : (
    <div
      className="product-detail-calendar-reservation"
      style={{ width: "100%", display: "flex" }}
    >
      <Calendar
        minDate={minDate}
        mapDays={({date}) => {
          for (let index = 0; index < disabledDates.length; index++) {
            const { from, to } = disabledDates[index]
            if (date >= from && date <= to) {
              return {
                disabled: true,
                style: { color: "#ccc" },
                onClick: () => alert("Ya hay una reserva para este día")
              }
            }
          }
        }}
        format="DD/MM/YYYY"
        range={true}
        locale={gregorian_en}
        numberOfMonths={2}
        onChange={handleChange}
        selectsRange={true}
        disableYearPicker
        className="teal"
        containerStyle={{ width: "100% " }}
        style={{ width: "100% " }}
      />
    </div>
  );
};

const UserData = (props) => {
  const { user , city, setCity} = props;

  return (
    <div>
      <h2>Tus datos</h2>

      <form className="userData-card">
        <div className="user-input">
          <label className="userData-label">Nombre</label>
          <input
            value={user && user.name}
            type={"text"}
            disabled
            className="userData-input"
          ></input>
          <label className="userData-label">Correo electrónico</label>
          <input
            value={user && user.email}
            type={"email"}
            disabled
            className="userData-input"
          ></input>
        </div>
        <div className="user-input">
          <label className="userData-label">Apellido</label>
          <input
            value={user && user.surname}
            type={"text"}
            disabled
            className="userData-input"
          ></input>
          <label className="userData-label">Ciudad</label>
          <input
            value={city}
            type="text"
            onChange={(e) => setCity(e.target.value)}
            className="userData-input"
          ></input>
        </div>
      </form>
    </div>
  );
};

const ReservationCalendar = (props) => {
  const { setFromDate, setToDate, productId } = props;
  return (
    <div className="reservation-calendar">
      <h2>Seleccioná tu fecha de reserva</h2>
      <BookingCalendar productId={productId} setFromDate={setFromDate} setToDate={setToDate} />
    </div>
  );
};

const Arrival = (props) => {
  const { setReservationTime } = props

  const changedReservationTime = async event => {
    const { value } = event.target
    await setReservationTime(value)
  } 

  return (
    <div>
      <h2>Tu horario de llegada</h2>
      <div className="arrival-card">
        <h3>
          Tu auto va a estar listo para retirar entre las 08:00 y las 18:00 horas
        </h3>
        <label style={{ marginBottom: "1rem" }}>
          Indicá tu horario estimado de llegada
        </label>
        <select onChange={changedReservationTime} className="arrival-select">
          <option value="" selected hidden>
            Seleccionar hora
          </option>
          <option>08:00</option>
          <option>09:00</option>
          <option>10:00</option>
          <option>11:00</option>
          <option>12:00</option>
          <option>13:00</option>
          <option>14:00</option>
          <option>15:00</option>
          <option>16:00</option>
          <option>17:00</option>
          <option>18:00</option>
        </select>
      </div>
    </div>
  );
};

const ReservationDetail = (props) => {
  const { product, fromDate, toDate, reservationTime } = props;
  const { logout } = React.useContext(UserContext)
  const navigate = useNavigate()
  var count_days = 0
  var final_price = 0 
  if (fromDate && toDate && product) {

    const formatToISO = (dateStr) => {
      const [day, month, year] = dateStr.split('/');
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    };
    const start = new Date(formatToISO(fromDate));
    const end = new Date(formatToISO(toDate));

    count_days = (end - start) / (1000 * 60 * 60 * 24)+1;
    const price = Number(product.precio) || 0;
    final_price = count_days * price;
  }

 
  let daysText = '';
  if (count_days === 1) {
    daysText = `Reserva de ${count_days} día`;
  } else if (count_days > 1) {
   daysText = `Reserva de ${count_days} días`;
  }

  const makeReservation = async _ => {
    if (!fromDate || !toDate || !reservationTime) {
      alert('Por favor complete todos los datos')
    } else {
      const makeReservationUrl = 'http://localhost:8080/bookings/add'
      let fecha_ingreso = fromDate && fromDate.split('/').reverse()
      if (fecha_ingreso) {
        if (fecha_ingreso[1].match(/^\d{1}$/)) {
          fecha_ingreso[1] = '0' + fecha_ingreso[1]
        }
        if (fecha_ingreso[2].match(/^\d{1}$/)) {
          fecha_ingreso[2] = '0' + fecha_ingreso[2]
        }
      }
      let fecha_final = toDate && toDate.split('/').reverse()
      if (fecha_final) {
        if (fecha_final[1].match(/^\d{1}$/)) {
          fecha_final[1] = '0' + fecha_final[1]
        }
        if (fecha_final[2].match(/^\d{1}$/)) {
          fecha_final[2] = '0' + fecha_final[2]
        }
      }
      
      const reservationPayload = {
        usuario: { id: JSON.parse(localStorage.getItem('user')).id },
        fecha_ingreso: fecha_ingreso.join('-'),
        hora_comienzo: reservationTime,
        fecha_final: fecha_final.join('-'),
        producto: { id: product.id },
        precio: final_price
      }
      
      const reservationHeaders = {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }

      try {
        const reservationResponse = await axios.post(makeReservationUrl, reservationPayload, reservationHeaders)
        
        if (reservationResponse && reservationResponse.data) {
          //navigate('/successfulBooking')
          navigate(`/reservation/${reservationResponse.data.id}/payment/${final_price}`);
        }
      } catch (error) {
        if (error.response && error.response.status && error.response.status === 401) {
          logout()
          alert("Tu sesión caducó, por favor iniciá sesión nuevamente para ver tus reservas")
          navigate('/login')
        } else {
          alert('Ocurrió un error al reservar el producto, por favor vuelva a intentarlo')
          
        }
      }
    }
  }
  

  return (
    <div className="reservation-detail">
      <div>
        <h1>Detalle de la reserva</h1>
        <img
          style={{ width: "100%" }}
          alt={product.imagenes[0].titulo}
          src={product.imagenes[0].url}
        />
      </div>
      <div>
        <h4 style={{ marginBottom: "0", color: "gray" }}>
          {product.categoria.titulo.toUpperCase()}
        </h4>
        <h2 style={{ margin: "0", fontSize: "2rem" }}>{product.titulo}</h2>
        <h4 style={{ margin: "0" }}>
          {<RatingToStars rating={product.rating} />}
        </h4>
        <h4>
          <FontAwesomeIcon icon={faMapMarkerAlt} /> {product.ciudad.titulo},{" "}
          {product.ciudad.pais}
        </h4>
        <hr
          style={{
            height: "1px",
            backgroundColor: "#BEBEBE",
            border: "0",
            width: "95%",
          }}
        ></hr>
        <div className="reservationDetail-check">
          <span>Check in</span>
          <span>{fromDate || "___/___/_____"}</span>
        </div>
        <hr
          style={{
            height: "1px",
            backgroundColor: "#BEBEBE",
            border: "0",
            width: "95%",
          }}
        ></hr>
        <div className="reservationDetail-check">
          <span>Check out</span>
          <span>{toDate || "___/___/_____"}</span>
        </div>
        <hr
          style={{
            height: "1px",
            backgroundColor: "#BEBEBE",
            border: "0",
            width: "95%",
          }}
        ></hr>
        <div>
          <h4>{`${daysText}`}</h4>
          <h3>{`Precio total: ARS$${final_price.toFixed(2)}`}</h3>
        </div>
        <button
        onClick={makeReservation}
        type="button"
        className="reservationDetail-button"
      >
        Iniciar pago
      </button>
      
      </div>
    </div>
  );
};

export const ProductPolicies = (props) => {
  const { product } = props;
  // const product = autos[0]
  const normas = []
  const saludSeguridad = []
  const cancelacion = []

  if (product.politicas && product.politicas.length) {
    product.politicas.forEach(politica => {
      if (politica.titulo === 'Normas reglamentarias') {
        normas.push(politica)
      } else if (politica.titulo === 'Salud y seguridad') {
        saludSeguridad.push(politica)
      } else if (politica.titulo === 'Política de cancelación') {
        cancelacion.push(politica)
      }
    })
  }

  return product.politicas && product.politicas.length ? (
      <div style={{ marginTop: "10px" }}>
        <h3 style={{ margin: 0, color: "#383B58" }}>Qué tenés que saber</h3>
        <hr style={{ height: "1px", backgroundColor: "#1DBEB4", border: "0" }} />
        <div className="product-detail-policies">
          <div>
            {
                (normas && normas.length && <>
                  <h4>Normas reglamentarias</h4>
                  {
                    normas.map(politica => <p>{politica.descripcion}</p>)
                  }
                </>) || <></>
            }
          </div>
          <div>
            {
                (saludSeguridad && saludSeguridad.length && <>
                  <h4>Salud y seguridad</h4>
                  {
                    saludSeguridad.map(politica => <p>{politica.descripcion}</p>)
                  }
                </>) || <></>
            }
          </div>
          <div>
            {
                (cancelacion && cancelacion.length && <>
                  <h4>Política de cancelación</h4>
                  {
                    cancelacion.map(politica => <p>{politica.descripcion}</p>)
                  }
                </>) || <></>
            }
          </div>
        </div>
      </div>
  ) : <></>;
};

const RedirectToLanding = (_) => {
  alert("Por favor, iniciá sesión para realizar una reserva");
  return <Navigate to="/login" />;
};

const Reservation = (_) => {
  const { productId } = useParams();
  const [product, setProduct] = React.useState("loading");
  const [fromDate, setFromDate] = React.useState();
  const [toDate, setToDate] = React.useState();
  const [reservationTime, setReservationTime] = React.useState()
  const [city,setCity] = React.useState()
  const { user } = React.useContext(UserContext);

  React.useEffect(() => {
    const getProduct = async (_) => {
      try {
        const productData = await axios.get(
          `http://localhost:8080/products/${productId}`
        );
        setProduct(productData.data);
      } catch (_) {
        alert(
          "Ocurrió un error al conectarse a la base de datos, por favor inténtelo de nuevo más tarde"
        );
        setProduct(null);
      }
    };
    getProduct();
  }, [productId]);

  if (!user) {
    return <RedirectToLanding />
  } else {
  if (product === "loading") {
    return (
    <div id="contenedor">
  <div class="contenedor-loader">
    <div class="loader"></div>
  </div>
  <div class="cargando">Iniciando reserva...</div>
</div>);
  } else {
    return (
      <div style={{ padding: "1.5rem", color: "#383B58" }}>
        <ProductDetailTop product={product} />
        <div className="reservation-container">
          <div className="reservation-data">
            <UserData user={user} ciyi={city} setCity={setCity}/>
            <ReservationCalendar
              productId={productId}
              setFromDate={setFromDate}
              setToDate={setToDate}
            />
            <Arrival setReservationTime={setReservationTime} />
          </div>
          <ReservationDetail
            fromDate={fromDate}
            toDate={toDate}
            product={product}
            city={city}
            reservationTime={reservationTime}
          />
        </div>
        <ProductPolicies product={product}/>
      </div>
    );
  }
};
};

export default Reservation;
