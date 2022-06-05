import {authAxios} from '@/http-common'

class TableService {

    getRestaurantTables(restaurantId){
        return authAxios.get('/tables/restaurant/' + restaurantId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    createTable(restaurantId, data){
        return authAxios.post('/tables/' + restaurantId, data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    deleteTable(tableId){
        return authAxios.delete('/tables/delete/' + tableId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }
}

export default new TableService()