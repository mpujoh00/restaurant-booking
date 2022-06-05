<template>
    <v-layout class="layout">
        <v-col cols="6">
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="40" color="#ff99a8">mdi-silverware</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">{{ restaurant.name }}</h2>
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
                </v-card-text>
                <v-divider class="mx-4"></v-divider>
                <booking-dialog :restaurantId="restaurant.id"></booking-dialog>
            </v-card>
        </v-col>
    </v-layout>
</template>

<style>
.layout {
    align-content: center;
    justify-content: center;
    padding-top: 5%;
}
.card {
    padding: 4%;
}
.cardHeader {
    text-align: center;
    padding: 2%;
}
.cardTitle {
    padding: 1%;
    color: #ff99a8;
}
.form {
    padding-top: 2%;
}
.button {
    margin-left: 3%;
    margin-right: 3%;
}
.buttonText {
    padding-left: 8%;
    padding-right: 8%;
}
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
    methods: {
        book() {

        }
    },
    mounted() {
        RestaurantService.getRestaurant(this.id).then(response => {
            this.restaurant = response.data
        })
    },
})
</script>
