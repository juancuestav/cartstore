<template>
  <div>
     <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <p class="text-h4">Dirección de envío</p>
              </v-col>
              <v-col cols="4"
               v-for="item in Items"
                   :key="item.dato">
                 <v-card >
            <v-card-title class="blue-grey lighten-4">{{ item.nombre }}</v-card-title>
                <v-divider></v-divider>
              <v-card-text>
                <v-container>
                  <v-row>
                      <v-col cols="6">
                        <v-toolbar-title >Dirección:</v-toolbar-title>
                      </v-col>
                      <v-col cols="6">
                        <v-toolbar-title >{{ item.direccion }}</v-toolbar-title>
                      </v-col>
                      <v-col cols="6">
                        <v-toolbar-title>Ciudad:</v-toolbar-title>
                      </v-col>
                      <v-col cols="6">
                          <v-toolbar-title >{{ item.ciudad }}</v-toolbar-title>
                      </v-col>
                        <v-col cols="6">  
                          <v-toolbar-title >Código Postal:</v-toolbar-title>
                      </v-col>
                      <v-col cols="6">
                           <v-toolbar-title >{{ item.postal }}</v-toolbar-title>
                      </v-col>
                        <v-col cols="6">  
                          <v-toolbar-title >Teléfono:</v-toolbar-title>
                      </v-col>
                      <v-col cols="6">
                           <v-toolbar-title >{{ item.telefono }}</v-toolbar-title>
                      </v-col>
                  </v-row>
                </v-container>
               </v-card-text>
                <v-divider></v-divider>
                <v-card-actions>
                    <v-container>
                        <v-row>
                        <v-col cols="12" class="py-0">
                            <v-btn   :to="{ name: 'Pedidos' }" color="blue-grey" class="ma-2 white--text" block >Entregar a esta dirección</v-btn>
                        </v-col>
                        <v-col cols="6" class="py-0">
                            <v-btn   color="blue-grey" class="ma-2 white--text" block >Editar</v-btn>
                        </v-col>
                        <v-col cols="6" class="py-0">
                            <v-btn   color="blue-grey lighten-4" class="ma-2" block >Eliminar</v-btn>
                        </v-col>
                        </v-row>
                    </v-container>
                </v-card-actions>
               </v-card>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
  </div>
</template>

<script>
export default {
  name: "Carrito",
 
  data() {
    return {
       Items: [
        { dato: 'Dirección 1 ', nombre: 'Juanito', direccion:'Av.Jubones', ciudad:'Machala', pais:'Ecuador', postal:'101256', telefono:'0923571820'},
        ],
    };
  },
  methods: {
    closeDialog() {
      this.dialog = false;
    },
  },
};
</script>

<style>
</style>