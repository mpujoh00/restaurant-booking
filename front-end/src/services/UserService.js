import http from '@/http-common'

class UserService {

    getAllUsers() {
        return http.get('/users')
    }

    getUser(email) {
        return http.get('/users', email)
    }

    register(data){
        return http.post('/auth/register', data)
    }

    login(data){
        return http.post('/auth/login', data)
    }

}

export default new UserService()