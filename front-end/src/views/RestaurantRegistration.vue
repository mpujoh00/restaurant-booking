<template>
    <v-layout class="layout" align-center>
        <v-col cols="6">
            <br/>
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="40" color="#ff99a8">mdi-silverware</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">Restaurant information</h2>
                </div>
                <v-form class="form" @submit.prevent="registrationSubmit"> 
                    <v-text-field 
                        v-model="name" 
                        label="Name" 
                        required
                        color="grey"/>
                    <v-text-field 
                        v-model="city" 
                        label="City" 
                        required
                        color="grey"/>
                    <v-menu
                        ref="openTimeMenuRef"
                        v-model="openTimeMenu"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        :return-value.sync="openTime"
                        transition="scale-transition"
                        offset-y
                        max-width="290px"
                        min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                v-model="openTime"
                                label="Opening time"
                                prepend-inner-icon="mdi-clock-time-four-outline"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                                color="grey"
                            ></v-text-field>
                        </template>
                        <v-time-picker
                            v-if="openTimeMenu"
                            v-model="openTime"
                            @click:minute="$refs.openTimeMenuRef.save(openTime)"
                            color="#ff99a8"
                        ></v-time-picker>
                    </v-menu>
                    <v-menu
                        ref="closeTimeMenuRef"
                        v-model="closeTimeMenu"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        :return-value.sync="closeTime"
                        transition="scale-transition"
                        offset-y
                        max-width="290px"
                        min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                v-model="closeTime"
                                label="Closing time"
                                prepend-inner-icon="mdi-clock-time-four-outline"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                                color="grey"
                            ></v-text-field>
                        </template>
                        <v-time-picker
                            v-if="closeTimeMenu"
                            v-model="closeTime"
                            @click:minute="$refs.closeTimeMenuRef.save(closeTime)"
                            color="#ff99a8"
                        ></v-time-picker>
                    </v-menu>
                    <v-select 
                        v-model="intervalMinutes"
                        label="Time between reservations"
                        :items="intervalMinutesOptions"
                        item-text="label"
                        item-value="value"
                        class="mb-5"
                        color="grey"/>
                    
                    <v-btn type="submit" color="#ff99a8" class="button">
                        <span class="buttonText">Register</span>    
                    </v-btn>
                    <v-btn @click="cancel" color="#ff99a8" class="button">
                        <span class="buttonText">Cancel</span>    
                    </v-btn>
                </v-form>
            </v-card>
        </v-col>
        
    </v-layout>
</template>

<style scoped>
.layout {
    height: 80vh;
}
</style>

<script>
import { mapState, mapActions } from 'vuex'
import router from '@/router'

require('@/assets/main.css')

export default ({
    name: 'RestaurantRegistration',
    data() {
        return {
            name: '',
            city: '',
            openTime: null,
            openTimeMenu: false,
            closeTime: null,
            closeTimeMenu: false,
            intervalMinutes: null,
            intervalMinutesOptions: [
                {label: '15 minutes', value: '15'},
                {label: '30 minutes', value: '30'},
                {label: '45 minutes', value: '45'},
                {label: '60 minutes', value: '60'},
            ],
            nameRules: [
                v => !!v || 'Name is required',
            ],
            cityRules: [
                v => !!v || 'City is required',
            ],
            openTimeRules: [
                v => !!v || 'Opening time is required',
            ],
            closeTimeRules: [
                v => !!v || 'Closing time is required',
            ],
            intervalMinutesRules: [
                v => !!v || 'Time between reservations is required',
            ],
        }
    },
    computed: {
        ...mapState([
            'tempUser'
        ])
    },
    methods: {
        ...mapActions([
            'registerUser',
            'removeTempUser',
            'registerRestaurant'
        ]),
        registrationSubmit(){
            console.log('registering restaurant...')
            const data = {
                user: this.tempUser,
                restaurant: {
                    name: this.name,
                    location: this.city,
                    restaurantAdminEmail: this.tempUser.email,
                    openTime: this.openTime,
                    closeTime: this.closeTime,
                    intervalMinutes: this.intervalMinutes}
            }
            console.log(data)
            this.registerRestaurant( data)
            // todo fix prevent submit
        },
        cancel(){
            console.log('canceling restaurant registration...')
            this.removeTempUser()
            this.reset()
            router.push('/login')
        },
        reset(){
            this.name = ''
            this.city = ''
            this.openTime = null
            this.closeTime = null
            this.intervalMinutes = null
        }
    },
})
</script>
