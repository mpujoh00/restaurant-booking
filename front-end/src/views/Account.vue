<template>
    <v-layout class="layout">
        <v-col cols="6">
            <v-card class="card">
                <div class="cardHeader">
                    <v-avatar size="50" color="#ffe6e9">
                        <v-icon size="40" color="#ff99a8">mdi-account-circle</v-icon>
                    </v-avatar>
                    <h2 class="cardTitle">Account</h2>
                </div>
                <v-form class="form" >
                    <!-- modify user -->
                    <div v-if="!editPassword">
                        <v-text-field 
                            v-model="user.fullname" 
                            label="Fullname" 
                            required
                            :readonly="nonEditable"
                            color="grey" prepend-inner-icon="mdi-account"/>
                        <v-text-field 
                            v-model="user.email" 
                            label="Email" 
                            type="email" 
                            required
                            :rules="emailRules"
                            readonly
                            color="grey" class="mb-5" prepend-inner-icon="mdi-at"/>
                        <!-- default mode buttons -->
                        <div v-if="nonEditable">
                            <v-btn @click="editUser" class="button" color="#ff99a8">
                                <span class="buttonText">Edit</span>    
                            </v-btn>
                            <v-btn @click="changePassword" class="button" color="#ff99a8">
                                <span class="buttonText">Change password</span>    
                            </v-btn>
                            <v-btn @click="deleteUserDialog" class="button" color="#ff99a8">
                                <span class="buttonText">Delete</span>    
                            </v-btn>
                            <v-btn @click="logout" class="button" color="#ff99a8">
                                <span class="buttonText">Logout</span>    
                            </v-btn>
                        </div>    
                        <!-- edit mode buttons -->
                        <div v-else-if="!nonEditable">                                
                            <v-btn v-if="!nonEditable" @click="saveUser" class="button" color="#ff99a8">
                                <span class="buttonText">Save</span>    
                            </v-btn>
                            <v-btn v-if="!nonEditable" @click="cancel" class="button" color="#ff99a8">
                                <span class="buttonText">Cancel</span>    
                            </v-btn>
                        </div>                        
                    </div>
                    <!-- password change -->
                    <div v-else-if="editPassword">
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

                        <v-btn @click="savePassword" class="button" color="#ff99a8">
                            <span class="buttonText">Save</span>    
                        </v-btn>
                        <v-btn @click="cancel" class="button" color="#ff99a8">
                            <span class="buttonText">Cancel</span>    
                        </v-btn>
                    </div>
                </v-form>
            </v-card>
        </v-col>
        <ConfirmationDialog ref="confirm"/>        
        <v-snackbar v-model="error">{{ error }}</v-snackbar>
    </v-layout>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import router from '@/router'

require('@/assets/main.css')

export default {
    components: {
        ConfirmationDialog: () => import("@/components/ConfirmationDialog.vue")
    },
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
            'editPasswordError',
            'deleteUserError',
            'error'
        ])
    },
    methods: {
        ...mapActions([
            'logout',
            'modifyUser',
            'modifyPassword',
            'deleteUser',
            'changeErrorMessage'
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
            .catch(error => {
                console.log(error)
                this.changeErrorMessage('Couldn\'t save user')
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
                this.changeErrorMessage('Couldn\'t change password')
                this.cancel()
                
            })
        },
        cancel(){
            if(this.editPassword || this.nonEditable)
                this.resetUser()
            this.nonEditable = true
            this.editPassword = false
            this.currentPassword = ''
            this.currentPasswordConfirmation = ''
            this.newPassword = ''
            this.sleep(2000).then(() => this.changeErrorMessage(null))            
        },
        async deleteUserDialog() {
            this.$refs.confirm.open("Confirm", "Are you sure you want to delete your account?")
            .then(() => {
                // if clicked on yes
                console.log("Deleting account")
                this.deleteUser(this.currentUser.email)
                .then(() => {
                    router.push('/')
                })
                .catch(() => {
                    console.log(this.deleteUserError)
                    this.changeErrorMessage('Couldn\'t delete user')
                })
            })
        },
        resetUser(){
            this.user = JSON.parse(JSON.stringify(this.currentUser))
        },
        sleep(ms){
            return new Promise(resolve => setTimeout(resolve, ms))
        }
    },
    mounted() {
        this.resetUser()
    },
}
</script>