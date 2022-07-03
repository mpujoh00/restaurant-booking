<template>
    <div class="container">
        <v-data-table :headers="headers" :items="categories" sort-by="name" class="elevation-1">
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Categories</v-toolbar-title>
                    <v-divider
                        class="mx-4"
                        inset
                        vertical
                    ></v-divider> 
                    <v-spacer></v-spacer>
                    <div class="tableIconButton">
                        <new-category-dialog></new-category-dialog>
                    </div>               
                </v-toolbar>
            </template>
        </v-data-table>
    </div>
</template>

<script>
import CategoryService from '@/services/CategoryService'

require('@/assets/main.css')

export default {
    name: 'RestaurantBookings',
    components: {
        NewCategoryDialog: () => import("@/components/NewCategoryDialog")
    },
    data() {
        return {
            categories: [],
            headers: [
                {
                    text: 'Name',
                    align: 'start',
                    value: 'name'
                },
            ],
        }
    },
    methods: {
        updateCategories(){
            console.log('Getting categories')
            CategoryService.getAllCategories()
            .then(response => {
                console.log('Categories updated')
                this.categories = response.data
            })
            .catch(() => {
                console.log('Couldn\'t get categories')
            })
        }
    },
    mounted() {
        console.log('getting categories')
        this.updateCategories()
    },
}
</script>