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
                            <v-rating
                                background-color="#9F71AD"
                                color="#9F71AD"
                                empty-icon="mdi-star-outline"
                                full-icon="mdi-star"
                                half-icon="mdi-star-half-full"
                                half-increments
                                length="5"
                                readonly
                                size="1.8vh"
                                :value="currentRestaurant.averageRating"
                            ></v-rating>
                            <h5 class="ml-2">{{ currentRestaurant.averageRating }}  ({{ currentRestaurant.numRatings }})</h5>
                        </v-toolbar>
                    </template>
                    <template v-slot:item.ratingStatus="{ item }">
                        <v-chip
                            :color="getColor(item.ratingStatus)"
                            dark>
                            {{ item.ratingStatus }}
                        </v-chip>
                    </template>
                    <template v-slot:item.actions="{ item }">
                        <v-tooltip bottom>
                            <template v-slot:activator="{ on, attrs }">
                                <v-icon
                                    v-if="item.ratingStatus === 'OK'"
                                    small
                                    class="ml-1"
                                    @click="flagRating(item.id)"
                                    color="#ff365a"
                                    v-bind="attrs"
                                    v-on="on"
                                >
                                    mdi-flag-variant-outline
                                </v-icon>
                            </template>
                            <span>Mark as offensive</span>
                        </v-tooltip> 
                    </template>
                </v-data-table>
            </v-col>
        </v-row>
        <v-snackbar v-model="error">{{ error }}</v-snackbar>
    </v-container>
</template>

<style scoped>
.ratings {
    align-content: center;
    justify-content: center;
}
.average {
    margin-left: 33% ;
    padding: 2%;
}
.row {
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>

<script>
import { mapState, mapActions } from 'vuex'
import RatingService from '@/services/RatingService'

require('@/assets/main.css')

export default {
    name: 'RestaurantRatings',
    computed: {
        ...mapState([
            'currentRestaurant',
            'error'
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
                {
                    text: 'Status',
                    value: 'ratingStatus',
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
        ...mapActions([
            'changeErrorMessage'
        ]),
        flagRating(ratingId){
            console.log('Marking rating as offensive')
            RatingService.flagRating(ratingId)
            .then(() => {
                console.log('Flag rated')
                this.$router.go()
            })
            .catch(() => {
                console.log('Couldn\'t flag rating')
                this.changeErrorMessage('Couldn\'t flag rating')
            })
        },
        getColor(ratingStatus){
            if(ratingStatus === 'OK')
                return '#7EB07E'
            else
                return '#907474'
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