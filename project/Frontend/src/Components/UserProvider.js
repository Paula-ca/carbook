import React from 'react'
import UserContext from "../Context/UserContext"
import axios from 'axios'

const loginUrl = 'http://localhost:8080/auth/signin'

const initializeUser = _ => {
    const user = localStorage.getItem('user')
    return user ? JSON.parse(user) : null
}

const UserProvider = (props) => {
    const [user, setUser] = React.useState(initializeUser())
    
    const login = async (email, password) => {
        try {
            const loginResponse = await axios.post(loginUrl, {email, contrasenia: password})
            if (loginResponse && loginResponse.data) {
                const authenticatedUser = { id: loginResponse.data.id, email, name: loginResponse.data.nombre, surname: loginResponse.data.apellido, rol: loginResponse.data.rol ,city: loginResponse.data.ciudad}
                await setUser(authenticatedUser) // TODO: add pronouns
               
                localStorage.setItem('user', JSON.stringify(authenticatedUser))
                localStorage.setItem('token', loginResponse.data.token)
               
                return authenticatedUser
                
            }
        } catch (_) {
            return false
        }
    }

    const signup = async (nombre, apellido, email,ciudad, contrasenia) => {
        const signupUrl = 'http://localhost:8080/auth/signup'
        const userPayload = { nombre, apellido, email,ciudad, contrasenia }

        try {
            const signupResponse = await axios.post(signupUrl, userPayload)
            if (signupResponse && signupResponse.data) {
                const authenticatedUser = { id: signupResponse.data.id, email, name: signupResponse.data.nombre, surname: signupResponse.data.apellido , city: signupResponse.data.ciudad}
                await setUser(authenticatedUser) // TODO: add pronouns
                localStorage.setItem('user', JSON.stringify(authenticatedUser))
                localStorage.setItem('token', signupResponse.data.token) // TODO: agregar token al registro
                return 1
            }  
        } catch (error) {
            if (error && error.response && error.response.data) {
                if (error.response.data === 'Este correo ya se encuentra registrado') {
                    return 2 // Error code 1: email provided already in use
                }
            }

            return -1
        }
    }

    const logout = _ => {
        localStorage.clear()
        setUser(null)
    }

    return (
        <UserContext.Provider value={{ user, login, logout, signup, setUser }}>
            {props.children}
        </UserContext.Provider>
    )
}

export default UserProvider