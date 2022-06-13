<template>
  <div class="home">
    <br/>
    <h1>Welcome to Restaurant Booking!!</h1>
    <br/>
    <v-container class="container">
      <v-row dense>
        <v-col v-for="(restaurant, index) in restaurants" :key="index">
          <v-card @click="openRestaurant(restaurant)" class="ma-1 pa-6" color="#f7f7f7">
            <div class="d-flex flex-no-wrap justify-space-between">
              <div class="restaurant">
                <div>
                  <v-card-title v-text="restaurant.name" class="text-h4"></v-card-title>
                </div>
                <div style="margin-top: -10%;">
                  <v-card-subtitle v-text="restaurant.location" style="font-size: large;"></v-card-subtitle>
                </div>                
                <div class="categories ml-3">
                    <v-chip v-for="(category, index) in restaurant.categories" 
                        :key="index"
                        color="#ffe6e9"
                        class="mr-2 mb-3">
                        {{ category.name }}
                    </v-chip>
                </div>
              </div>
              <v-img 
                :src="'data:image/jpg;base64,' + restaurant.logo.data" 
                alt="logo"
                max-height="150"
                max-width="450"
              ></v-img>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

  </div>
</template>

<style scoped>
.restaurant {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.container {
  max-width: 50%;
  align-content: center;
  justify-content: center;
}
</style>

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
    RestaurantService.getEnabledRestaurants().then(response => {
      this.restaurants = response.data
    })
  },
}
</script>
