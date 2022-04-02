import Vue from 'vue'
import Vuex from 'vuex'
import UserService from '@/services/UserService'
import router from '@/router'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // Login state
    logginIn: false,
    loginError: null,
    token: null,
    currentUser: null,
    editPasswordError: null
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
    },
    updateCurrentUser: (state, user) => {
      state.currentUser = user
      localStorage.setItem('currentUser', JSON.stringify(user))
    },
    editPasswordError: (state, errorMessage) => {
      state.editPasswordError = errorMessage
    }
  },
  actions: {
    // Login
    login({ commit }, loginData) {
      console.log("Logging user with email " + loginData.email)

      commit('loginStart')  // executes (commits) loginStart mutation

      UserService.login(loginData)
      .then(response => {
        // saves token to local storage
        localStorage.setItem('token', response.data.token)
        // correct login
        commit('updateToken', response.data.token)
        commit('loginStop', null)
        commit('updateCurrentUser', {
          email: response.data.user.email,
          fullname: response.data.user.fullname,
          role: response.data.user.roles[0].name
        })
        // redirects to account page
        router.push('/account')
      })
      .catch(() => {
        commit('loginStop', "Incorrect credentials")
        // incorrect login
        commit('updateToken', null)
        commit('updateCurrentUser', null)
      })
    },
    // executed everytime the application is loaded (in App.vue created())
    fetchToken({ commit }) {
      commit('updateToken', localStorage.getItem('token'))
      commit('updateCurrentUser', JSON.parse(localStorage.getItem('currentUser')))
    },
    logout({ commit }) {
      // removes user's token
      localStorage.removeItem('token')
      localStorage.removeItem('currentUser')
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
            email: response.data.email,
            fullname: response.data.fullname,
            role: response.data.roles[0].name
          })
          resolve()
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
          commit('editPasswordError', null)
          resolve()
        })
        .catch(error => {
          if(error.response.status === 400){
            commit('editPasswordError', error.response.data)
            reject(error.response.data)
          }
        })
      })      
    }
  }
})
