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
    token: null
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
    logout: state => state.token = null,
  },
  actions: {
    // Login
    doLogin({ commit }, loginData) {
      console.log("Logging user with email " + loginData.email)

      commit('loginStart')  // Executes (commits) loginStart mutation

      UserService.login(loginData)
      .then(response => {
        // saves token to local storage
        localStorage.setItem('token', response.data.token)
        // correct login
        commit('updateToken', response.data.token)
        commit('loginStop', null)
        // redirects to account page
        router.push('/account')
      })
      .catch(error => {
        commit('loginStop', error)
        // incorrect login
        commit('updateToken', null)
      })
    },
    // executed everytime the application is loaded (in App.vue created())
    fetchToken({ commit }) {
      commit('updateToken', localStorage.getItem('token'))
    },
    logout({ commit }) {
      // removes user's token
      localStorage.removeItem('token')
      commit('logout')
      router.push('/login')
    }
  },
  modules: {
  }
})
