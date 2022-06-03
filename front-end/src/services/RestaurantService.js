import {authAxios} from '@/http-common'

class RestaurantService {

    registerRestaurant(data) {
        return authAxios.post('/restaurants/register', data)
    }

    getRestaurants(){
        return authAxios.get('/restaurants')
    }
}

export default new RestaurantService()