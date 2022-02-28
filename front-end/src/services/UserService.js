import http from '@/http-common'

class UserService {

    getAllUsers() {
        return http.get('/users')
    }

    getUser(email) {
        return http.get('/users', email)
    }

    modifyUser(data){
        return http.put('/users', data)
    }

    register(data){
        return http.post('/auth/register', data)
    }

    login(data){
        return http.post('/auth/login', data)
    }

    modifyPassword(data){
        return http.put('/users/changePassword', data)
    }
}

export default new UserService()