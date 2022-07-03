<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="button" color="#ff99a8" v-bind="attrs" v-on="on">
                <v-icon left>
                    mdi-plus-circle-outline
                </v-icon>
                Add course
            </v-btn>
        </template>
        <v-card class="card">
            <div class="cardHeader">
                <v-avatar size="50" color="#ffe6e9">
                    <v-icon size="35" color="#ff99a8" class="mt-1">mdi-noodles</v-icon>
                </v-avatar>
                <h2 class="cardTitle">New course</h2>
            </div>
            <v-form class="form" ref="form">                
                <v-text-field 
                    v-model="name" 
                    label="Name" 
                    required
                    :rules="nameRules"
                    color="grey"/>            
                <v-text-field 
                    v-model="ingredients" 
                    label="Ingredients" 
                    required
                    :rules="ingredientsRules"
                    color="grey"/>
                <v-text-field 
                    v-model="price" 
                    label="Price" 
                    suffix="€"
                    required
                    :rules="priceRules"
                    color="grey"/>
                <v-select 
                    v-model="courseType" 
                    :items="courseTypes" 
                    label="Course type" 
                    item-text="name" 
                    :rules="courseTypeRules" 
                    required
                    return-object
                    color="grey"></v-select>
                <v-file-input
                    label="Imagen"
                    accept="image/jpg, image/jpeg, image/png"
                    placeholder="Choose your courses's image"
                    prepend-icon="mdi-file-image"
                    small-chips
                    :rules="imageRules"
                    @change="selectImage"
                    color="grey"
                    class="mb-5"
                ></v-file-input>
                <v-btn color="#ff99a8" class="button" @click="addCourse">
                    <span class="buttonText">Save</span>    
                </v-btn>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
import CourseService from '@/services/CourseService'
import { mapState, mapActions } from 'vuex'

require('@/assets/main.css')

export default {
    name: 'AddCourseDialog',
    computed: {
        ...mapState([
            'currentRestaurant',
        ])
    },
    data() {
        return {
            dialog: false,
            courses: [],
            courseTypes: [],
            name: '',
            ingredients: '',
            price: null,
            courseType: '',
            image: null,
            nameRules: [
                v => !!v || 'Name is required'
            ],
            ingredientsRules: [
                v => !!v || 'Ingredients is required'
            ],
            priceRules: [
                v => !!v || 'Price is required'
            ],
            courseTypeRules: [
                v => !!v || 'You must select a course type'
            ],
            imageRules: [
                v => !!v || 'Image is required',
            ],
        }
    },
    methods: {
        ...mapActions([
            'modifyRestaurant'
        ]),
        addCourse(){
            if(this.$refs.form.validate()){
                console.log('Adding course')
                CourseService.createCourse({
                    restaurantId: this.currentRestaurant.id,
                    name: this.name,
                    ingredients: this.ingredients,
                    price: this.price,
                    courseType: this.courseType
                })
                .then(response => {
                    console.log('Course added, saving image')
                    console.log(this.image)
                    CourseService.saveImage(response.data.id, this.image)
                    .then(() => {
                        console.log('Image saved')
                        this.$router.go()
                        this.dialog = false 
                    })
                    .catch(() => {
                        console.log('Couldn\'t save image')
                        this.dialog = false
                    })
                })
                .catch(() => {
                    console.log('Couldn\'t add course')
                    this.dialog = false
                })
            }
        },
        selectImage(image){
            console.log('Getting image')
            this.image = new FormData()
            this.image.append('file', image, image.name)
        },        
        reset(){
            this.name = ''
            this.ingredients = ''
            this.price = null
            this.courseType = ''
            this.image = null
        }
    },
    watch: {
        dialog(){
            this.reset()
        }
    },
    mounted() {
        CourseService.getCourseTypes()
        .then(response => {
            this.courseTypes = response.data
        })
    },
}

</script>