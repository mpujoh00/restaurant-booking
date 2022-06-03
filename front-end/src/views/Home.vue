<template>
  <div class="home">
    <br/>
    <h1>Welcome to Restaurant Booking!!</h1>
    <br/>

    <v-container>
      <v-row dense>
        <v-col v-for="(restaurant, index) in restaurants" cols="12" :key="index">
          <v-card @click="openRestaurant(restaurant)">
            <v-card-title v-text="restaurant.name" class="text-h5">
            </v-card-title>
            <v-card-subtitle v-text="restaurant.location">
            </v-card-subtitle>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

  </div>
</template>

<script>
import RestaurantService from '@/services/RestaurantService'
import router from '@/router'

export default {
  name: 'Home',
  data() {
    return {
      restaurants: [],
    }
  },
  methods: {
    openRestaurant(restaurant){
      console.log('opening restaurant: ' + restaurant.name)
      router.push({name: 'restaurant', params: {id: restaurant.id}})
    }
  },
  mounted() {
    console.log('getting restaurants')
    this.restaurants = RestaurantService.getRestaurants()
  },
}
</script>
