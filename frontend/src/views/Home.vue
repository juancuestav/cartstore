<template>
  <v-container>
    <v-layout wrap>
      <v-flex v-for="producto in lista_productos" :key="producto.id">
        <v-card class="mx-auto" max-width="340" style="margin: 10px">
          <v-card-title>
            <v-row style="margin: 0px 2px">
              <v-icon class="black--text" medium left style="margin-right: 0px"
                >mdi-store</v-icon
              >
              <h5 class="black--text">{{ producto.nombre }}</h5>
              <v-spacer></v-spacer>
              <v-icon class="black--text" medium left style="margin-right: 0px"
                >mdi-arrow-up-bold-box-outline</v-icon
              >
              <h5>{{ producto.stock }}</h5>
            </v-row>
          </v-card-title>

          <v-card-text>
            <v-img contain height="130" :src="producto.foto"></v-img>
            <div class="mt-6 mx-1 text-justify">
              {{ producto.descripcion }}
            </div>
          </v-card-text>

          <div v-if="currentDataUser.us_rol != 'ADMINISTRADOR'">
            <v-card-actions class="justify-center">
              <div v-if="!carrito.includes(producto.id)">
                <v-btn
                  small
                  color="blue"
                  outlined
                  @click="add_cart(producto.id)"
                >
                  Agregar al carrito <v-icon medium right>mdi-cart</v-icon>
                </v-btn>
              </div>
              <div v-else>
                <v-btn small color="green" outlined>
                  Añadido al carrito
                  <v-icon medium right>mdi-cart-check</v-icon>
                </v-btn>
              </div>
              <v-btn
                small
                color="error"
                depressed
                style="margin-left: 5px"
                @click="comprarAcarrito(producto.id)"
              >
                Comprar
              </v-btn>
            </v-card-actions>
          </div>
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
  computed: {
    currentDataUser() {
      return this.$store.state.datosUsuario;
    },
    carrito() {
      return this.$store.state.indiceCarrito;
    },
  },
  methods: {
    add_cart(id_prod) {
      if (!this.carrito.includes(id_prod)) {
        this.carrito.push(id_prod);
      }
    },
    comprarAcarrito(id_prod) {
      if (!this.carrito.includes(id_prod)) {
        this.carrito.push(id_prod);
      }
      this.$router.push({ name: "Carshop" });
    },
  },
  created: function () {
    axios.get("http://localhost:8080/productos/listar").then((response) => {
      this.lista_productos = response.data.filter((p) => p != null);
      this.lista_productos.forEach((elem) => {
        elem.foto = this.servidor + elem.foto;
      });
    });
  },
};
</script>
