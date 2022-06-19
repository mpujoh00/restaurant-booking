import {authAxios} from '@/http-common'

class RatingService {

    createRating(data){
        return authAxios.post('/ratings', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getRestaurantRatings(restaurantId){        
        return authAxios.get('/ratings/restaurant/' + restaurantId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

}

export default new RatingService()