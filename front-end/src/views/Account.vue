<template>
    <v-layout align-center justify-center>
        <v-col cols="10" lg="4">
            <h1>Account page</h1>
            <br/>
            <v-btn @click="logout" color="pink lighten-3">
                <span class="px-8">Logout</span>    
            </v-btn>
        </v-col>
        
    </v-layout>
</template>

<script>
import { mapActions } from 'vuex'

export default {
    methods: {
        ...mapActions([
            'logout'
        ])
    },
}
</script>