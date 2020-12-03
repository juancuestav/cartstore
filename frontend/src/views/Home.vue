<template>
  <v-container>
    <v-layout wrap>
      <v-flex v-for="producto in lista_productos" :key="producto.nombre">
        <v-card class="mx-auto" max-width="320" style="margin: 10px">
          <!-- <v-img height="200" src="https://cdn.vuetifyjs.com/images/cards/cooking.png"></v-img> -->
          <v-img height="200" :src=producto.foto></v-img>

          <v-card-title>
            <v-row class="mx-5">
              <span class="title blue--text">Producto</span>
              <v-spacer></v-spacer>
              <span class="title"> $ 0.00</span>
            </v-row>
          </v-card-title>

          <v-card-text>
            <div class="mx-5 text-justify">
              {{ producto.descripcion }}
            </div>
          </v-card-text>

          <v-card-actions class="justify-center">
            <v-btn color="blue-grey accent-3" text> Añadir a carrito </v-btn>
            <v-btn color="blue-grey accent-3" text> Comprar </v-btn>
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
      producto: {
        nombre: "",
        descripcion: "",
        precio: "",
        stock: "",
        foto: "",
      },
      books: [
        {
          prod_nombre: "book1",
          prod_precio: 3,
          prod_stock: "2016",
          prod_descripcion: "kjsahdjksagbdhasgdyuhasgdhabsdhasgvdhyasfdgahsygasyudg",
        },
        {
          prod_nombre: "book2",
          prod_precio: 3,
          prod_stock: "2016",
          prod_descripcion: "2016",
        },
        {
          prod_nombre: "book3",
          prod_precio: 3,
          prod_stock: "2016",
          prod_descripcion: "2016",
        },
        {
          prod_nombre: "book4",
          prod_precio: 3,
          prod_stock: "2016",
          prod_descripcion: "2016",
        },
        {
          prod_nombre: "book5",
          prod_precio: 3,
          prod_stock: "2016",
          prod_descripcion: "2016",
        },
        {
          prod_nombre: "book6",
          prod_precio: 3,
          prod_stock: "2016",
          prod_descripcion: "2016",
        },
      ],
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
