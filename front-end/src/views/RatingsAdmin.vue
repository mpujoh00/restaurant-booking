<template>
    <div class="container">
        <v-data-table :headers="headers" :items="ratings" sort-by="date" class="elevation-1">
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Offensive ratings</v-toolbar-title>
                    <v-divider
                        class="mx-4"
                        inset
                        vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                </v-toolbar>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-icon
                            small
                            class="ml-1"
                            @click="acceptRating(item.id)"
                            color="#65ad63"
                            v-bind="attrs"
                            v-on="on"
                        >
                            mdi-check-bold
                        </v-icon>
                    </template>
                    <span>Accept</span>
                </v-tooltip> 
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-icon
                            small
                            class="ml-1"
                            @click="cancelRating(item.id)"
                            color="#ff365a"
                            v-bind="attrs"
                            v-on="on"
                        >
                            mdi-close-thick
                        </v-icon>
                    </template>
                    <span>Cancel</span>
                </v-tooltip>
            </template>
        </v-data-table>
        <v-snackbar v-model="error">{{ error }}</v-snackbar>
    </div>
</template>

<script>
import RatingService from '@/services/RatingService'
import { mapState, mapActions } from 'vuex'

require('@/assets/main.css')

export default {
    name: 'RatingsAdmin',
    computed: {
        ...mapState([
            'error'
        ])
    },
    data() {
        return {
            ratings: [],
            headers: [
                {
                    text: 'Comment',
                    align: 'start',
                    value: 'comment',
                },     
                {
                    text: 'Client',
                    value: 'userName',
                },           
                {
                    text: 'Date',
                    value: 'date',
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
        acceptRating(ratingId){
            console.log('Accepting rating' + ratingId)
            this.changeRatingStatus(ratingId, 'OK')
        },
        cancelRating(ratingId){
            console.log('Canceling rating' + ratingId)
            this.changeRatingStatus(ratingId, 'CANCELLED')
        },
        changeRatingStatus(ratingId, status){
            RatingService.changeRatingStatus(ratingId, status)
            .then(() => {
                console.log('Status correctly changed')
                this.$router.go()
            })
            .catch(() => {
                console.log('Status couldn\'t be changed')
                this.changeErrorMessage('Couldn\'t change rating status')
            })
        }
    },
    mounted() {
        console.log('Getting flagged ratings')
        RatingService.getFlaggedRatings()
        .then(response => {
            this.ratings = response.data
        })
    },
}
</script>