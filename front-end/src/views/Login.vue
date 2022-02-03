<template>
    <v-layout align-center justify-center>
        <v-col cols="10" lg="4">
            <v-card class="pa-4">
                <div class="text-center pa-2">
                    <v-avatar size="60" color="pink lighten-5">
                        <v-icon size="40" color="pink lighten-2">mdi-account-circle</v-icon>
                    </v-avatar>
                    <h2 class="pa-1 pink--text text--lighten-2">Login</h2>
                </div>
                <v-form class="pt-2">
                    <v-text-field label="Email" type="email" color="grey" prepend-inner-icon="mdi-at"/>
                    <v-text-field label="Password" color="grey"
                        :type="showPassword ? 'text': 'password'" 
                        prepend-inner-icon="mdi-lock-outline" 
                        :append-icon="showPassword ?  'mdi-eye-outline': 'mdi-eye-off-outline'"
                        @click:append="showPassword = !showPassword"/>
                </v-form>
                <v-card-actions class="justify-center">
                    <v-btn color="pink lighten-3">
                        <span class="px-8">Login</span>
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-col>
    </v-layout>
    
</template>

<script>

export default {
    name: 'Login',

    data() {
        return {
            showPassword: false
        }
    },
}
</script>
