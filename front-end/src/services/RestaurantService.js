import {authAxios} from '@/http-common'

class RestaurantService {

    registerRestaurant(data) {
        return authAxios.post('/restaurants/register', data)
    }

    getRestaurants(){
        return authAxios.get('/restaurants')
    }

    getRestaurant(id){
        console.log(id)
        return authAxios.get('/restaurants', id)
    }
}

export default new RestaurantService()