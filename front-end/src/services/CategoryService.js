import {authAxios} from '@/http-common'

class CategoryService {

    getAllCategories(){
        return authAxios.get('/categories', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    createCategory(name){
        return authAxios.post('/categories', name, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }
}

export default new CategoryService()