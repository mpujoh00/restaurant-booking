<template>
  <v-app>
    <!-- menu, cabecera -->
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

    <!-- página -->
    <v-main class="main">
      <router-view></router-view>
    </v-main>

    <!-- pie de página -->
    <v-footer padless color="#ffd6dd">
      <v-col class="text-center" cols="12">
        <v-btn icon class="mr-2 mb-1">
          <a href="https://www.linkedin.com/in/micaela-pujol-higueras/" style="text-decoration: none">
            <v-icon size="26" color="black">
              mdi-linkedin
            </v-icon>
          </a>
        </v-btn>
        2022 - <strong>Restaurant Booking</strong>
      </v-col>
    </v-footer>
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
.main {
  //background: url('~@/assets/images/fondo9.png') center;
  background-color: #ffebee;
  //background-size: cover;
}
</style>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  data() {
    return {
      restaurantMenus:[
        {title: 'Restaurant administration', route:'/restaurant/admin'},
        {title: 'Bookings', route:'/restaurant/admin/bookings'},
        {title: 'Tables administration', route:'/restaurant/admin/tables'},
        {title: 'Courses administration', route:'/restaurant/admin/courses'},
        {title: 'Ratings', route:'/restaurant/admin/ratings'},
      ],
      clientMenus:[
        {title: 'Home', route:'/home'},
        {title: 'Bookings', route:'/bookings'},
      ],
      adminMenus:[
        {title: 'Restaurants administration', route:'/admin/restaurants'},
        {title: 'Users administration', route:'/admin/users'},
        {title: 'Categories administration', route:'/admin/categories'},
        {title: 'Ratings administration', route:'/admin/ratings'},
      ]
    }
  },
  computed: {
      ...mapState([
          'currentUser'
      ])
  },
  methods: {
    ...mapActions([
      'changeErrorMessage'
    ]),
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
  watch: {
    $route (){
      this.changeErrorMessage(null)
    }
  }
}
</script>