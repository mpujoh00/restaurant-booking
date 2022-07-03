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

    getFlaggedRatings(){        
        return authAxios.get('/ratings/flag', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    flagRating(ratingId){        
        return authAxios.put('/ratings/' + ratingId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    changeRatingStatus(ratingId, ratingStatus){        
        return authAxios.put('/ratings/' + ratingId + '/status/' + ratingStatus, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }
}

export default new RatingService()