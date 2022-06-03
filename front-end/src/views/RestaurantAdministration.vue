<template>
  <div class="home">
    <br/>
    <h1>{{ restaurant.name }}</h1>
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
import RestaurantService from '@/services/RestaurantService'

export default {
    props: [
        'id'
    ],
    data() {
        return {
            restaurant: null
        }
    },
    mounted() {
        RestaurantService.getRestaurant(this.id).then(response => {
            this.restaurant = response.data[0]
        })
    },
}
</script>