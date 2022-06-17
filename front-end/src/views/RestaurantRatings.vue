<template>
    <v-container class="ratings">
        <v-row justify="center" class="mt-5">
            <v-col class="mr-auto">
                <v-data-table :headers="headers" :items="ratings" sort-by="date" class="elevation-1">
                    <template v-slot:top>
                        <v-toolbar flat>
                            <v-toolbar-title>Your ratings</v-toolbar-title>
                            <v-divider
                                class="mx-4"
                                inset
                                vertical
                            ></v-divider>
                            <v-spacer></v-spacer>
                        </v-toolbar>
                    </template>
                </v-data-table>
            </v-col>
            <v-col md="3">
                <v-card class="average">
                    <v-card-title>Average rating</v-card-title>
                    <v-card-text>

                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<style scoped>
.ratings {
    width: auto;
    align-content: center;
    justify-content: center;
}
.average {
    margin-left: 40%;
}
</style>

<script>
import { mapState } from 'vuex'
import RatingService from '@/services/RatingService'

require('@/assets/main.css')

export default {
    name: 'RestaurantBookings',
    computed: {
        ...mapState([
            'currentRestaurant',
        ])
    },
    data() {
        return {
            ratings: [],
            headers: [
                {
                    text: 'Rating',
                    align: 'start',
                    value: 'value'
                },
                {
                    text: 'Comment',
                    value: 'comment',
                },                
                {
                    text: 'Date',
                    value: 'date',
                },
                {
                    text: 'Client',
                    value: 'userName',
                },
            ],
        }
    },
    mounted() {
        console.log('Getting ratings')
        RatingService.getRestaurantRatings(this.currentRestaurant.id)
        .then(response => {
            this.ratings = response.data
        })
    },
}
</script>