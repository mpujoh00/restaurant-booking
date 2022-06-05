<template>
  <v-app>
    <!-- CREAR DISTINTAS BARS PARA NO LOGGED, USER, ADMIN -->
    <v-app-bar app color="#ff99a8" dense elevation="3">
      <!--icono-->
      <v-app-bar-title class="appBarTitle">Restaurant Booking</v-app-bar-title>
      <!-- secciones -->
      <v-list class="d-flex align-center menu" style="background: transparent; font-weight: 550;">
        <v-list-item v-for="(menu, index) in getMenus()" :key="index" :to="menu.route">
          <v-list-item-title>{{menu.title}}</v-list-item-title>
        </v-list-item>
      </v-list>      
      <v-spacer/>
      <!-- login, usuario -->
      <v-btn :to="currentUser ? '/account': '/login'" plain>
        <v-icon large>mdi-account-circle-outline</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  align-content: center;
}
.appBarTitle {
  margin-left: 2%;
  margin-right: 3%;
  text-align: center;
  font-weight: bold;
}
</style>

<script>
import { mapState } from 'vuex'

export default {
  data() {
    return {
      restaurantMenus:[
        {title: 'Bookings', route:'/restaurant/admin/bookings'},
        {title: 'Restaurant administration', route:'/restaurant/admin'},
        {title: 'Tables administration', route:'/restaurant/admin/tables'}
      ],
      clientMenus:[
        {title: 'Home', route:'/home'},
        {title: 'Bookings', route:'/bookings'},
      ],
      adminMenus:[
        {title: 'Restaurants administration', route:'/admin/restaurants'},
        {title: 'Users administration', route:'/admin/users'},
      ]
    }
  },
  computed: {
      ...mapState([
          'currentUser'
      ])
  },
  methods: {
    getMenus() {
      if(this.currentUser !== null && this.currentUser.role == "ROLE_ADMIN"){
        return this.adminMenus
      }
      else if(this.currentUser !== null && this.currentUser.role == "ROLE_CLIENT"){
        return this.clientMenus
      }
      else if(this.currentUser !== null && this.currentUser.role == "ROLE_RESTAURANT"){
        return this.restaurantMenus
      }
    }
  },
}
</script>