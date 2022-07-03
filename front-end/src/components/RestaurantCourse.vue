<template>
    <v-card class="ma-1 pa-2" :color="color" max-height="75" :flat="currentUser.role != 'ROLE_RESTAURANT'">
        <v-row no-gutters>
            <v-col sm="2">
                <v-img 
                    :src="'data:image/jpg;base64,' + course.image" 
                    alt="image"
                    height="60"
                    max-width="60"
                    class="rounded-lg"
                ></v-img>
            </v-col>
            <v-col class="d-flex flex-no-wrap justify-space-between">
                <div class="course">
                    <v-card-title v-text="course.name" class="text-subtitle-2"></v-card-title>
                    <v-card-subtitle class="text-caption" style="font-size: small;">
                        {{ course.ingredients }}
                    </v-card-subtitle>
                </div>
                <div class="text-subtitle-2 price">
                    {{ course.price }} €
                    <v-btn v-if="currentUser.role == 'ROLE_RESTAURANT'" @click="deleteCourse(course.id)" icon class="ml-2 mb-1">
                        <v-icon>mdi-delete</v-icon>
                    </v-btn>
                </div>
            </v-col>
        </v-row>
    </v-card>
</template>

<style scoped>
.course {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    margin-top: -1.6%;
    margin-left: -5%;
}
.price{
    margin-top: 2.5%;
}
</style>

<script>
import CourseService from '@/services/CourseService'
import { mapState } from 'vuex'

export default {
    name: 'RestaurantCourse',
    props: [
        'course'
    ],
    computed: {
        ...mapState([
            'currentUser',
        ])
    },
    data() {
        return {
            color: ''
        }
    },
    methods: {
        deleteCourse(courseId){
            console.log('Deleting course')
            CourseService.deleteCourse(courseId)
            .then(() => {
                console.log('Course deleted')
                this.$router.go()
            })
            .catch(() => {
                console.log('Couldn\'t delete course')
            })
        }
    },
    mounted() {
        if(this.currentUser.role === 'ROLE_RESTAURANT'){
            this.color = '#f7f7f7'
        }
    },
}
</script>