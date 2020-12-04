<template>
  <v-container fluid style="padding-left: 50px; padding-right: 50px">
    <v-row>
      <v-col md="8">
        <v-card>
          <v-data-table
            :headers="headers"
            :items="carrito_list"
            hide-default-footer
          >
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
                <v-img
                  :src="`${servidor}${item.foto}`"
                  max-width="100px"
                  contain
                  style="margin: 10px 0px"
                ></v-img>
              </div>
            </template>

            <template v-slot:item.cantidad="{ item }">
              <div class="px-2 d-flex flex-column align-center">
                <v-text-field
                  type="number"
                  v-model="item.cantidad_comprar"
                  :rules="[
                    rules.campoVacio(item.cantidad_comprar),
                    rules.soloPositivos(item.cantidad_comprar),
                  ]"
                  error-count="2"
                  @click="
                    calculoSubtotalXprod(
                      item.cantidad_comprar,
                      item.precio,
                      item.id
                    )
                  "
                ></v-text-field>
              </div>
            </template>

            <template v-slot:item.actions="{ item }">
              <v-icon color="red" @click="eliminarDeCarrito(item.id)">
                mdi-delete
              </v-icon>
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
                    sub_total.toFixed(2)
                  }}</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title>IVA 12%:</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title class="blue-grey lighten-5">{{
                    iva.toFixed(2)
                  }}</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title>Total a Pagar:</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title class="blue-grey lighten-5">{{
                    total_pagar.toFixed(2)
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
                  <v-btn
                    :to="{ name: 'Envio' }"
                    @click="realizarPago"
                    color="blue"
                    class="ma-2 white--text"
                    block
                    :disabled="!loggedIn"
                    >Realizar Pago</v-btn
                  >
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
        {
          text: "Nombre",
          value: "nombre",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Imagen",
          value: "foto",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Precio",
          value: "precio",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Cantidad",
          value: "cantidad",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Subtotal por producto",
          value: "subtotalXprod",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Acciones",
          value: "actions",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
      ],
      rules: {
        campoVacio: (texto) => !!texto || "Campo requerido!",
        soloNumeros: (texto) =>
          Number.isInteger(Number(texto)) || "Ingresar sólo números",
        soloPositivos: (texto) =>
          Number(texto) > 0 || "No se permiten cantidades negativas",
      },
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
    iva() {
      return this.sub_total * 0.12;
    },
    total_pagar() {
      return this.iva + this.sub_total;
    },
    datosCarrito() {
      return this.$store.state.carrito_data_producto;
    },
  },
  mounted() {
    this.carrito_indices = this.$store.state.indiceCarrito;
    this.cargarInfo();
    this.$store.state.carrito_data_producto = this.carrito_list;
  },
  methods: {
    cargarInfo() {
      if (this.carrito_indices.length == this.datosCarrito.length) {
        this.carrito_list = this.datosCarrito;
        this.sub_total = 0;
        for (let index = 0; index < this.carrito_list.length; index++) {
          this.sub_total =
            Number(this.sub_total) +
            Number(this.carrito_list[index].subtotalXprod);
        }
      } else if (this.datosCarrito.length == 0) {
        this.carrito_indices.forEach((indice) => {
          axios
            .get("http://localhost:8080/productos/getproductscarrito/" + indice)
            .then((response) => {
              response.data.cantidad_comprar = 1;
              response.data.subtotalXprod =
                response.data.precio * response.data.cantidad_comprar;
              this.carrito_list.push(response.data);
              this.sub_total += response.data.precio;
            });
        });
      } else if (this.carrito_indices.length > this.datosCarrito.length) {
        for (let index = 0; index < this.carrito_indices.length; index++) {
          let obj = 0;
          obj = this.datosCarrito.find(
            (o) => o.id === this.carrito_indices[index]
          );
          if (obj == undefined) {
            this.carrito_list = this.datosCarrito;
            axios
              .get(
                "http://localhost:8080/productos/getproductscarrito/" +
                  this.carrito_indices[index]
              )
              .then((response) => {
                response.data.cantidad_comprar = 1;
                response.data.subtotalXprod =
                  response.data.precio * response.data.cantidad_comprar;
                this.carrito_list.push(response.data);
                this.sub_total += response.data.precio;
              });
          }
        }
      }
    },
    eliminarDeCarrito(prod_id) {
      const index = this.carrito_indices.indexOf(prod_id);
      this.carrito_indices.splice(index, 1);
      this.carrito_list.splice(index, 1);
      this.sub_total = 0;
      for (let index = 0; index < this.carrito_list.length; index++) {
        this.sub_total =
          Number(this.sub_total) +
          Number(this.carrito_list[index].subtotalXprod);
      }
    },
    calculoSubtotalXprod(cantidad, precio, prod_id) {
      this.sub_total = 0;
      for (let index = 0; index < this.carrito_list.length; index++) {
        if (this.carrito_list[index].id == prod_id) {
          this.carrito_list[index].subtotalXprod = (cantidad * precio).toFixed(
            2
          );
        }
        this.sub_total =
          Number(this.sub_total) +
          Number(this.carrito_list[index].subtotalXprod);
      }
    },
    realizarPago() {
      console.log("Tienes que pagar: " + this.sub_total);
    },
    realizarPago(){
      console.log("Tienes que pagar: " + this.total_pagar)
    }
  },
};
</script>