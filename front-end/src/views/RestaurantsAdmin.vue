<template>
    <div class="container">
        <v-data-table :headers="headers" :items="restaurants" sort-by="date" class="elevation-1">
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Restaurants Administration</v-toolbar-title>
                    <v-divider
                        class="mx-4"
                        inset
                        vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                    <div class="selector">
                        <v-select v-model="restaurantsStatus" :items="restaurantsStatuses" @change="updateRestaurants" color="grey" background-color="#ffe6e9" rounded></v-select>
                    </div>                    
                </v-toolbar>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-icon
                    v-if="item.status === 'PENDING' || item.status === 'DISABLED'"
                    small
                    class="ml-1"
                    @click="changeRestaurantStatus(item.id)"
                    color="#65ad63"
                >
                    mdi-check-bold
                </v-icon>
                <v-icon
                    v-if="item.status === 'ENABLED'"
                    small
                    class="ml-1"
                    @click="changeRestaurantStatus(item.id)"
                    color="#ff365a"
                >
                    mdi-close-thick
                </v-icon>
            </template>
        </v-data-table>
    </div>
</template>

<style>
.container {
    margin-top: 2%;
    margin-left: 19%;
    margin-right: 19%;
    width: auto;
}
.selector {
    margin-top: 2%;
    max-width: 150px;
    max-height: 50px;
}
</style>

<script>
import { mapState } from 'vuex'
import RestaurantService from '@/services/RestaurantService'

export default {
    name: 'RestaurantBookings',
    computed: {
        ...mapState([
            'currentUser',
        ])
    },
    data() {
        return {
            restaurants: [],
            restaurantsStatuses: [
                'All',
                'Pending',
                'Disabled'
            ],
            restaurantsStatus: 'All',
            headers: [
                {
                    text: 'Name',
                    align: 'start',
                    value: 'name'
                },
                {
                    text: 'Location',
                    value: 'location',
                },
                {
                    text: 'Administrator',
                    value: 'restaurantAdminEmail',
                },
                {
                    text: 'Status',
                    value: 'status',
                },
                {
                    text: 'Actions',
                    value: 'actions',
                    sortable: false
                }
            ],
        }
    },
    methods: {
        changeRestaurantStatus(restaurantId){
            console.log('Changing restaurant\'s status')
            RestaurantService.updateRestaurantStatus(restaurantId)
            .then(() => {
              console.log('Correctly changed')
              this.$router.go()
            })
            .catch(() => {
              console.log('Couldn\'t change the status')
            })
        },
        updateRestaurants(){
            if(this.restaurantsStatus === 'All'){
                console.log('Getting all restaurants')
                RestaurantService.getAllRestaurants()
                .then(response => {
                    this.restaurants = response.data
                })
            }
            else if(this.restaurantsStatus === 'Disabled'){
                console.log('Getting disabled restaurants')
                RestaurantService.getDisabledRestaurants()
                .then(response => {
                    this.restaurants = response.data
                })
            }
            else{                
                console.log('Getting pending restaurants')
                RestaurantService.getPendingRestaurants()
                .then(response => {
                    this.restaurants = response.data
                })
            }
        }
    },
    mounted() {
        this.updateRestaurants()
    },
}
</script>