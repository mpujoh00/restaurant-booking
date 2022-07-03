<template>
  <div class="home">
    <v-layout class="layout">
        <v-col cols="6">
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="150" color="#ffe6e9" class="mt-4">
                        <v-img 
                            :src="'data:image/jpg;base64,' + currentRestaurant.logo" 
                            alt="logo"
                            max-height="300"
                        ></v-img>
                    </v-avatar>
                    <h2 class="cardTitle">Restaurant</h2>
                </div>
                <v-form class="form" ref="form">
                    <v-text-field 
                        v-model="name" 
                        label="Name" 
                        required
                        :readonly="nonEditable"
                        :rules="nameRules"
                        prepend-inner-icon="mdi-silverware"
                        color="grey"/>
                    <v-text-field 
                        v-model="location" 
                        label="City" 
                        required
                        :readonly="nonEditable"
                        :rules="locationRules"
                        prepend-inner-icon="mdi-city-variant"
                        color="grey"/>
                    <v-text-field 
                        v-model="address" 
                        label="Address" 
                        required
                        :readonly="nonEditable"
                        :rules="addressRules"
                        prepend-inner-icon="mdi-map-marker"
                        color="grey"/>
                    <v-textarea
                        v-if="currentRestaurant.description || !nonEditable"
                        :readonly="nonEditable"
                        name="input-7-1"
                        label="Description"
                        auto-grow
                        rows="1"
                        v-model="description"
                        prepend-inner-icon="mdi-sticker-text-outline"
                        color="grey"
                        background-color="white"
                    ></v-textarea>
                    <v-textarea
                        v-if="currentRestaurant.menu || !nonEditable"
                        :readonly="nonEditable"
                        name="input-7-1"
                        label="Menu"
                        auto-grow
                        rows="1"
                        v-model="menu"
                        prepend-inner-icon="mdi-food"
                        color="grey"
                        background-color="white"
                    ></v-textarea>
                    <div class="categories" v-if="nonEditable">
                        <v-chip v-for="(category, index) in currentRestaurant.categories" 
                            :key="index"
                            close
                            @click:close="removeCategory(category)"
                            return-object
                            color="#ffe6e9"
                            class="mr-2 mb-5">
                            {{ category.name }}
                        </v-chip>
                    </div>
                    <div v-if="nonEditable">
                        <v-btn @click="editRestaurant" class="button" color="#ff99a8">
                            <span class="buttonText">Edit</span>    
                        </v-btn>
                        <add-category-dialog></add-category-dialog>
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
    <v-snackbar v-model="error">{{ error }}</v-snackbar>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import RestaurantService from '@/services/RestaurantService'

require('@/assets/main.css')

export default {
    components: { 
        AddCategoryDialog: () => import("@/components/AddCategoryDialog.vue")
    },
    computed: {
        ...mapState([
            'currentRestaurant',
            'error'
        ])
    },
    data() {
        return {
            nonEditable: true,
            name: '',
            location: '',
            address: '',
            description: '',
            menu: '',
            nameRules: [
                v => !!v || 'Name is required'
            ],
            locationRules: [
                v => !!v || 'Location is required'
            ],
            addressRules: [
                v => !!v || 'Address is required'
            ],
        }
    },
    methods: {
        ...mapActions([
            'modifyRestaurant',
            'changeErrorMessage'
        ]),
        editRestaurant(){
            this.nonEditable = !this.nonEditable
        },
        saveRestaurant(){            
            if(this.$refs.form.validate()){
                console.log('Saving restaurant...')
                RestaurantService.updateRestaurant({
                    restaurantId: this.currentRestaurant.id,
                    name: this.name,
                    location: this.location,
                    address: this.address,
                    description: this.description != '' ? this.description : null,
                    menu: this.menu != '' ? this.menu : null,
                })
                .then(response => {
                    this.modifyRestaurant(response.data)
                })
                .catch(() => {
                    this.reset()
                    this.changeErrorMessage('Couldn\'t save restaurant')
                })
                this.nonEditable = !this.nonEditable
            }
        },
        removeCategory(category){
            console.log('Removing category')
            RestaurantService.removeCategory(this.currentRestaurant.id, category.name)
            .then(response => {
                console.log('Category removed')
                this.modifyRestaurant(response.data)
            })
            .catch(() => 
                this.changeErrorMessage('Couldn\'t remove category')
            )
        },
        reset(){
            this.name = this.currentRestaurant.name
            this.location = this.currentRestaurant.location
            this.address = this.currentRestaurant.address
            if(this.currentRestaurant.description != null){
                this.description = this.currentRestaurant.description
            }
            else{
                this.description = ''
            }
            if(this.currentRestaurant.menu != null){
                this.menu = this.currentRestaurant.menu
            }
            else{
                this.menu = ''
            }
        },
        cancel(){
            console.log('Canceling')
            this.reset()
            this.nonEditable = !this.nonEditable
        }
    },
    mounted() {
        this.reset()
        if(this.currentRestaurant.logo == null){
            RestaurantService.getRestaurant(this.currentRestaurant.id)
            .then(response => {
                console.log('Restaurante actualizado')
                this.modifyRestaurant(response.data)
            })
        }
    },
}
</script>