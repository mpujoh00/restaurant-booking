<template>
    <v-layout class="layout">
        <v-col cols="6">
            <v-card class="card">
                <div class="cardHeader">
                    <v-img 
                        :src="'data:image/jpg;base64,' + restaurant.logo" 
                        alt="logo"
                        max-height="300"
                    ></v-img>
                    <h1 class="pa-1 mt-2" style="margin-bottom: -3%;">{{ restaurant.name }}</h1>
                </div>
                <v-card-text>
                    <div class="rating">
                        <v-rating
                            color="#9F71AD"
                            background-color="#9F71AD"
                            empty-icon="mdi-star-outline"
                            full-icon="mdi-star"
                            half-icon="mdi-star-half-full"
                            half-increments
                            length="5"
                            readonly
                            size="2.4vh"
                            :value="restaurant.averageRating"
                        ></v-rating>
                        <h5 class="numRatings">({{ restaurant.numRatings }}) review/s</h5>
                    </div>
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
                    <v-tabs color="#ff99a8">
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
                        <v-tab>
                            <v-icon left>mdi-comment-text-outline</v-icon>
                            Reviews
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
                        <v-tab-item>
                            <v-card flat>
                                <v-card-text>
                                    <div v-for="(rating, index) in ratings" :key="index">
                                        <v-row class="mt-3 ml-3">
                                            <v-col>
                                                <v-row>
                                                    <h4>{{ rating.userName}}</h4>
                                                </v-row>
                                                <v-row>
                                                    <h5 v-text="getDate(rating.date)" class="date"></h5>
                                                </v-row>
                                            </v-col>
                                            <v-spacer></v-spacer>
                                            <v-col>                                                
                                                <v-rating
                                                    background-color="#9F71AD"
                                                    color="#9F71AD"
                                                    empty-icon="mdi-star-outline"
                                                    full-icon="mdi-star"
                                                    half-icon="mdi-star-half-full"
                                                    half-increments
                                                    length="5"
                                                    readonly
                                                    size="2vh"
                                                    :value="rating.value"
                                                ></v-rating>
                                            </v-col>
                                        </v-row>
                                        <v-row>
                                            <v-col class="ml-3 mr-5">
                                                <p style="text-align:justify">{{ rating.comment }}</p>
                                            </v-col>
                                        </v-row>
                                        <v-divider v-if="index !== ratings.length-1"></v-divider>
                                    </div>
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
.rating {
    display: flex;
    font-size: 130%;
    font-weight: bold;
    text-align: left;
}
.numRatings {
    margin-left: 2%;
    margin-top: 0.9%;
}
.date {
    font-weight: 400;
}
</style>

<script>
import RestaurantService from '@/services/RestaurantService'
import RatingService from '@/services/RatingService'

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
            ratings: []
        }
    },
    methods: {
        getDate(date){
            const options = {year: 'numeric', month: 'long', day: 'numeric'}
            return new Date(date).toLocaleDateString('en-US', options)
        }
    },
    mounted() {
        RestaurantService.getRestaurant(this.id)
        .then(response => {
            this.restaurant = response.data
        })
        RatingService.getRestaurantRatings(this.id)
        .then(response => {
            this.ratings = response.data
        })
    },
})
</script>
