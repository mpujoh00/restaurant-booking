<template>
    <v-layout class="layout">
        <v-col cols="6">
            <h1>Account page</h1>
            <br/>
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="40" color="#ff99a8">mdi-account-circle</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">Account</h2>
                </div>
                <v-form class="form" >  <!-- TODO: FIX prevent with multiple buttons -->
                    <!-- modify user -->
                    <div v-if="!editPassword">
                        <v-text-field 
                            v-model="user.fullname" 
                            label="Fullname" 
                            required
                            :readonly="nonEditable"
                            color="grey" prepend-inner-icon="mdi-at"/>
                        <v-text-field 
                            v-model="user.email" 
                            label="Email" 
                            type="email" 
                            required
                            :rules="emailRules"
                            readonly
                            color="grey" class="mb-5" prepend-inner-icon="mdi-at"/>

                        <div v-if="nonEditable">
                            <v-btn v-if="nonEditable" @click="editUser" color="#ff99a8" class="button">
                                <span class="buttonText">Edit</span>    
                            </v-btn>
                            <v-btn v-if="nonEditable" @click="changePassword" color="#ff99a8" class="button">
                                <span class="buttonText">Change password</span>    
                            </v-btn>
                            <v-btn v-if="nonEditable" @click="logout" color="#ff99a8" class="button">
                                <span class="buttonText">Logout</span>    
                            </v-btn>
                        </div>    
                        <div v-else-if="!nonEditable">                                
                            <v-btn v-if="!nonEditable" @click="saveUser" color="#ff99a8" class="button">
                                <span class="buttonText">Save</span>    
                            </v-btn>
                            <v-btn v-if="!nonEditable" @click="cancel" color="#ff99a8" class="button">
                                <span class="buttonText">Cancel</span>    
                            </v-btn>
                        </div>                        
                    </div>
                    <!-- password change -->
                    <div v-else-if="editPassword">
                        <!-- TODO: (improvement) fix autofilled passwords with username and pass -->
                        <v-text-field 
                            v-model="currentPassword" 
                            label="Password" 
                            :type="showCurrentPassword ? 'text': 'password'" 
                            :rules="passwordRules"
                            required
                            :append-icon="showCurrentPassword ?  'mdi-eye-outline': 'mdi-eye-off-outline'"
                            @click:append="showCurrentPassword = !showCurrentPassword"
                            color="grey" prepend-inner-icon="mdi-at"/>
                        <v-text-field 
                            v-model="currentPasswordConfirmation" 
                            label="Confirm password"
                            :type="showConfirmationPassword ? 'text': 'password'"  
                            :rules="passwordRules.concat(currentPasswordConfirmationRules)"
                            required
                            :append-icon="showConfirmationPassword ?  'mdi-eye-outline': 'mdi-eye-off-outline'"
                            @click:append="showConfirmationPassword = !showConfirmationPassword"
                            color="grey" class="mb-5" prepend-inner-icon="mdi-at"/>
                        <v-text-field 
                            v-model="newPassword" 
                            label="New password" 
                            :type="showNewPassword ? 'text': 'password'" 
                            :rules="passwordRules"
                            required
                            :append-icon="showNewPassword ?  'mdi-eye-outline': 'mdi-eye-off-outline'"
                            @click:append="showNewPassword = !showNewPassword"
                            color="grey" class="mb-5" prepend-inner-icon="mdi-at"/>

                        <v-btn @click="savePassword" color="#ff99a8" class="button">
                            <span class="buttonText">Save</span>    
                        </v-btn>
                        <v-btn @click="cancel" color="#ff99a8" class="button">
                            <span class="buttonText">Cancel</span>    
                        </v-btn>
                    </div>
                </v-form>
                <v-snackbar v-if="editPassword" v-model="editPasswordError">{{ editPasswordError }}</v-snackbar>
            </v-card>
        </v-col>
        
    </v-layout>
</template>

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
    margin-left: 3%;
    margin-right: 3%;
}
.buttonText {
    padding-left: 8%;
    padding-right: 8%;
}
</style>

<script>
import { mapState, mapActions } from 'vuex'

export default {
    data() {
        return {
            nonEditable: true,
            editPassword: false,
            showCurrentPassword: false,
            showConfirmationPassword: false,
            showNewPassword: false,
            email: '',
            emailRules: [
                v => !!v || 'Email is required',
                v => v.length >= 10 || 'Email must be at least 10 characters'
            ],
            currentPassword: '',
            currentPasswordConfirmation: '',
            newPassword: '',
            passwordRules: [
                v => !!v || 'Password is required',
                v => v.length >= 8 || 'Password must be at least 8 characters'
            ],
            currentPasswordConfirmationRules: [
                v => v == this.currentPassword || 'Passwords do not match'
            ],
            user: null
        }
    },
    computed: {
        ...mapState([
            'currentUser',
            'editPasswordError'
        ])
    },
    methods: {
        ...mapActions([
            'logout',
            'modifyUser',
            'modifyPassword'
        ]),
        editUser(){
            this.nonEditable = !this.nonEditable
        },
        changePassword(){
            this.editPassword = !this.editPassword
        },
        saveUser(){
            this.modifyUser({
                email: this.user.email,
                fullname: this.user.fullname
            })
            .then(error => {
                console.log(error)
                this.cancel()
            })
            this.nonEditable = !this.nonEditable
        },
        savePassword(){
            this.modifyPassword({
                currentPassword: this.currentPassword,
                newPassword: this.newPassword
            })
            .then(() => {
                this.nonEditable = true
                this.editPassword = false
            })
            .catch(error => {
                console.log(error)
            })
        },
        cancel(){
            if(this.editPassword)
                this.resetUser()
            this.nonEditable = true
            this.editPassword = false
            this.currentPassword = ''
            this.currentPasswordConfirmation = ''
            this.newPassword = ''
        },
        resetUser(){
            this.user = JSON.parse(JSON.stringify(this.currentUser))
        } 
    },
    mounted() {
        this.resetUser()
    },
}
</script>