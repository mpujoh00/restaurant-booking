<template>
  <div class="home">
    <v-layout class="layout">
        <v-col cols="6">
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="40" color="#ff99a8">mdi-silverware</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">Restaurant</h2>
                </div>
                <v-form class="form">
                    <v-text-field 
                        v-model="currentRestaurant.name" 
                        label="Name" 
                        required
                        :readonly="nonEditable"
                        color="grey"/>
                    <v-text-field 
                        v-model="currentRestaurant.location" 
                        label="City" 
                        required
                        :readonly="nonEditable"
                        color="grey"/>
                    <div v-if="nonEditable">
                        <v-btn @click="editRestaurant" class="button" color="#ff99a8">
                            <span class="buttonText">Edit</span>    
                        </v-btn>
                    </div>  
                    <div v-if="!nonEditable">
                        <v-btn @click="saveRestaurant" class="button" color="#ff99a8">
                            <span class="buttonText">Save</span>    
                        </v-btn>
                        <v-btn @click="cancel" class="button" color="#ff99a8">
                            <span class="buttonText">Cancel</span>    
                        </v-btn>
                    </div>
                </v-form>
            </v-card>
        </v-col>
    </v-layout>

  </div>
</template>

<style scoped>
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
</style>

<script>
import { mapState, mapActions } from 'vuex'
import RestaurantService from '@/services/RestaurantService'

export default {
    computed: {
        ...mapState([
            'currentRestaurant',
        ])
    },
    data() {
        return {
            nonEditable: true,
        }
    },
    methods: {
        ...mapActions([
            'modifyRestaurant'
        ]),
        editRestaurant(){
            this.nonEditable = !this.nonEditable
        },
        saveRestaurant(){
            console.log('Saving restaurant...')
            RestaurantService.updateRestaurant({
                restaurantId: this.currentRestaurant.id,
                name: this.currentRestaurant.name,
                location: this.currentRestaurant.location
            })
            .then(response => {
                this.modifyRestaurant(response.data)
                this.nonEditable = !this.nonEditable
            })
        },
        cancel(){

        }
    },
}
</script>