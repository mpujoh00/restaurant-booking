import {authAxios, noAuthAxios} from '@/http-common'

class UserService {

    getAllUsers() {
        return authAxios.get('/users')
    }

    getUser(email) {
        return authAxios.get('/users', email)
    }

    modifyUser(data){
        return authAxios.put('/users', data)
    }

    register(data){
        return noAuthAxios.post('/auth/register', data)
    }

    login(data){
        return noAuthAxios.post('/auth/login', data)
    }

    modifyPassword(data){
        return authAxios.put('/users/change-password', data)
    }
}

export default new UserService()