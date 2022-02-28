<template>
    <v-layout class="layout">
        <v-col cols="6">
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
                    <v-btn type="submit" :loading="loggingIn" color="#ff99a8">
                        <span class="button">Login</span>
                    </v-btn>
                </v-form>
                <v-snackbar v-model="loginError">{{ loginError }}</v-snackbar>
            </v-card>
        </v-col>
    </v-layout>
</template>


<!-- TO DO: extract style from template -->
<style scoped>
.layout {
    align-content: center;
    justify-content: center;
}
.card {
    padding: 4%;
}
.cardHeader {
    text-align: center;
    padding: 2%;
}
.cardTitle {
    padding: 1%;
    color: #ff99a8;
}
.form {
    padding-top: 2%;
}
.button {
    padding-left: 8%;
    padding-right: 8%;
}
</style>

<script>
import { mapState, mapActions } from 'vuex'

export default {
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
            'login'
        ]),
        loginSubmit(){
            this.login({
                email: this.email,
                password: this.password
            })
        }
    }
}
</script>
