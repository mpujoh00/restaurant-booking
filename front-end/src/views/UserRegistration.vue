<template>
    <v-layout class="layout" align-center>
        <v-col cols="6">
            <br/>
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="40" color="#ff99a8">mdi-account-circle</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">User information</h2>
                </div>
                <v-form class="form" @submit.prevent="registrationSubmit"> 
                    <v-text-field 
                        v-model="fullname" 
                        label="Fullname" 
                        :rules="fullnameRules"
                        required
                        color="grey" prepend-inner-icon="mdi-at"/>
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
                        :rules="passwordRules"
                        required
                        :append-icon="showPassword ?  'mdi-eye-outline': 'mdi-eye-off-outline'"
                        @click:append="showPassword = !showPassword"
                        color="grey" prepend-inner-icon="mdi-at"/>
                    <v-text-field 
                        v-model="confirmationPassword" 
                        label="Confirm password"
                        :type="showConfirmationPassword ? 'text': 'password'"  
                        :rules="passwordRules.concat(passwordConfirmationRules)"
                        required
                        :append-icon="showConfirmationPassword ?  'mdi-eye-outline': 'mdi-eye-off-outline'"
                        @click:append="showConfirmationPassword = !showConfirmationPassword"
                        color="grey" class="mb-5" prepend-inner-icon="mdi-at"/>

                    <v-btn type="submit" color="#ff99a8" class="button">
                        <span class="buttonText">{{ submitButtonLabel }}</span>    
                    </v-btn>
                    <v-btn @click="cancel" color="#ff99a8" class="button">
                        <span class="buttonText">Cancel</span>    
                    </v-btn>               
                </v-form>
            </v-card>
        </v-col>
        
    </v-layout>
</template>

<style scoped>
.layout {
    align-content: center;
    justify-content: center;
    height: 80vh;
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
    margin-left: 3%;
    margin-right: 3%;
}
.buttonText {
    padding-left: 8%;
    padding-right: 8%;
}
</style>

<script>
import { mapActions } from 'vuex'
import router from '@/router'

export default ({
    props: [
        'role'
    ],
    data() {
        return{
            fullname: '',
            email: '',
            password: '',
            roleName: '',
            confirmationPassword: '',
            fullnameRules: [
                v => !!v || 'Fullname is required',
                v => v.length >= 3 || 'Fullname must be at least 3 characters'
            ],
            emailRules: [
                v => !!v || 'Email is required',
                v => v.length >= 10 || 'Email must be at least 10 characters'
            ],
            showPassword: false,
            showConfirmationPassword: false,
            passwordRules: [
                v => !!v || 'Password is required',
                v => v.length >= 8 || 'Password must be at least 8 characters'
            ],
            passwordConfirmationRules: [
                v => v == this.password || 'Passwords do not match'
            ],
            submitButtonLabel: ''
        }
    },
    methods: {
        ...mapActions([
            'registerUser',
            'saveTempUser'
        ]),
        registrationSubmit(){       
            const user = {
                fullname: this.fullname,
                email: this.email,
                password: this.password,
                role: this.role
            }  
            if(this.roleName !== 'restaurant')   {
                this.registerUser(user)
                router.push('/account')
            }
            // if role is restaurant, a next page will appear to register the restaurant
            if(this.roleName === 'restaurant'){
                this.saveTempUser(user)
                router.push('/restaurant-registration')
            }
            // todo review prevent ¿?
        },
        cancel(){
            router.push('/login')
            this.reset()
        },
        reset(){
            this.fullname = '',
            this.email = '',
            this.password = '',
            this.confirmationPassword = ''
            this.showPassword = false
            this.showConfirmationPassword = false
        }
    },
    mounted() {
        this.reset()
        if(this.role === 'ROLE_CLIENT'){
            this.roleName = 'client'
            this.submitButtonLabel = 'Register'
        }            
        else if(this.role === 'ROLE_RESTAURANT'){
            this.roleName = 'restaurant'
            this.submitButtonLabel = 'Next'
        }            
        else
            this.roleName = 'admin'
    },
    // TODO cambiar título
})
</script>
