<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on: dialog, attrs }">
            <v-tooltip bottom>
                <template v-slot:activator="{ on: tooltip }">
                    <v-btn class="button mt-5" icon color="#ff99a8" v-bind="attrs" v-on="{...tooltip, ...dialog}">
                        <v-icon>mdi-plus-circle-outline</v-icon>
                    </v-btn>
                </template>
                <span>Add table</span>
            </v-tooltip> 
        </template>
        <v-card class="card">
            <div class="cardHeader">
                <v-avatar size="50" color="#ffe6e9">
                    <v-icon size="35" color="#ff99a8">mdi-table-chair</v-icon>
                </v-avatar>
                <h2 class="cardTitle">Table</h2>
            </div>
            <v-form class="form" ref="form">
                <v-text-field 
                    v-model="number" 
                    label="Number" 
                    required
                    :rules="numberRules"
                    color="grey"/>
                <v-text-field 
                    v-model="minPeople" 
                    label="Minimum number of people" 
                    required
                    :rules="minPeopleRules"
                    color="grey"/>
                <v-text-field 
                    v-model="maxPeople" 
                    label="Maximum number of people" 
                    required
                    :rules="maxPeopleRules"
                    color="grey"/>
                <v-btn class="button mt-4" color="#ff99a8" :loading="loading" @click="saveTable">
                    <span class="buttonText">Save</span>
                </v-btn>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
import { mapState } from 'vuex'
import TableService from '@/services/TableService'

require('@/assets/main.css')

export default {
    name: 'TableDialog',
    computed: {
        ...mapState([
            'currentRestaurant'
        ])
    },
    data() {
        return {
            dialog: false,
            number: null,
            minPeople: null,
            maxPeople: null,
            numberRules: [
                v => !!v || 'Number is required'
            ],
            minPeopleRules: [
                v => !!v || 'Minimum number of people is required'
            ],
            maxPeopleRules: [
                v => !!v || 'Maximum number of people is required',
                v => v >= this.minPeople || 'The maximum number of people can\'t be smaller than the minimum' 
            ],
            loading: false
        }
    },
    methods: {
        saveTable(){
            if(this.$refs.form.validate()){
                console.log('Creating table...')
                this.loading = true
                TableService.createTable(this.currentRestaurant.id, {
                    number: this.number,
                    minPeople: this.minPeople,
                    maxPeople: this.maxPeople
                })
                .then(() => {
                    console.log('Table created')
                    this.dialog = false
                    this.$router.go()
                })
                .catch(() => {
                    console.log('Couldn\'t create table')
                })
                this.dialog = false
                this.loading = false
                this.reset()
            }
        },
        reset() {
            this.number = null
            this.minPeople = null
            this.maxPeople = null
        }
    },
}
</script>