import Vue from 'vue'
import Vuex from 'vuex'
import UserService from '@/services/UserService'
import RestaurantService from '@/services/RestaurantService'
import router from '@/router'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // Login state
    logginIn: false,
    loginError: null,
    token: null,
    currentUser: null,
    editPasswordError: null,
    deleteUserError: null,
    tempUser: null,
    currentRestaurant: null,
  },
  mutations: {
    // Login
    loginStart: state => state.logginIn = true,
    loginStop: (state, errorMessage) => {
      state.logginIn = false
      state.loginError = errorMessage
    },
    updateToken: (state, token) => {
      state.token = token
    },
    logout: state => {
      state.token = null
      state.currentUser = null
      state.currentRestaurant = null
    },
    updateCurrentUser: (state, user) => {
      state.currentUser = user
      localStorage.setItem('currentUser', JSON.stringify(user))
    },
    editPasswordError: (state, errorMessage) => {
      state.editPasswordError = errorMessage
    },
    deleteUserError: (state, errorMessage) => {
      state.deleteUserError = errorMessage
    },
    saveTempUser: (state, user) => {
      state.tempUser = user
    },
    updateCurrentRestaurant: (state, restaurant) => {
      state.currentRestaurant = restaurant
      localStorage.setItem('currentRestaurant', JSON.stringify(restaurant))
    }
  },
  actions: {
    // Login
    login({ commit }, loginData) {
      console.log("Logging user with email " + loginData.email)

      commit('loginStart')  // executes (commits) loginStart mutation

      UserService.login(loginData)
      .then(response => {
        console.log('Correctly logged in')
        // saves token to local storage
        console.log(response.data.token)
        localStorage.setItem('token', response.data.token)
        console.log(localStorage.getItem('token'))
        // correct login
        commit('updateToken', response.data.token)
        commit('loginStop', null)
        commit('updateCurrentUser', {
          id: response.data.user.id,
          email: response.data.user.email,
          fullname: response.data.user.fullname,
          role: response.data.user.roles[0].name,
        })
        if(response.data.user.restaurant !== null){
          commit('updateCurrentRestaurant', response.data.user.restaurant)
        }
        // redirects to account page
        if(response.data.user.roles[0].name === 'ROLE_CLIENT')
          router.push('/home')
        else if(response.data.user.roles[0].name === 'ROLE_RESTAURANT')
          router.push('/restaurant/admin/bookings')
        else
          router.push('/admin/restaurants')
      })
      .catch(() => {
        console.log('Couldn\'t log in')
        commit('loginStop', "Incorrect credentials")
        // incorrect login
        commit('logout')
      })
    },
    // executed everytime the application is loaded (in App.vue created())
    fetchToken({ commit }) {
      commit('updateToken', localStorage.getItem('token'))
      commit('updateCurrentUser', JSON.parse(localStorage.getItem('currentUser')))
      commit('updateCurrentRestaurant', JSON.parse(localStorage.getItem('currentRestaurant')))
    },
    logout({ commit }) {
      // removes user's token
      localStorage.removeItem('token')
      localStorage.removeItem('currentUser')
      localStorage.removeItem('currentRestaurant')
      commit('logout')
      router.push('/login')
    },
    // Registration
    registerUser({ dispatch }, user){
      console.log("Registering user with email " + user.email + " and role " + user.role)
      // registers user
      UserService.register({
        email: user.email,
        password: user.password,
        fullname: user.fullname,
        role: user.role
      })
      .then(() => {
        // login
        dispatch('login', {
          email: user.email,
          password: user.password
        })
      })
      .catch(() => {
        console.log("Couldn't register user")
      })
    },
    // Modification of the user
    modifyUser({ commit }, modifiedUser) {
      console.log("Modifying user")
      return new Promise((resolve, reject) => {
        UserService.modifyUser(modifiedUser)
        .then(response => {
          console.log("updating user..")
          console.log(response.data)
          // updates user
          commit('updateCurrentUser', {
            id: response.data.id,
            email: response.data.email,
            fullname: response.data.fullname,
            role: response.data.roles[0].name
          })
          resolve()
          router.push('/login')
        })
        .catch(error => {
          console.log(error)
          reject(error)
        })
      })      
    },
    modifyPassword({ commit }, modifiedPassword) {
      console.log("Modifying password")
      return new Promise((resolve, reject) => {
        UserService.modifyPassword({
          currentPassword: modifiedPassword.currentPassword,
          newPassword: modifiedPassword.newPassword
        })
        .then(response => {
          console.log(response)
          // saves updated token
          commit('updateToken', response.data)
          //commit('editPasswordError', null)
          resolve()
        })
        .catch(error => {
          if(error.response.status === 400){
            //commit('editPasswordError', error.response.data)
            reject(error.response.data)
          }
        })
      })      
    }, 
    deleteUser({ commit, dispatch }, userEmail) {
      console.log("Deleting user")
      return new Promise((resolve, reject) => {
        UserService.deleteUser(userEmail)
        .then(() => {
          console.log("User deleted")
          dispatch('logout')
          resolve()
        })
        .catch(error => {
          if(error.response.status == 404){ // user not found
            commit('deleteUserError', "Can't delete, user not found")
          }
          else{ // user not the same as logged in
            commit('deleteUserError', "Can't delete other user than self")
          }
          reject()
        })
      })
    },
    saveTempUser({ commit }, user){
      console.log('Saving temporary user')
      commit('saveTempUser', user)
    },
    removeTempUser({ commit }){
      console.log('Removing temporary user')
      commit('saveTempUser', null)
    },
    registerRestaurant({ dispatch, commit }, data){

      console.log('Registering user')
      UserService.register(data.user)
      .then(() => {
        commit('saveTempUser', null)
        UserService.login({
          email: data.user.email,
          password: data.user.password
        })
        .then(response => {
          console.log('logged in')
          localStorage.setItem('token', response.data.token)
          commit('updateToken', response.data.token)
          commit('updateCurrentUser', {
            id: response.data.user.id,
            email: response.data.user.email,
            fullname: response.data.user.fullname,
            role: response.data.user.roles[0].name
          })
          console.log('Registering restaurant')
          RestaurantService.registerRestaurant(data.restaurant)
          .then(response => {
            console.log('Restaurant registered')
            dispatch('login', {
              email: data.user.email,
              password: data.user.password
            })
            RestaurantService.saveLogo(response.data.id, data.logo)
            .then(response => {
              console.log('Restaurant\'s logo saved')
              commit('updateCurrentRestaurant', response.data)
            })
            .catch(error => {
              console.log('Couldn\'t save restaurant\'s logo: ', error)
              commit('logout')
            })
          })
          .catch(error => {
            console.log('Couldn\'t register restaurant: ', error)
            commit('logout')
          })
        })
        .catch(() => {
          console.log('Couldn\'t log in')
          commit('logout')
        })        
      })
      .catch(error => {
        console.log('Couldn\'t register user: ', error)
      })
    },
    modifyRestaurant({ commit }, restaurant){
      commit('updateCurrentRestaurant', restaurant)
    }
  }
})
