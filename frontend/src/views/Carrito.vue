<template>
  <v-container>
    <v-row>
      <v-col md="8">
        <v-card>
          <v-data-table :headers="headers" :items="carrito_list" hide-default-footer >

            <!--<template v-slot:item.prod_image="{ item }">
              <div class="pa-2 d-flex flex-column align-center">
                <v-img
                  src="https://cdn.vuetifyjs.com/images/cards/cooking.png"
                  height="120px"
                  width="150px"
                ></v-img>
              </div>
            </template>-->

            <template v-slot:item.foto="{ item }">
              <div class="p-2 d-flex flex-column align-center">
                <v-img :src="`${servidor}${item.foto}`" max-width="100px" contain style="margin: 10px 0px" ></v-img>
              </div>
            </template>

            <template v-slot:item.stock="{ item }">
              <div class="px-4 d-flex flex-column align-center">
                <v-text-field type="number" v-model="item.stock"></v-text-field> 
              </div>
            </template>

            <template v-slot:item.actions="{ item }">
              <v-icon color="red"> mdi-delete </v-icon>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
      <v-col md="4">
        <v-card>
          <v-card-title class="blue-grey darken-1 white--text"
            >Generar Compra</v-card-title
          >
          <v-divider></v-divider>

          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-toolbar-title>Subtotal:</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title class="blue-grey lighten-5">{{
                    sub_total
                  }}</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title>IVA 12%:</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title class="blue-grey lighten-5">{{
                    iva
                  }}</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title>Total a Pagar:</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title class="blue-grey lighten-5">{{
                    total_pagar
                  }}</v-toolbar-title>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>

          <v-divider></v-divider>

          <v-card-actions>
            <v-container>
              <v-row>
                <v-col cols="12" class="py-0">
                  <v-btn :to="{ name: 'Envio' }" @click="realizarPago" color="blue" class="ma-2 white--text" block >Realizar Pago</v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      servidor: "http://localhost:8080/",
      carrito_indices: [],
      sub_total: 0,
      carrito_list: [],
      producto: {
        nombre: "",
        descripcion: "",
        precio: "",
        stock: "",
        foto: null,
      },
      headers: [
        {text: "Nombre", value: "nombre", sortable: false, class: "blue-grey accent-3 white--text", align: "center",},
        {text: "Imagen", value: "foto", sortable: false, class: "blue-grey accent-3 white--text", align: "center",},
        {text: "Precio", value: "precio", sortable: false, class: "blue-grey accent-3 white--text", align: "center",},
        {text: "Cantidad", value: "stock", sortable: false, class: "blue-grey accent-3 white--text", align: "center",},
        {text: "Acciones", value: "actions", sortable: false, class: "blue-grey accent-3 white--text", align: "center",},
      ],
    };
  },
  computed: {
    iva() {
      return this.sub_total * 0.12;  
    },
    total_pagar() {
      return (this.iva + this.sub_total).toFixed(2);
    }
  },
  mounted () {
    
    this.carrito_indices = this.$store.state.indiceCarrito;
    console.log(this.carrito_indices)
    this.cargarInfo()
  },
  methods: {    
    cargarInfo(){
      this.carrito_indices.forEach(indice => {
      axios.get("http://localhost:8080/productos/getproductscarrito/" + indice).then((response) => {
        this.carrito_list.push(response.data);
        this.sub_total += response.data.precio;
      });

    
      })
    },
    realizarPago(){
      console.log("Tienes que pagar: " + this.sub_total)
    }
  },

  

}
</script>