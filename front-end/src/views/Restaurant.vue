<template>
    <div>
        
    </div>
</template>

<style>

</style>

<script>
import RestaurantService from '@/services/RestaurantService'

export default ({
    name: 'Restaurant',
    props: [
        'id'
    ],
    data() {
        return {
            restaurant: null,
        }
    },
    mounted() {
        this.restaurant = RestaurantService.getRestaurant()
    },
})
</script>
