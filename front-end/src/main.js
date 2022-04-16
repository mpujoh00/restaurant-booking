import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
import DisableAutocomplete from 'vue-disable-autocomplete'

Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  store,
  DisableAutocomplete,
  render: h => h(App)
}).$mount('#app')
