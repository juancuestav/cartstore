<template>
  <v-container>
    <v-layout wrap>
      <v-flex v-for="producto in lista_productos" :key="producto.id">

        <v-card class="mx-auto" max-width="340" style="margin: 10px">

          <v-card-title>
            <v-row class="mx-1">
              <v-icon class="black--text" medium left >mdi-store</v-icon>
              <span class="title black--text">{{producto.nombre}}</span>
              <v-spacer></v-spacer>
              <v-icon class="black--text" medium left >mdi-arrow-up-bold-box-outline</v-icon>
              <span class="title">{{producto.stock}}</span>
            </v-row>
          </v-card-title>

          <v-card-text>
            <v-img contain height="130" :src=producto.foto></v-img>
            <div class="mt-6 mx-1 text-justify">
              {{ producto.descripcion }}
            </div>
          </v-card-text>

          <v-card-actions class="justify-center">
            <v-btn color="blue" outlined > 
              Agregar al carrito <v-icon medium right >mdi-cart</v-icon>
            </v-btn>
            <v-btn color="error" depressed> Comprar </v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      servidor: "http://localhost:8080/",
      imagen: null,
      lista_productos: [],
    };
  },
  created: function() {
    axios.get("http://localhost:8080/productos/listar").then((response) => {
      this.lista_productos = response.data.filter((p) => p != null);
      console.log(response.data);
      this.lista_productos.forEach((elem) => {
        elem.foto = this.servidor + elem.foto;
        console.log(response.data);
      });
    });
  }
};
</script>
