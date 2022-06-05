<template>
    <div class="container">
        <v-data-table :headers="headers" :items="users" sort-by="date" class="elevation-1">
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Users Administration</v-toolbar-title>
                    <v-divider
                        class="mx-4"
                        inset
                        vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                    <div class="adminButton">
                        <admin-dialog></admin-dialog>
                    </div>
                    <div class="selector">
                        <v-select v-model="usersRole" :items="usersRoles" @change="updateUsers" color="grey" background-color="#ffe6e9" rounded></v-select>
                    </div>                    
                </v-toolbar>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-icon
                    v-if="item.status === 'DISABLED'"
                    small
                    class="ml-1"
                    @click="changeUserStatus(item.email)"
                    color="#65ad63"
                >
                    mdi-check-bold
                </v-icon>
                <v-icon
                    v-if="item.status === 'ENABLED'"
                    small
                    class="ml-1"
                    @click="changeUserStatus(item.email)"
                    color="#ff365a"
                >
                    mdi-close-thick
                </v-icon>
                <v-icon
                    v-if="item.roles[0].name === 'ROLE_ADMIN'"
                    small
                    class="ml-1"
                    color="black"
                    @click="deleteAdmin(item.email)"
                >
                    mdi-delete
                </v-icon>
            </template>
        </v-data-table>
    </div>
</template>

<style>
.container {
    margin-top: 2%;
    margin-left: 19%;
    margin-right: 19%;
    width: auto;
}
.selector {
    margin-top: 2%;
    max-width: 170px;
    max-height: 50px;
}
.adminButton {
    margin-right: 3%;
    margin-bottom: 1.5%;
}
</style>

<script>
import { mapState } from 'vuex'
import UserService from '@/services/UserService'

export default {
    name: 'RestaurantBookings',
    components: {
        AdminDialog: () => import("@/components/AdminDialog")
    },
    computed: {
        ...mapState([
            'currentUser',
        ])
    },
    data() {
        return {
            users: [],
            usersRoles: [
                'All',
                'Restaurant',
                'Client',
                'Admin'
            ],
            usersRole: 'All',
            headers: [
                {
                    text: 'Name',
                    align: 'start',
                    value: 'fullname'
                },
                {
                    text: 'Email',
                    value: 'email',
                },
                {
                    text: 'Role',
                    value: 'roles[0].name',
                },
                {
                    text: 'Status',
                    value: 'status',
                },
                {
                    text: 'Actions',
                    value: 'actions',
                    sortable: false
                }
            ],
        }
    },
    methods: {
        changeUserStatus(userEmail){
            console.log('Changing user\'s status')
            UserService.updateUserStatus(userEmail)
            .then(() => {
              console.log('Correctly changed')
              this.$router.go()
            })
            .catch(() => {
              console.log('Couldn\'t change the status')
            })
        },
        deleteAdmin(adminEmail){
            console.log('Deleting admin')
            UserService.deleteAdmin(adminEmail)
            .then(() => {
              console.log('Correctly deleted admin')
              this.$router.go()
            })
            .catch(() => {
              console.log('Couldn\'t delete the admin')
            })
        },
        updateUsers(){
            if(this.usersRole === 'All'){
                console.log('Getting all users')
                UserService.getAllUsers()
                .then(response => {
                    this.users = response.data.filter(user => user.id !== this.currentUser.id)
                })
                }
            else if(this.usersRole === 'Restaurant'){
                console.log('Getting restaurant users')
                UserService.getAllUsers()
                .then(response => {
                    this.users = response.data.filter(user => user.id !== this.currentUser.id && user.roles[0].name === 'ROLE_RESTAURANT')
                })
            }
            else if(this.usersRole === 'Client'){                
                console.log('Getting client users')
                UserService.getAllUsers()
                .then(response => {
                    this.users = response.data.filter(user => user.id !== this.currentUser.id && user.roles[0].name === 'ROLE_CLIENT')
                })
            }
            else if(this.usersRole === 'Admin'){                
                console.log('Getting admin users')
                UserService.getAllUsers()
                .then(response => {
                    this.users = response.data.filter(user => user.id !== this.currentUser.id && user.roles[0].name === 'ROLE_ADMIN')
                })
            }
        }
    },
    mounted() {
        this.updateUsers()
    },
}
</script>