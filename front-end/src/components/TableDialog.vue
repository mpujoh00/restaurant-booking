<template>
    <v-dialog v-model="dialog" :max-width="600">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="button mt-5" color="#ff99a8" v-bind="attrs" v-on="on">
                <span class="buttonText">Add table</span>
            </v-btn>
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
                <v-btn class="button mt-4" color="#ff99a8" @click="saveTable">
                    <span class="buttonText">Save</span>
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
import { mapState } from 'vuex'
import TableService from '@/services/TableService'

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
                v => !!v || 'Maximum number of people is required'
            ],
        }
    },
    methods: {
        saveTable(){
            if(this.$refs.form.validate()){
                console.log('Creating table...')
                TableService.createTable(this.currentRestaurant.id, {
                    number: this.number,
                    minPeople: this.minPeople,
                    maxPeople: this.maxPeople
                })
                .then(() => {
                    console.log('Table created')
                    this.$router.go()
                })
                .catch(() => {
                    console.log('Couldn\'t create table')
                })
            }
        }
    },
}
</script>