import {authAxios} from '@/http-common'

class RestaurantAdminService {

    getRestaurants(){
        return authAxios.get('/admin/restaurants')
    }

}

export default new RestaurantAdminService()