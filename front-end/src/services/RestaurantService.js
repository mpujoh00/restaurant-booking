import {authAxios} from '@/http-common'

class RestaurantService {

    registerRestaurant(data) {
        return authAxios.post('/restaurants/register', data)
    }
}

export default new RestaurantService()