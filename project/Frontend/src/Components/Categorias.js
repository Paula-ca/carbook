import React from "react"
import axios from 'axios'

const capitalizeFirstLetter = (word) => word.charAt(0).toUpperCase() + word.slice(1)

const Categorias = (props) => {

    const [categorias, setCategorias] = React.useState()
    const { categoryFilter, setCategoryFilter } = props


    React.useEffect(() => {
        const getCategories = async _ => {
            try {
                const categoriasData = await axios.get('http://localhost:8080/categories/list/false')
                
                setCategorias(categoriasData.data)
            } catch (_) {
                alert('Ocurrió un error al conectarse a la base de datos, por favor inténtelo de nuevo más tarde')
            }
        }
        getCategories()
    }, [])

    const updateCategoryFilter = async (categoryId) => {
     
        if (categoryId===categoryFilter) {
            setCategoryFilter(null)
        } else {
            setCategoryFilter(categoryId)
        }
    }
   
    return !categorias || !categorias.length ? (
        <></>
    ) : (
        <div className="category-field">
            <h2>Buscá por categoría</h2>
            <div className="card-wrapper">
                {
                    categorias.map((categoria) => {
                        return (
                            <div style={categoryFilter === categoria.id ? { background: '#545776', color: 'white' } : {}} onClick={_ => updateCategoryFilter(categoria.id)} key={categoria.id} className="card">
                                <div className="card-body">
                                    <img src={categoria.urlImagen} className="card-image" alt={categoria.titulo} />
                                    <h2 className="card-title">{categoria.titulo.split(' ').map(word => capitalizeFirstLetter(word)).join(' ')}</h2>
                                    <p className="card-description">{categoria.descripcion.charAt(0).toUpperCase() + categoria.descripcion.slice(1)}</p>
                                </div>
                            </div>
                        )
                    })
                }
            </div>
        </div>
    )
}

export default Categorias 
