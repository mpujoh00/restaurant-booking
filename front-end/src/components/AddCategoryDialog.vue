<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="button" color="#ff99a8" v-bind="attrs" v-on="on">
                Add category
            </v-btn>
        </template>
        <v-card class="card">
            <div class="cardHeader">
                <v-avatar size="50" color="#ffe6e9">
                    <v-icon size="35" color="#ff99a8" class="ml-1 mt-1">mdi-tag-text-outline</v-icon>
                </v-avatar>
                <h2 class="cardTitle">New category</h2>
            </div>
            <v-form class="form" ref="form">
                <v-select 
                    v-model="category" 
                    :items="categories" 
                    label="Category" 
                    item-text="name" 
                    :rules="categoryRules" 
                    required
                    return-object 
                    color="grey"></v-select>
                <v-btn color="#ff99a8" class="button" @click="addCategory">
                    <span class="buttonText">Save</span>    
                </v-btn>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
import CategoryService from '@/services/CategoryService'
import { mapState, mapActions } from 'vuex'
import RestaurantService from '@/services/RestaurantService'

require('@/assets/main.css')

export default {
    fullname: 'AddCategoryDialog',
    computed: {
        ...mapState([
            'currentRestaurant',
        ])
    },
    data() {
        return {
            dialog: false,
            categories: [],
            category: null,
            categoryRules: [
                v => !!v || 'You must select a category'
            ]
        }
    },
    methods: {
        ...mapActions([
            'modifyRestaurant'
        ]),
        addCategory(){
            if(this.$refs.form.validate()){
                console.log('Adding category')
                RestaurantService.addCategory(this.currentRestaurant.id, this.category.name)
                .then(response => {
                    console.log('Category added')
                    this.modifyRestaurant(response.data)
                    this.dialog = false
                })
                .catch(() => {
                    console.log('Couldn\'t add category')
                    this.dialog = false
                })
            }
        }
    },
    mounted() {
        CategoryService.getAllCategories()
        .then(response => {
            this.categories = response.data.filter(cat => !this.currentRestaurant.categories.map(c => c.name).includes(cat.name))
        })
    },
}

</script>