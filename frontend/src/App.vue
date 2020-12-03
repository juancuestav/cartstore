<template>
  <v-app>
    <NavigationBar/>
    
    <!-- Contenido de otras vistas -->
    <v-main>
      <router-view ></router-view>
    </v-main>

    <Footer />
  </v-app>
</template>
 
<script>
import NavigationBar from "@/components/NavigationBar";
import Footer from "@/components/Footer";
export default {
  components: {
    NavigationBar,
    Footer,
  },
};
</script>