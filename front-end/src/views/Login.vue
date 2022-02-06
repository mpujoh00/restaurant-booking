<template>
    <v-layout align-center justify-center>
        <v-col cols="10" lg="4">
            <v-card class="pa-4">
                <div class="text-center pa-2">
                    <v-avatar size="60" color="pink lighten-5">
                        <v-icon size="40" color="pink lighten-2">mdi-account-circle</v-icon>
                    </v-avatar>
                    <h2 class="pa-1 pink--text text--lighten-2">Login</h2>
                </div>
                <v-form class="pt-2" @submit.prevent="loginSubmit">
                    <v-text-field 
                        v-model="email" 
                        label="Email" 
                        type="email" 
                        required
                        :rules="emailRules"
                        color="grey" prepend-inner-icon="mdi-at"/>
                    <v-text-field 
                        v-model="password" 
                        label="Password" 
                        :type="showPassword ? 'text': 'password'" 
                        required
                        :rules="passwordRules"
                        color="grey" prepend-inner-icon="mdi-lock-outline" 
                        :append-icon="showPassword ?  'mdi-eye-outline': 'mdi-eye-off-outline'"
                        @click:append="showPassword = !showPassword"/>
                    <v-btn type="submit" :loading="loggingIn" color="pink lighten-3">
                        <span class="px-8">Login</span>
                    </v-btn>
                </v-form>
                <v-snackbar v-model="loginError">{{ loginError }}</v-snackbar>
            </v-card>
        </v-col>
    </v-layout>
    
</template>


<!-- TO DO: extract style from template -->
<style scoped>
</style>

<script>
import { mapState, mapActions } from 'vuex'

export default {
    name: 'Login',
    data() {
        return {
            showPassword: false,
            email: '',
            emailRules: [
                v => !!v || 'Email is required',
                v => (v && v.length >= 10) || 'Email must be at least 10 characters'
            ],
            password: '',
            passwordRules: [
                v => !!v || 'Password is required',
                v => (v && v.length >= 8) || 'Password must be at least 8 characters'
            ],
        }
    },
    computed: {
        ...mapState([
            'loggingIn',
            'loginError'
        ])
    },
    methods: {
        ...mapActions([
            'doLogin'
        ]),
        loginSubmit(){
            this.doLogin({
                email: this.email,
                password: this.password
            })
        }
    }
}
</script>
