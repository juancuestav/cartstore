<template>
  <v-container>
    <v-row>
      <v-col md="8">
        <v-card>
          <v-data-table
            :headers="headers"
            :items="carrito_list"
            hide-default-footer
          >
            <template v-slot:item.prod_image="{ item }">
              <div class="pa-2 d-flex flex-column align-center">
                <v-img
                  src="https://cdn.vuetifyjs.com/images/cards/cooking.png"
                  height="120px"
                  width="150px"
                ></v-img>
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
                  <v-toolbar-title class="blue-grey lighten-5"
                    >0.00</v-toolbar-title
                  >
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title>Descuento:</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title class="blue-grey lighten-5"
                    >0.00</v-toolbar-title
                  >
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title>Total a Pagar:</v-toolbar-title>
                </v-col>
                <v-col cols="12">
                  <v-toolbar-title class="blue-grey lighten-5"
                    >0.00</v-toolbar-title
                  >
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
                    color="blue-grey"
                    class="ma-2 white--text"
                    block
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
export default {
  data() {
    return {
      carrito_list: [
        {
          prod_name: "Producto 1",
          prod_image: "",
          prod_precio: 399.99,
          prod_stock: 100,
        },
      ],
      headers: [
        {
          text: "Nombre",
          value: "prod_name",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Imagen",
          value: "prod_image",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Precio",
          value: "prod_precio",
          sortable: false,
          class: "blue-grey accent-3 white--text",
          align: "center",
        },
        {
          text: "Stock",
          value: "prod_stock",
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
    };
  },
};
</script>