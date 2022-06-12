import {authAxios} from '@/http-common'
import {noAuthAxios} from '../http-common'

class RestaurantService {

    registerRestaurant(data) {
        return authAxios.post('/restaurants/register', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getEnabledRestaurants(){
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
    }

    updateRestaurantStatus(id){
        return noAuthAxios.put('/admin/restaurants/change-status/' + id, null, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    addCategory(id, categoryName){
        return authAxios.put('/restaurants/' + id + '/add-category', categoryName, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token'),
                'Content-Type': 'text/plain'
            }
        })
    }

    removeCategory(id, categoryName){
        return authAxios.put('/restaurants/' + id + '/remove-category', categoryName, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token'),
                'Content-Type': 'text/plain'
            }
        })
    }
}

export default new RestaurantService()