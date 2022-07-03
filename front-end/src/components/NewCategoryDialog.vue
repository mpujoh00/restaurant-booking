<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on: dialog, attrs }">
            <v-tooltip bottom>
                <template v-slot:activator="{ on: tooltip }">
                    <v-btn class="button mt-5" small icon color="#ff99a8" v-bind="attrs" v-on="{...tooltip, ...dialog}">
                        <v-icon>mdi-tag-plus</v-icon>
                    </v-btn>
                </template>
                <span>New category</span>
            </v-tooltip> 
        </template>
        <v-card class="card">
            <div class="cardHeader">
                <v-avatar size="50" color="#ffe6e9">
                    <v-icon size="35" color="#ff99a8" class="ml-1 mt-1">mdi-tag-text-outline</v-icon>
                </v-avatar>
                <h2 class="cardTitle">Category</h2>
            </div>
            <v-form class="form" ref="form">
                <v-text-field 
                    v-model="name" 
                    label="Name" 
                    required
                    :rules="nameRules"
                    color="grey"
                    class="mb-5"/>
                <v-btn color="#ff99a8" class="button" @click="createCategory">
                    <span class="buttonText">Save</span>    
                </v-btn>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
import CategoryService from '@/services/CategoryService'

require('@/assets/main.css')

export default {
    fullname: 'NewCategoryDialog',
    data() {
        return {
            dialog: false,
            name: '',
            nameRules: [
                v => !!v || 'Name is required'
            ]
        }
    },
    methods: {
        createCategory(){
            if(this.$refs.form.validate()){
                console.log('Creating category')
                CategoryService.createCategory(this.name)
                .then(() => {
                    console.log('Category created')
                    this.dialog = false
                    this.$router.go()
                })
                .catch(() => {
                    console.log('Couldn\'t create category')
                    this.dialog = false
                })
            }
        }
    },
}

</script>