import {authAxios} from '@/http-common'

class BookingService {

    getRestaurantsSlotsForPeopleDate(restaurantId, date, numPeople) {
        return authAxios.get('/reservation-slots/restaurant/' + restaurantId + '/people/' + numPeople + '/date/' + date)
    }

    createReservation(data){
        console.log(data)
        return authAxios.post('/reservations', data)
    }
}

export default new BookingService()