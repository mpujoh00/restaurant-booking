<template>
  <div class="home">
    <br/>
    <h1>Restaurants</h1>
    <br/>

    <v-container>
      <v-row dense>
        <v-col v-for="(restaurant, index) in restaurants" cols="12" :key="index">
          <v-card @click="openRestaurant(restaurant)" class="ma-1">
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
import RestaurantAdminService from '@/services/RestaurantAdminService'
import router from '@/router'

export default {
  name: 'RestaurantAdmin',
  data() {
    return {
      restaurants: [],
    }
  },
  methods: {
    openRestaurant(restaurant){
      console.log('opening restaurant: ' + restaurant.name)
      router.push({name: 'restaurantAdmin', params: {id: restaurant.id}})
    }
  },
  mounted() {
    console.log('getting restaurants')
    RestaurantAdminService.getRestaurants().then(response => {
      this.restaurants = response.data
    })
  },
}
</script>