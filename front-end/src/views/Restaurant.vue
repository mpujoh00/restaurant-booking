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
                        {{ restaurant.location }}
                    </div>
                    <div class="address">
                        {{ restaurant.address }}
                    </div>
                    <div class="categories mt-5">
                        <v-chip v-for="(category, index) in restaurant.categories" 
                            :key="index"
                            color="#ffe6e9"
                            class="mr-2 mb-3">
                            {{ category.name }}
                        </v-chip>
                    </div>
                    <v-tabs
                        color="#ff99a8">
                        <v-tab v-if="restaurant.description">
                            <v-icon left>mdi-sticker-text-outline</v-icon>
                            Description
                        </v-tab>
                        <v-tab>
                            <v-icon left>mdi-clock-outline</v-icon>
                            Schedule
                        </v-tab>
                        <v-tab v-if="restaurant.menu">
                            <v-icon left>mdi-food-outline</v-icon>
                            Menu
                        </v-tab>
                        <v-tab-item v-if="restaurant.description">
                            <v-card flat>
                                <v-card-text>
                                    <v-row
                                        align="center"
                                        class="mt-1 ml-1">
                                        {{ restaurant.description }}
                                    </v-row>
                                </v-card-text>
                            </v-card>
                        </v-tab-item>
                        <v-tab-item>
                            <v-card flat>
                                <v-card-text>
                                    <v-row
                                        align="center"
                                        class="mt-1 ml-1">
                                        M - S : {{ restaurant.openingTime }} - {{ restaurant.closingTime }}
                                    </v-row>
                                </v-card-text>
                            </v-card>
                        </v-tab-item>
                        <v-tab-item v-if="restaurant.menu">
                            <v-card flat>
                                <v-card-text>
                                    <v-row
                                        align="center"
                                        class="mt-1 ml-1">
                                        {{ restaurant.menu }}
                                    </v-row>
                                </v-card-text>
                            </v-card>
                        </v-tab-item>
                    </v-tabs>
                </v-card-text>
                <v-divider class="mx-4 mb-4"></v-divider>
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
.address {
    font-style: italic;
    text-align: left;
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
