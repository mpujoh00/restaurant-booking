<template>
    <v-container>
        <restaurant-courses-list style="margin-top: -3%;"></restaurant-courses-list>            
        <v-row class="mt-10" justify="center">
            <add-course-dialog></add-course-dialog>
        </v-row>
    </v-container>
</template>

<script>

export default {
    name: 'RestaurantCourses',
    components: {
        AddCourseDialog: () => import("@/components/AddCourseDialog"),
        RestaurantCoursesList: () => import("@/components/RestaurantCoursesList")
    },
}
</script>
