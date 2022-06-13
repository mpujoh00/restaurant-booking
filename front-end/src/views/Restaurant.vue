<template>
    <v-layout class="layout">
        <v-col cols="6">
            <v-card class="card">
                <div class="cardHeader">
                    <v-img 
                        :src="'data:image/jpg;base64,' + restaurant.logo.data" 
                        alt="logo"
                        max-height="300"
                    ></v-img>
                    <h2 class="pa-1 mt-2" style="margin-bottom: -3%;">{{ restaurant.name }}</h2>
                </div>
                <v-card-text>
                    <div class="location">
                        {{ restaurant.location }}, Spain
                    </div>
                    <div class="extras">
                        €€ • Restaurant, Coffee
                    </div>
                    <div class="description">
                        Lovely restaurant in the city center
                    </div>
                    <div class="categories">
                        <v-chip v-for="(category, index) in restaurant.categories" 
                            :key="index"
                            color="#ffe6e9"
                            class="mr-2 mb-3">
                            {{ category.name }}
                        </v-chip>
                    </div>
                </v-card-text>
                <v-divider class="mx-4"></v-divider>
                <booking-dialog :restaurantId="restaurant.id"></booking-dialog>
            </v-card>
        </v-col>
    </v-layout>
</template>

<style scoped>
.location {
    font-size: 120%;
    font-weight: bold;
    text-align: left;
    margin: 1%;
}
.extras {    
    text-align: left;
    font-style: italic;
    color: #979797;
    margin: 1%;
}
.description {    
    text-align: left;
    text-justify: distribute;
    margin: 1%;
    margin-bottom: 3%;
}
</style>

<script>
import RestaurantService from '@/services/RestaurantService'

export default ({
    name: 'Restaurant',
    components: {
        BookingDialog: () => import("@/components/BookingDialog")
    },
    props: [
        'id'
    ],
    data() {
        return {
            restaurant: null,
        }
    },
    mounted() {
        RestaurantService.getRestaurant(this.id).then(response => {
            this.restaurant = response.data
        })
    },
})
</script>
