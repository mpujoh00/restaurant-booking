<template>
  <v-app>
    <!-- CREAR DISTINTAS BARS PARA NO LOGGED, USER, ADMIN -->

    <v-app-bar app color="pink lighten-3" dense elevation="3">
      <!--icono-->
      <v-app-bar-title style="margin-left: 10px; margin-right: 40px;">Restaurant Booking</v-app-bar-title>
      <!-- secciones -->
      <v-list class="d-flex align-center menu" style="background: transparent">
        <v-list-item v-for="(menu, index) in menus" :key="index" :to="menu.route">
          <v-list-item-title>{{menu.title}}</v-list-item-title>
        </v-list-item>
      </v-list>      
      <v-spacer/>
      <!-- login, usuario -->
      <v-btn to="/login" plain>
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
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>

<script>

export default {
  data() {
    return {
      menus:[
        {title: 'Home', route:'/home'},
        {title: 'About', route:'/about'}
      ]
    }
  }
}
</script>