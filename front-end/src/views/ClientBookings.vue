<template>
    <div class="container">
        <v-data-table :headers="headers" :items="bookings" sort-by="restaurantName" class="elevation-1">
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Your Bookings</v-toolbar-title>
                    <v-divider
                        class="mx-4"
                        inset
                        vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                    <div class="selector">
                        <v-select v-model="reservTypeSelected" :items="reservationType" @change="updateBookings" color="grey" background-color="#ffe6e9" rounded></v-select>
                    </div>
                    
                </v-toolbar>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-icon
                    v-if="item.status !== 'CANCELED'"
                    small
                    class="ml-1"
                    @click="cancelBooking(item.id)"
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
    max-width: 130px;
    max-height: 50px;
}
</style>

<script>
import { mapState } from 'vuex'
import BookingService from '@/services/BookingService'

export default {
    name: 'RestaurantBookings',
    computed: {
        ...mapState([
            'currentUser',
        ])
    },
    data() {
        return {
            bookings: [],
            reservationType: [
                'Future',
                'Past'
            ],
            reservTypeSelected: 'Future',
            headers: [
                {
                    text: 'Restaurant',
                    align: 'start',
                    value: 'restaurantName'
                },
                {
                    text: 'Date',
                    value: 'reservationSlot.date',
                },
                {
                    text: 'Time',
                    value: 'reservationSlot.time',
                },
                {
                    text: 'Number of people',
                    value: 'numPeople',
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
        cancelBooking(reservationId){
            console.log('Canceling reservation' + reservationId)
            BookingService.cancelReservation(reservationId)
            .then(() => {
                console.log('Status correctly changed')
                this.$router.go()
            })
            .catch(() => {
                console.log('Status couldn\'t be changed')
            })
        },
        updateBookings(){
            if(this.reservTypeSelected === 'Future'){
                console.log('getting future bookings')
                BookingService.getActiveUserReservations(this.currentUser.id)
                .then(response => {
                    this.bookings = response.data
                })
            }
            else{                
                console.log('getting past bookings')
                BookingService.getInactiveUserReservations(this.currentUser.id)
                .then(response => {
                    this.bookings = response.data
                })
            }
        }
    },
    mounted() {
        console.log('getting bookings')
        this.updateBookings()
    },
}
</script>