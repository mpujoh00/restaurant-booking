import axios from 'axios'

// common configuration
axios.defaults.headers.post["Content-Type"] = "application/json"
axios.defaults.baseURL = "http://localhost:8765/api/v1"

// for requests where authentication is needed
const authAxios = axios.create({
    headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
})
console.log(authAxios.defaults.headers)

// for requests where there is no need to be authenticated
const noAuthAxios = axios.create()

export {
    authAxios,
    noAuthAxios,
    axios
}