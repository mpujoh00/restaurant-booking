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
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-icon
                            v-if="item.status !== 'CANCELED' && reservTypeSelected !== 'Past'"
                            small
                            class="ml-1"
                            @click="cancelBooking(item.id)"
                            color="#ff365a"
                            v-bind="attrs"
                            v-on="on"
                        >
                            mdi-close-thick
                        </v-icon>
                    </template>
                    <span>Cancel</span>
                </v-tooltip>  
                <RateBookingDialog v-if="reservTypeSelected === 'Past' && item.status === 'ATTENDED' && !item.rated" :reservation="item"/>
            </template>
        </v-data-table>
        <ConfirmationDialog ref="confirm"/>
        <v-snackbar v-model="error">{{ error }}</v-snackbar>
    </div>
</template>

<style scoped>
.selector {
    max-width: 8rem;
}
</style>

<script>
import { mapState, mapActions } from 'vuex'
import BookingService from '@/services/BookingService'

require('@/assets/main.css')

export default {
    name: 'ClientBookings',
    components: {
        ConfirmationDialog: () => import("@/components/ConfirmationDialog.vue"),
        RateBookingDialog: () => import("@/components/RateBookingDialog.vue")
    },
    computed: {
        ...mapState([
            'currentUser',
            'error'
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
        ...mapActions([
            'changeErrorMessage'
        ]),
        cancelBooking(reservationId){
            this.$refs.confirm.open("Confirm", "Are you sure you want to cancel your booking?")
            .then(() => {
                console.log('Canceling reservation' + reservationId)
                BookingService.cancelReservation(reservationId)
                .then(() => {
                    console.log('Status correctly changed')
                    this.$router.go()
                })
                .catch(() => {
                    console.log('Status couldn\'t be changed')
                    this.changeErrorMessage('Couldn\'t cancel booking')
                })
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