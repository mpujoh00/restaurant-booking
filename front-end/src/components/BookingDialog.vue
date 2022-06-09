<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="button mt-5" color="#ff99a8" v-bind="attrs" v-on="on">
                <span class="buttonText">Book</span>
            </v-btn>
        </template>
        <v-card class="card">
            <div class="cardHeader">
                <v-avatar size="50" color="#ffe6e9">
                    <v-icon size="35" color="#ff99a8">mdi-table-chair</v-icon>
                </v-avatar>
                <h2 class="cardTitle">Booking</h2>
            </div>
            <v-form class="form" ref="form">
                <v-menu
                    v-model="menu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    min-width="auto"
                >
                    <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        v-model="date"
                        label="Date"
                        prepend-inner-icon="mdi-calendar"
                        readonly
                        :rules="dateRules"
                        v-bind="attrs"
                        v-on="on"
                        color="grey"
                        required
                    ></v-text-field>
                    </template>
                    <v-date-picker
                        v-model="date"
                        @input="menu = false"
                        @change="updateAvailableHours"
                        color="#ff99a8"
                    ></v-date-picker>
                </v-menu>
                <v-select
                    v-model="numPeople"
                    label="Number of people"
                    :items="people"
                    :rules="peopleRules"
                    prepend-inner-icon="mdi-account-group"
                    color="grey"
                    @change="updateAvailableHours"
                    required>
                </v-select>
                <v-select
                    v-model="time"
                    label="Time"
                    :items="availableHours"
                    :rules="timeRules"
                    prepend-inner-icon="mdi-clock-time-four-outline"
                    no-data-text="No slots available"
                    color="grey"
                    :disabled="!date || !numPeople"
                    required>
                </v-select>
                <v-btn class="button" color="#ff99a8" @click="book">
                    <span class="buttonText">Book</span>
                </v-btn>
                <v-snackbar v-model="error">{{ error }}</v-snackbar>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
import BookingService from '@/services/BookingService'
import { mapState } from 'vuex'

require('@/assets/main.css')

export default {
    name: 'BookingDialog',
    props: {
        restaurantId: String
    },
    computed: {
        ...mapState([
            'currentUser'
        ])
    },
    data() {
        return {
            dialog: false,
            date: '',
            numPeople: null,
            people: [1, 2, 3, 4, 5, 6, 7, 8],
            time: '',
            availableHours: [],
            dateRules: [
                v => !!v || 'Date is required'
            ],
            peopleRules: [
                v => !!v || 'Number of people is required'
            ],
            timeRules: [
                v => !!v || 'Time is required'
            ],
            error: ''
        }
    },
    methods: {
        updateAvailableHours() {
            console.log('Updating available hours for the ' + this.date)
            if(this.date && this.numPeople){
                BookingService.getRestaurantsSlotsForPeopleDate(this.restaurantId, this.date, this.numPeople)
                .then(response => {
                    console.log('Correct')
                    this.availableHours = response.data
                })
                .catch(() => {
                    console.log('error')
                })
            }            
        },
        book(){
            if(this.$refs.form.validate()){
                console.log('Booking a table...')
                BookingService.createReservation({
                    userId: this.currentUser.id,
                    restaurantId: this.restaurantId,
                    numPeople: this.numPeople,
                    date: this.date,
                    time: this.time
                })
                .then(response => {
                    console.log(response.data)
                    this.dialog = false
                })
                .catch(error => {
                    console.log(error)
                    this.error = 'Couldn\'t book'
                })
            }
        }
    },
}
</script>