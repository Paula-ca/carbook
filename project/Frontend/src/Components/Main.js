import React, { useState , useRef, useEffect } from 'react'
import axios from 'axios'
import moment from 'moment'

import DatalistInput, { useComboboxControls } from 'react-datalist-input';
import 'react-datalist-input/dist/styles.css'

import Calendar from 'react-calendar'
import 'react-calendar/dist/Calendar.css'

import Categorias from './Categorias';
import Recomendations from './Recomendations';

const bingApiKey = 'AtC0h-1-QDcbqYJ6y_qEMvjj5iN4qmDfrj8oYaUkK3bkJvv4CGBPtPmte4DTvlSc'

const CalendarFilter = ({ date, setDate }) => {
    const [showCalendar, setShowCalendar] = useState(false)
    let fromDate, toDate

    const handleChange = (inputDate) => {
        setDate(inputDate)
    }

    if (date && date.length) {
        [fromDate, toDate] = date
    }

    return (
      <div>
        <button className='search-form-button select-date-search-form' type='button' onClick={_ => setShowCalendar(!showCalendar)}><span role="img" aria-label="calendar">📆</span> Desde - Hasta</button>
        {!showCalendar && fromDate && toDate && <div><p>Desde: {moment(fromDate).format('DD/MM/YYYY')}</p><p>Hasta: {moment(toDate).format('DD/MM/YYYY')}</p></div>}
        <Calendar
          className={showCalendar ? "" : "hide"}
          value={date}
          onChange={handleChange}
          minDate={new Date()}
          selectRange={true}
          showDoubleView={true}
        />
      </div>
    )
}

const LocationFilter = ({value, setValue, getLocation, cities, setCities, cityId, setCityId}) => {
    // const [loading, setLoading] = React.useState(false)
    React.useEffect(() => {
        const getCities = async _ => {
            // setLoading(true)
            try {
                const citiesData = await axios.get('http://localhost:8080/cities/list')
                setCities(citiesData.data)
            } catch (_) {
                setCities(null)
                alert('Ocurrió un error al conectarse a la base de datos, por favor inténtelo de nuevo más tarde')
            }
            // setLoading(false)
        }
        getCities()
    }, [setCities])


    return (
        <div className='location-filter'>
            {
                cities ? <DatalistInput id='city-search'
                    value={value}
                    setValue={setValue}
                    placeholder='¿A dónde vamos?'
                    items={cities.map((city) => { 
                        return { key: city.id, id: city.id, value: city.titulo } 
                    })}
                    onSelect={(item) => {
                        setValue(item.value)
                        setCityId(item.id)
                    }}
                /> : <input style={{ cursor: 'not-allowed' }} disabled={true} type='text' id='city-search'/>
            }
            
            <button className='search-form-button' type='button' onClick={getLocation}>Usar tu ubicación actual</button>
        </div>
    )
}

const SearchField = props => {
    const { setLocation, setDates } = props
    const { setValue, value } = useComboboxControls({ initialValue: '' })
    const [date, setDate] = useState()
    const [cities, setCities] = React.useState([{ titulo: 'Aguardá un momento...' }])
    const [cityId, setCityId] = React.useState()

    const getLocation = async (_) => {
        let latitude, longitude
        navigator.geolocation.getCurrentPosition(async function(position) {
            latitude = position.coords.latitude
            longitude = position.coords.longitude
            if (latitude && longitude) {
                let city
                const getLocationUrl = `http://dev.virtualearth.net/REST/v1/Locations/${`${latitude},${longitude}`}?includeEntityTypes=PopulatedPlace&includeNeighborhood=1&key=${bingApiKey}`
                try {
                    const result = await axios.get(getLocationUrl)
                    city = result.data.resourceSets && result.data.resourceSets[0] && result.data.resourceSets[0].resources && result.data.resourceSets[0].resources[0] && result.data.resourceSets[0].resources[0].address &&  result.data.resourceSets[0].resources[0].address.locality
                } catch (error) {
                    alert("Ocurrió un error al obtener tu ubicación")
                    console.log("error", error)
                } finally {
                    const cityNames = cities.map(cityObject => cityObject.titulo)
                    if (city && cityNames.indexOf(city) >= 0) {
                        setValue(city)
                    } else {
                        setValue('')
                        if (!cityNames.indexOf(city) >= 0) {
                            alert('Todavía no llegamos a tu ciudad, por favor volvé a consultar más adelante. Seguimos expandiéndonos!')
                        } else {
                            alert('Ocurrió un error al obtener tu ubicación. Comprobá que hayas dado permiso a que la podamos ver')
                        }
                    }
                }
            }
        })
    }

    const submitForm = (e) => {
        e.preventDefault()
        if (cityId) {
            setLocation(cityId)
        }
        if (date) {
            const from = moment(date[0]).format('YYYY-MM-DD')
            const to = moment(date[1]).format('YYYY-MM-DD')
            setDates([from, to])
        }
    }

    return (
        <div className='search-field'>
            <h1>Buscá el auto ideal para vos</h1>
            <p>Completá los siguientes campos como necesites, y nosotros nos encargamos del resto</p>
            <form className='search-form' onSubmit={submitForm}>
                <div className="form-field-div">
                    <LocationFilter
                        value={value}
                        setValue={setValue}
                        getLocation={getLocation}
                        cities={cities}
                        setCities={setCities}
                        cityId={cityId}
                        setCityId={setCityId}
                    />
                </div>
                <div className="form-field-div">
                    <CalendarFilter
                        className="form-field-div"
                        date={date}
                        setDate={setDate}
                    />
                </div>
                <button className='search-form-button search-form-button-send' type='submit'>Buscar</button>
            </form>
        </div>
    )
}

const Main = _ => {
    const [category, setCategory] = React.useState()
    const [location, setLocation] = React.useState()
    const [dates, setDates] = React.useState()
    const recommendationsRef = useRef(null);

  useEffect(() => {
    if ((location || dates || category) && recommendationsRef.current) {
      recommendationsRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [location,dates,category]);
    
    return (
        <main>
            <SearchField location={location} setDates={setDates} setLocation={setLocation} />
            <Categorias categoryFilter={category} setCategoryFilter={setCategory} />
            <div  ref={recommendationsRef}>
                <Recomendations dates={dates} location={location} category={category} />
            </div>        
        </main>
    )
}

export default Main