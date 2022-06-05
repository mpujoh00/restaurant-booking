<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="button mt-5" color="#ff99a8" v-bind="attrs" v-on="on">
                <span class="buttonText">Add admin</span>
            </v-btn>
        </template>
        <v-card class="card">
            <div class="cardHeader">
                <v-avatar size="50" color="#ffe6e9">
                    <v-icon size="35" color="#ff99a8">mdi-account-star</v-icon>
                </v-avatar>
                <h2 class="cardTitle">Admin</h2>
            </div>
            <v-form class="form" ref="form">
                <v-text-field 
                    v-model="fullname" 
                    label="Fullname" 
                    required
                    :rules="fullnameRules"
                    color="grey"/>
                <v-text-field 
                    v-model="email" 
                    label="Email" 
                    required
                    :rules="emailRules"
                    color="grey"/>
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
                <v-btn color="#ff99a8" class="button" @click="registerAdmin">
                    <span class="buttonText">Register</span>    
                </v-btn>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<style scoped>
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
import UserService from '@/services/UserService'

export default {
    fullname: 'AdminDialog',
    data() {
        return {
            dialog: false,
            fullname: '',
            email: '',
            password: '',
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
        }
    },
    methods: {
        registerAdmin(){
            if(this.$refs.form.validate()){
                console.log('Registering admin...')
                UserService.registerAdmin({
                    fullname: this.fullname,
                    email: this.email,
                    password: this.password,
                    role: 'ROLE_ADMIN'
                })
                .then(() => {
                    console.log('Admin created')
                    this.$router.go()
                })
                .catch(() => {
                    console.log('Couldn\'t create admin')
                })
            }
        }
    },
}
</script>