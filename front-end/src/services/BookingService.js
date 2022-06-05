import {authAxios} from '@/http-common'

class BookingService {

    getRestaurantsSlotsForPeopleDate(restaurantId, date, numPeople) {
        return authAxios.get('/reservation-slots/restaurant/' + restaurantId + '/people/' + numPeople + '/date/' + date, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    createReservation(data){
        return authAxios.post('/reservations', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getAllRestaurantReservations(restaurantId){
        return authAxios.get('/admin/reservations/restaurant/' + restaurantId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    changeReservationStatus(reservationId, status){
        return authAxios.put('/admin/reservations/' + reservationId + '/status/' + status, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getActiveUserReservations(userId){
        return authAxios.get('/reservations/user/active/' + userId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getInactiveUserReservations(userId){
        return authAxios.get('/reservations/user/inactive/' + userId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    cancelReservation(reservationId){
        return authAxios.put('/reservations/' + reservationId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }
}

export default new BookingService()