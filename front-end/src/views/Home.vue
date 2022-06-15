<template>
  <div class="home">
    <v-container class="container">
      <v-row justify="center" class="mt-5">
        <v-col md="4">
          <v-text-field
            v-model="citySearch"
            solo
            label="Search city"
            append-icon="mdi-magnify"
            color="grey"
            @click:append="search"
          ></v-text-field>
        </v-col>
        <v-col md="2">
          <v-select
            v-model="categoriesSelected"
            :items="categories"
            label="Categories"
            item-text="name" 
            multiple
            chips
            return-object
            solo
            color="grey"
            @change="search"
          ></v-select>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col md="6">
          <div class="categories mb-5">
            <v-chip v-for="(category, index) in categoriesSelected" 
              :key="index"
              color="#ffe6e9"
              class="mr-2 mb-3"
              close
              @click:close="removeCategory(index)">
              {{ category.name }}
            </v-chip>
          </div>
        </v-col>
      </v-row>
      <v-row dense v-for="(restaurant, index) in restaurants" :key="index" justify="center">
        <v-col md="6">
          <v-card @click="openRestaurant(restaurant)" class="ma-1 pa-6" color="#f7f7f7">
            <div class="d-flex flex-no-wrap justify-space-between">
              <v-img 
                :src="'data:image/jpg;base64,' + restaurant.logo.data" 
                alt="logo"
                height="150"
                max-width="150"
                class="rounded-lg"
              ></v-img>
              <div class="restaurant">
                <div>
                  <v-card-title v-text="restaurant.name" class="text-h4"></v-card-title>
                </div>
                <div style="margin-top: -10%;">
                  <v-card-subtitle style="font-size: large;">
                    {{ restaurant.address }}, {{ restaurant.location }}
                  </v-card-subtitle>
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
              <v-spacer></v-spacer>
            </div>
          </v-card>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col md="6">
          <h3 v-if="restaurants.length == 0">No available restaurants</h3>
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
  margin-left: 3%;
}
.container {
  max-width: 80%;
  align-content: center;
  justify-content: center;
}
</style>

<script>
import RestaurantService from '@/services/RestaurantService'
import CategoryService from '@/services/CategoryService'
import router from '@/router'

export default {
  name: 'Home',
  data() {
    return {
      restaurants: [],
      categories: [],
      categoriesSelected: [],
      citySearch: '',
    }
  },
  methods: {
    openRestaurant(restaurant){
      console.log('opening restaurant: ' + restaurant.name)
      router.push({name: 'restaurant', params: {id: restaurant.id}})
    },
    removeCategory(index){
      this.categoriesSelected.splice(index, 1)
      this.search()
    },
    search(){
      console.log('Searching for restaurants')
      RestaurantService.searchRestaurants({
        location: this.citySearch,
        categories: this.categoriesSelected
      })
      .then(response => {
        this.restaurants = response.data
      })
    }
  },
  mounted() {
    console.log('getting restaurants')
    RestaurantService.getEnabledRestaurants().then(response => {
      this.restaurants = response.data
    })
    CategoryService.getAllCategories()
    .then(response => {
      this.categories = response.data
    })
  },
}
</script>
