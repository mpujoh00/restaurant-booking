<template>
    <v-dialog v-model="dialog" @keydown.esc="cancel" :max-width="400">
        <v-card>
            <v-toolbar dense flat color="#ffe6e9">
                <v-toolbar-title class="text-body-1  grey--text">
                    {{title}}
                </v-toolbar-title>
            </v-toolbar>
            <v-card-text v-show="!!message" class="question" v-html="message"/>
            <v-card-actions class="buttons">
                <spacer></spacer>
                <v-btn v-if="!noConfirm" color="grey lighten-1" class="button"  @click.native="cancel">
                    No
                </v-btn>
                <v-btn color="#ff99a8" class="button" @click.native="agree">
                    Yes
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<style scoped>
.question {
    margin-top: 4%;
    font-weight: bold;
    font-size: 18px;
}
.buttons {
    justify-content: center;
}
.button {
    font-weight: bold;
    margin-bottom: 2%;
}
</style>

<script>
export default {
    name: "ConfirmationDialog",
    data() {
        return {
            dialog: false,
            resolve: null,
            reject: null,
            title: '',
            message: '',
            noConfirm: false
        }
    },
    methods: {
        open(title, message, noConfirm) {
            this.dialog = true
            this.title = title
            this.message = message
            this.noConfirm = noConfirm
            return new Promise((resolve, reject) => {
                this.resolve = resolve
                this.reject = reject
            })

        },
        agree() {
            this.dialog = false
            this.resolve(true)
        },
        cancel() {
            this.dialog = false
            this.resolve(false)
        }
    }
}
</script>
