<template>
    <div class="home">
        <br/>
        <h1>Your Tables</h1>
        <br/>
        <v-container class="container">
            <v-row dense class="mb-5">
                <table-dialog></table-dialog>
            </v-row>
            <v-row dense>
                <v-col v-for="(table, index) in tables" cols="12" :key="index">
                    <v-card class="ma-1">
                        <v-card-title class="text-h5">
                            Table {{ table.number }}
                        </v-card-title>
                        <v-card-text>
                            <div style="text-align: left">
                                Minimum people: {{ table.minPeople }}, Maximum people: {{ table.maxPeople }}
                                <v-btn
                                    class="ml-10"
                                    fab
                                    icon
                                    height="40px"
                                    width="40px"
                                    @click="deleteTable(table)"
                                >
                                    <v-icon>mdi-delete-forever</v-icon>
                                </v-btn>
                            </div>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<style>
.container {
    margin-left: 30%;
    margin-right: 30%;
    width: auto;
}
</style>

<script>
import { mapState } from 'vuex'
import TableService from '@/services/TableService'

export default {
    name: 'RestaurantTablesAdministration',
    components: {
        TableDialog: () => import("@/components/TableDialog")
    },
    computed: {
        ...mapState([
            'currentRestaurant',
        ])
    },
    data() {
        return {
            tables: [],
        }
    },
    methods: {
        deleteTable(table){
            console.log('Deleting table ' + table.number)
            TableService.deleteTable(table.id)
            .then(() => {
                console.log('Table deleted')
                this.$router.go()
            })
            .catch(() => {
                console.log('Couldn\'t delete table')
            })
        }
    },
    mounted() {
        console.log('getting tables')
        TableService.getRestaurantTables(this.currentRestaurant.id).then(response => {
            this.tables = response.data
        })
    },
}
</script>