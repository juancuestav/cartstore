<template>
  <v-footer color="blue-grey darken-3" padless>
    <v-row justify="center" no-gutters>
      <v-col class="blue-grey darken-3 py-4 text-center white--text" cols="12">
        UTMACH - {{ new Date().getFullYear() }}
      </v-col>
    </v-row>
  </v-footer>
</template>
<script>
export default {
  name: "Footer",
};
</script>