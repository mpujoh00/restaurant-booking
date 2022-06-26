<template>
    <div class="container">
        <v-data-table :headers="headers" :items="bookings" sort-by="userName" class="elevation-1">
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Bookings</v-toolbar-title>
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
                            v-if="item.status === 'PENDING'"
                            small
                            class="ml-1"
                            @click="acceptBooking(item.id)"
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
                            v-if="item.status === 'PENDING'"
                            small
                            class="ml-1"
                            @click="rejectBooking(item.id)"
                            color="#ff365a"
                            v-bind="attrs"
                            v-on="on"
                        >
                            mdi-close-thick
                        </v-icon>
                    </template>
                    <span>Reject</span>
                </v-tooltip>
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-icon
                            v-if="canConfirmAttendance(item)"
                            small
                            class="ml-1"
                            @click="confirmAttendance(item.id)"
                            color="#5BBFCC"
                            v-bind="attrs"
                            v-on="on"
                        >
                            mdi-check-all
                        </v-icon>
                    </template>
                    <span>Confirm attendance</span>
                </v-tooltip>
            </template>
        </v-data-table>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import BookingService from '@/services/BookingService'

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
            bookings: [],
            headers: [
                {
                    text: 'Client',
                    align: 'start',
                    value: 'userName'
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
                    text: 'Table',
                    value: 'reservationSlot.table.number',
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
        acceptBooking(reservationId){
            console.log('Accepting reservation' + reservationId)
            this.changeReservation(reservationId, 'CONFIRMED')
        },
        rejectBooking(reservationId){
            console.log('Canceling reservation' + reservationId)
            this.changeReservation(reservationId, 'CANCELED')
        },
        changeReservation(reservationId, status){
            BookingService.changeReservationStatus(reservationId, status)
            .then(() => {
                console.log('Status correctly changed')
                this.$router.go()
            })
            .catch(() => {
                console.log('Status couldn\'t be changed')
            })
        },
        canConfirmAttendance(reservation){
            const reservationDate = new Date(reservation.reservationSlot.date + ' ' + reservation.reservationSlot.time)
            const today = new Date()
            return reservation.status === 'CONFIRMED' && reservationDate < today
        },
        confirmAttendance(reservationId){
            console.log('Confirming attendance to reservation ' + reservationId)
            this.changeReservation(reservationId, 'ATTENDED')
        }
    },
    mounted() {
        console.log('Getting bookings')
        BookingService.getAllRestaurantReservations(this.currentRestaurant.id)
        .then(response => {
            this.bookings = response.data
        })
    },
}
</script>