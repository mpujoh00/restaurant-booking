<template>
    <v-layout class="layout" align-center>
        <!-- Login option -->
        <v-col cols="5">
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="40" color="#ff99a8">mdi-account-circle</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">Login</h2>
                </div>
                <v-form class="form" @submit.prevent="loginSubmit">
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
                    <v-btn type="submit" :loading="loggingIn" class="button" color="#ff99a8">
                        <span class="button">Login</span>
                    </v-btn>
                </v-form>
                <v-snackbar v-model="error">{{ error }}</v-snackbar>
            </v-card>
        </v-col>
        <!-- Registration option -->
        <v-col cols="4">
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="35" color="#ff99a8">mdi-account-plus</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">Registration</h2>
                </div>
                <div class="registrationButtons">
                    <div>
                        <v-btn class="button" color="#ff99a8" @click="registration('ROLE_CLIENT')">
                            <span class="buttonText">Register client</span>
                        </v-btn>
                    </div>
                    <div>
                        <v-btn class="button" color="#ff99a8" @click="registration('ROLE_RESTAURANT')">
                            <span class="buttonText">Register restaurant</span>
                        </v-btn>
                    </div> 
                </div>
            </v-card>
        </v-col>
    </v-layout>
</template>

<style scoped>
.layout {
    height: 80vh;
}
.card {
    padding: 5%;
    margin: 2%;
    height: 47vh;
}
.button {
    margin: 3%;
    width: 25vh;
}
.registrationButtons {
    padding-top: 5vh;
}
</style>

<script>
import { mapState, mapActions } from 'vuex'
import router from '@/router'

require('@/assets/main.css')

export default {
    data() {
        return {
            showPassword: false,
            email: '',
            emailRules: [
                v => !!v || 'Email is required',
                v => (v && v.length >= 10) || 'Email must be at least 10 characters',
                v => /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) || 'Email must have a valid format',
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
            'error'
        ])
    },
    methods: {
        ...mapActions([
            'login'
        ]),
        loginSubmit(){
            this.login({
                email: this.email,
                password: this.password
            })
        },
        registration(roleName){
            console.log('Going to register a user with ' + roleName)
            router.push({name: 'userRegistration', params: {role: roleName}})
        }
    }
}
</script>
