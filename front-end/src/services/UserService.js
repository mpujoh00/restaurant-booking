import {authAxios, noAuthAxios} from '@/http-common'

class UserService {

    getAllUsers() {
        return authAxios.get('/admin/users', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getUser(email) {
        return authAxios.get('/users', email, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    modifyUser(data){
        return authAxios.put('/users', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    register(data){
        return noAuthAxios.post('/auth/register', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    login(data){
        return noAuthAxios.post('/auth/login', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    modifyPassword(data){
        return authAxios.put('/users/change-password', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    deleteUser(email){
        return authAxios.delete('/users/delete/' + email, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    registerAdmin(data){
        return authAxios.post('/admin/users', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    deleteAdmin(adminEmail){
        return authAxios.delete('/admin/users/delete/' + adminEmail, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    updateUserStatus(userEmail){
        return authAxios.put('/admin/users/change-status/' + userEmail, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }
}

export default new UserService()