<template>
    <div>
        <div v-if="starterCourses.length != 0">
            <h2 v-if="!clientView" class="my-5">Starters</h2>
            <h3 v-if="clientView" class="my-5 ml-3 text-left">Starters</h3>
            <v-row dense v-for="(course, index) in starterCourses" :key="index" justify="center">
                <v-col :cols="cols">
                    <restaurant-course :course="course"></restaurant-course>
                    <v-divider v-if="clientView && index != starterCourses.length-1" class="my-2"></v-divider>
                </v-col>
            </v-row>
        </div>
        <div v-if="mainCourses.length != 0">
            <h2 v-if="!clientView" class="my-5">Main courses</h2>
            <h3 v-if="clientView" class="my-5 ml-3 text-left">Main courses</h3>
            <v-row dense v-for="(course, index) in mainCourses" :key="index" justify="center">
                <v-col :cols="cols">
                    <restaurant-course :course="course"></restaurant-course>
                    <v-divider v-if="clientView && index != mainCourses.length-1" class="my-2"></v-divider>
                </v-col>
            </v-row>
        </div>
        <div v-if="dessertCourses.length != 0">
            <h2 v-if="!clientView" class="my-5">Desserts</h2>
            <h3 v-if="clientView" class="my-5 ml-3 text-left">Desserts</h3>
            <v-row dense v-for="(course, index) in dessertCourses" :key="index" justify="center">
                <v-col :cols="cols">
                    <restaurant-course :course="course"></restaurant-course>
                    <v-divider v-if="clientView && index != dessertCourses.length-1" class="my-2"></v-divider>
                </v-col>
            </v-row>
        </div>        
        <v-row justify="center" v-if="starterCourses.length == 0 && mainCourses.length == 0 && dessertCourses.length == 0">
            <v-col>
                <h3 v-if="!clientView">No courses</h3>
                <h4 v-if="clientView" class="text-left">No menu</h4>
            </v-col>
        </v-row>
    </div>
</template>

<script>
import CourseService from '@/services/CourseService'
import { mapState } from 'vuex'

export default {
    name: 'RestaurantCourseList',
    props: [
        'restaurantId'
    ],
    components: {
        RestaurantCourse: () => import("@/components/RestaurantCourse")
    },
    computed: {
        ...mapState([
            'currentRestaurant',
        ])
    },
    data() {
        return {
            starterCourses: [],
            mainCourses: [],
            dessertCourses: [],
            cols: '',
            clientView: true
        }
    },
    mounted() {        
        console.log('Getting courses')
        if(this.restaurantId === undefined){
            this.restaurantId = this.currentRestaurant.id
            this.cols = '6'
            this.clientView = false
        }            
        console.log(this.restaurantId)
        CourseService.getCoursesByRestaurant(this.restaurantId, 'STARTER')
        .then(response => {
            console.log('Starters obtained')
            this.starterCourses = response.data
        })
        .catch(() => {
            console.log('Couldn\'t obtain starters')
        })
        CourseService.getCoursesByRestaurant(this.restaurantId, 'MAIN')
        .then(response => {
            console.log('Mains obtained')
            this.mainCourses = response.data
        })
        .catch(() => {
            console.log('Couldn\'t obtain mains')
        })
        CourseService.getCoursesByRestaurant(this.restaurantId, 'DESSERT')
        .then(response => {
            console.log('Desserts obtained')
            this.dessertCourses = response.data
        })
        .catch(() => {
            console.log('Couldn\'t obtain desserts')
        })
    },
}
</script>