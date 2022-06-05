import {authAxios} from '@/http-common'

class RestaurantService {

    registerRestaurant(data) {
        return authAxios.post('/restaurants/register', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getRestaurants(){
        return authAxios.get('/restaurants', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getRestaurant(id){
        return authAxios.get('/restaurants/' + id, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    updateRestaurant(data){
        return authAxios.put('/restaurants', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getAllRestaurants(){
        return authAxios.get('/admin/restaurants', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getDisabledRestaurants(){
        return authAxios.get('/admin/restaurants/disabled', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getPendingRestaurants(){
        return authAxios.get('/admin/restaurants/pending', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }s

    updateRestaurantStatus(id){
        return authAxios.put('/admin/restaurants/change-status/' + id, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }
}

export default new RestaurantService()