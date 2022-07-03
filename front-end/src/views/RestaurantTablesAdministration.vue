<template>
    <div class="container">
        <v-data-table :headers="headers" :items="tables" sort-by="number" class="elevation-1">
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Your tables</v-toolbar-title>
                    <v-divider
                        class="mx-4"
                        inset
                        vertical
                    ></v-divider>     
                    <v-spacer></v-spacer>
                    <div class="tableIconButton">
                        <table-dialog></table-dialog>
                    </div>
                </v-toolbar>           
            </template>
            <template v-slot:item.actions="{ item }">
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-icon
                            small
                            class="ml-1"
                            @click="deleteTable(item.id)"
                            color="#ff365a"
                            v-bind="attrs"
                            v-on="on"
                        >
                            mdi-delete
                        </v-icon>
                    </template>
                    <span>Delete</span>
                </v-tooltip> 
            </template>
        </v-data-table>
        <ConfirmationDialog ref="confirm"/>
        <v-snackbar v-model="error">{{ error }}</v-snackbar>
    </div>
</template>

<style>
.container {
    margin-left: 30%;
    margin-right: 30%;
}
</style>

<script>
import { mapState, mapActions } from 'vuex'
import TableService from '@/services/TableService'

require('@/assets/main.css')

export default {
    name: 'RestaurantTablesAdministration',
    components: {
        TableDialog: () => import("@/components/TableDialog"),
        ConfirmationDialog: () => import("@/components/ConfirmationDialog.vue")
    },
    computed: {
        ...mapState([
            'currentRestaurant',
            'error'
        ])
    },
    data() {
        return {
            tables: [],
            headers: [
                {
                    text: 'Name',
                    align: 'start',
                    value: 'name'
                },
                {
                    text: 'Minimum people',
                    value: 'minPeople',
                },
                {
                    text: 'Maximum people',
                    value: 'maxPeople',
                },
                {
                    text: 'Actions',
                    value: 'actions',
                    sortable: false
                }
            ]
        }
    },
    methods: {
        ...mapActions([
            'changeErrorMessage'
        ]),
        deleteTable(tableId){
            this.$refs.confirm.open("Confirm", "Are you sure you want to delete the table?")
            .then(() => {
                console.log('Deleting table')
                TableService.deleteTable(tableId)
                .then(() => {
                    console.log('Table deleted')
                    this.$router.go()
                })
                .catch(() => {
                    console.log('Couldn\'t delete table')
                    this.changeErrorMessage('Couldn\'t delete table')
                })
            })
        }
    },
    mounted() {
        console.log('getting tables')
        TableService.getRestaurantTables(this.currentRestaurant.id).then(response => {
            this.tables = response.data.map(table => {
                table.name = 'Table ' + table.number
                return table
            })
        })
    },
}
</script>