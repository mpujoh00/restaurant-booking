<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on: dialog, attrs }">
            <v-tooltip bottom>
                <template v-slot:activator="{ on: tooltip }">
                    <v-icon
                        small
                        class="ml-1"
                        @click="rateBooking(item.id)"
                        color="#9f71ad"
                        v-bind="attrs"
                        v-on="{...tooltip, ...dialog}"
                    >
                        mdi-star
                    </v-icon>
                </template>
                <span>Rate experience</span>
            </v-tooltip>  
        </template>
        <v-card class="card">
            <div class="cardHeader">
                <v-avatar size="50" color="#ffe6e9">
                    <v-icon size="40" color="#ff99a8">mdi-star-circle</v-icon>
                </v-avatar>
                <h2 class="cardTitle">Rate your experience</h2>
            </div>
            <v-form class="form">
                <v-rating
                    background-color="#9F71AD"
                    color="#63458a"
                    empty-icon="mdi-star-outline"
                    full-icon="mdi-star"
                    half-icon="mdi-star-half-full"
                    half-increments
                    hover
                    length="5"
                    size="40"
                    v-model="rating"
                    @input="showRatingRule = false"
                    class="mt-3"
                ></v-rating>
                <h4 class="rule" v-if="showRatingRule">You must select a rating</h4>
                <v-textarea
                    name="input-7-1"
                    label="Comment"
                    auto-grow
                    rows="4"
                    v-model="comment"
                    color="grey"
                    outlined
                    clearable
                    shaped
                    class="mx-6 mt-5"
                ></v-textarea>
                <v-btn color="#ff99a8" class="button mt-2" @click="addRating">
                    <span class="buttonText">Rate</span>    
                </v-btn>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<style scoped>
.rule {
    color: red;
    font-weight: 200;
}
</style>

<script>
import RatingService from '@/services/RatingService'
import { mapState } from 'vuex'

require('@/assets/main.css')

export default {
    fullname: 'RateBookingDialog',
    props: [
        'reservation'
    ],
    computed: {
        ...mapState([
            'currentUser',
        ])
    },
    data() {
        return {
            dialog: false,
            rating: null,
            showRatingRule: false,
            comment: ''
        }
    },
    methods: {
        addRating(){
            if(this.rating !== null){
                console.log('Adding rating')
                RatingService.createRating({
                    value: this.rating,
                    comment:  this.comment,
                    reservationId: this.reservation.id,
                    restaurantId: this.reservation.restaurantId,
                    userId: this.reservation.userId,
                    userName: this.currentUser.fullname
                })
                .then(() => {
                    console.log('Rating submitted')
                    this.$router.go()
                    this.dialog = false
                })
                .catch(() => {
                    console.log('Couldn\'t submit rating')
                    this.dialog = false
                })
            }
            else {
                this.showRatingRule = true
            }
        }
    },
    mounted() {
    },
}

</script>