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
                    <div>
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
        ])
    },
    data() {
        return {
            nonEditable: true,
            name: '',
            location: ''
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
                name: this.name,
                location: this.location
            })
            .then(response => {
                this.modifyRestaurant(response.data)
            })
            .catch(() => {
                this.reset()
            })
            this.nonEditable = !this.nonEditable
        },
        removeCategory(category){
            console.log('Removing category')
            RestaurantService.removeCategory(this.currentRestaurant.id, category.name)
            .then(response => {
                console.log('Category removed')
                this.modifyRestaurant(response.data)
            })
        },
        reset(){
            this.name = this.currentRestaurant.name,
            this.location = this.currentRestaurant.location
        },
        cancel(){
            console.log('Canceling')
            this.reset()
        }
    },
}
</script>