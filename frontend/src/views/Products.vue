<template>
  <v-container>
    <v-data-table
      :headers="headers"
      :items="lista_productos"
      :items-per-page="5"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Productos</v-toolbar-title>

          <v-divider class="mx-4" inset vertical></v-divider>

          <v-spacer></v-spacer>

          <!-- Dialogo con formulario para crear productos-->
          <v-dialog v-model="dialog" max-width="500px" eager>
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on"
                >Nuevo producto</v-btn
              >
            </template>

            <v-card class="elevation-12 mx-auto">
              <v-row class="ml-0 mr-0">
                <v-col cols="12" md="12" class="white">
                  <v-card-text>
                    <h1
                      class="text-center display-1 blue-grey--text text--accent-3"
                    >
                      {{ formTitle }} producto
                    </h1>
                    <v-form ref="formProduct" v-model="isValidFormProduct">
                      <v-text-field
                        color="blue-grey accent-3"
                        v-model="editedItem.nombre"
                        :rules="[rules.campoVacio(editedItem.nombre)]"
                        label="Nombre"
                      ></v-text-field>
                      <v-textarea
                        v-model="editedItem.descripcion"
                        :rules="[rules.campoVacio(editedItem.descripcion)]"
                        autocomplete="email"
                        label="Descripción"
                        rows="2"
                        color="blue-grey accent-3"
                      ></v-textarea>

                      <v-row>
                        <v-col cols="12" md="6">
                          <v-text-field
                            color="blue-grey accent-3"
                            v-model="editedItem.precio"
                            :rules="[
                              rules.campoVacio(editedItem.precio),
                              rules.soloPositivos(editedItem.precio),
                            ]"
                            error-count="3"
                            label="Precio"
                            type="number"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" md="6">
                          <v-text-field
                            color="blue-grey accent-3"
                            v-model="editedItem.stock"
                            :rules="[
                              rules.campoVacio(editedItem.stock),
                              rules.soloPositivos(editedItem.stock),
                            ]"
                            error-count="3"
                            label="Stock"
                            type="number"
                          ></v-text-field>
                        </v-col>
                      </v-row>

                      <!-- SI ES REGISTRAR SE VALIDA QUE SE CARGUE LA IMAGEN -->
                      <div v-if="formTitle == 'Registrar'">
                        <v-row>
                          <v-col cols="12" md="6">
                            <v-file-input
                              color="blue-grey accent-3"
                              v-model="editedItem.foto"
                              :rules="[rules.campoVacio(editedItem.foto)]"
                              type="file"
                              accept="image/*"
                              label="Subir foto"
                              @change="obtenerImagen"
                            ></v-file-input>
                          </v-col>
                          <v-col
                            cols="12"
                            md="6"
                            class="d-flex flex-column align-center"
                          >
                            <v-img
                              contain
                              :src="imageUrl"
                              max-width="145"
                              max-height="150"
                              class="d-flex align-center"
                            ></v-img>
                          </v-col>
                        </v-row>
                      </div>

                      <!-- SI ES EDITAR, NO SE VALIDA QUE SE CARGUE LA IMAGEN -->
                      <div v-else>
                        <v-row>
                          <v-col cols="12" md="6">
                            <v-file-input
                              color="blue-grey accent-3"
                              v-model="editedItem.foto"
                              type="file"
                              accept="image/*"
                              label="Subir nueva foto"
                              @change="obtenerImagen"
                            ></v-file-input>
                          </v-col>
                          <v-col
                            cols="12"
                            md="6"
                            class="d-flex flex-column align-center"
                          >
                            <v-img
                              contain
                              :src="imageUrl"
                              max-width="145"
                              max-height="150"
                              class="d-flex align-center"
                            ></v-img>
                          </v-col>
                        </v-row>
                      </div>
                    </v-form>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="close"
                      >Cancelar</v-btn
                    >
                    <v-btn
                      color="green lighten-2 accent-3"
                      text
                      @click="guardar"
                      :disabled="!isValidFormProduct"
                      >Guardar</v-btn
                    >
                  </v-card-actions>
                </v-col>
              </v-row>
            </v-card>
          </v-dialog>
          <!-- FIN: Dialogo con formulario para crear productos-->

          <!-- Dialogo para confirmar eliminacion -->
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="headline"
                >Are you sure you want to delete this item?</v-card-title
              >
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete"
                  >Cancelar</v-btn
                >
                <v-btn
                  color="green lighten-2 accent-3"
                  text
                  @click="deleteItemConfirm(prod_id_to_delete)"
                  >Eliminar</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <!-- FIN: Dialogo para confirmar eliminacion -->
        </v-toolbar>
      </template>

      <template v-slot:item.image="{ item }">
        <div class="p-2 d-flex flex-column align-center">
          <v-img
            :src="`${servidor}${item.foto}`"
            max-width="100px"
            contain
            style="margin: 10px 0px"
          ></v-img>
        </div>
      </template>
      <template v-slot:item.actions="{ item }">
        <v-icon class="mr-2" @click="cargarInfoProduct(item)"
          >mdi-pencil</v-icon
        >
        <v-icon @click="deleteItem(item.id)"> mdi-delete</v-icon>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import ProductService from "../services/products.service";

export default {
  data() {
    return {
      servidor: "http://localhost:8080/",
      dialog: false,
      dialogDelete: false,
      editedIndex: -1,
      imageUrl: "",
      prod_id_to_delete: 0,
      editedItem: {
        nombre: "",
        descripcion: "",
        precio: "",
        stock: "",
        foto: null,
      },
      defaultItem: {
        nombre: "",
        descripcion: "",
        precio: "",
        stock: "",
        foto: null,
      },
      search: "",
      lista_productos: [],
      headers: [
        { text: "Nombre", value: "nombre", align: "center" },
        { text: "Foto", value: "image", align: "center" },
        { text: "Precio", value: "precio", align: "center" },
        { text: "Stock", value: "stock", align: "center" },
        {
          text: "Acciones",
          value: "actions",
          align: "center",
          sortable: false,
        },
      ],

      rules: {
        campoVacio: (texto) => !!texto || "Campo requerido!",
        soloNumeros: (texto) =>
          Number.isInteger(Number(texto)) || "Ingresar sólo números",
        soloNumerosYDecimal: (texto) =>
          Number.isInteger(Number(texto)) || "Ingresar sólo números",
        soloPositivos: (texto) =>
          Number(texto) >= 0 || "No se permiten cantidades negativas",
      },

      isValidFormProduct: true,
    };
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "Registrar" : "Editar";
    },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  mounted() {
    this.listarProductos();
  },

  methods: {
    listarProductos() {
      this.lista_productos = [];
      setTimeout(() => {
        ProductService.get_products().then((response) => {
          response.data.forEach((producto) => {
            this.lista_productos.push(producto);
          });
        });
      }, 1000);
    },
    // Muestra la imagen cargada en un v-img
    obtenerImagen(event) {
      if (event != undefined) {
        let filename = this.editedItem.foto.name;
        if (filename.lastIndexOf(".") <= 0) {
          return alert("Por favor carga una imagen");
        }
        const fileReader = new FileReader();
        fileReader.addEventListener("load", () => {
          this.imageUrl = fileReader.result;
        });
        fileReader.readAsDataURL(this.editedItem.foto);
      } else {
        this.imageUrl = "";
      }
    },

    // ENVIA solicitud POST a servidor
    async guardar() {
      if (this.formTitle == "Registrar") {
        let formdata = new FormData();
        formdata.append("nombre", this.editedItem.nombre);
        formdata.append("descripcion", this.editedItem.descripcion);
        formdata.append("precio", this.editedItem.precio);
        formdata.append("stock", this.editedItem.stock);
        formdata.append("foto", this.editedItem.foto);

        let response = await ProductService.add_product(formdata);
        if (response.status == 200) {
          this.close();
          this.showSuccess({ message: response.data });
          this.$refs.formProduct.resetValidation();
          this.listarProductos();
        }
      } else {
        let formdata = new FormData();
        formdata.append("nombre", this.editedItem.nombre);
        formdata.append("descripcion", this.editedItem.descripcion);
        formdata.append("precio", this.editedItem.precio);
        formdata.append("stock", this.editedItem.stock);
        formdata.append("foto", this.editedItem.foto);
        console.log(formdata.get("foto"));

        ProductService.update_product(this.editedItem.id, formdata).then(
          (response) => {
            if (response.status == 200) {
              this.showSuccess({ message: response.data });
              this.close();
              this.listarProductos();
            }
          }
        );
      }
    },

    // Muestra dialogo principal y guarda en memoria el indice del producto al que hizo clic
    cargarInfoProduct(item) {
      this.editedIndex = this.lista_productos.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.imageUrl = `${this.servidor}${this.editedItem.foto}`;
      this.editedItem.foto = null;
      this.dialog = true;
    },

    // Muestra dialogo ELIMINAR y guarda en memoria el indice del producto al que hizo clic
    deleteItem(prod_id) {
      /*this.editedIndex = this.lista_productos.indexOf(item);
      this.editedItem = Object.assign({}, item);*/
      this.prod_id_to_delete = prod_id;
      this.dialogDelete = true;
    },

    // ELIMINA producto de la tabla de acuerdo a su indice
    deleteItemConfirm() {
      ProductService.delete_product(this.prod_id_to_delete).then((response) => {
        if (response.status == 200) {
          this.showSuccess({ message: response.data });
          this.closeDelete();
          this.listarProductos();
        }
      });
    },

    // Cierra dialogo y resetea los valores de editedItem
    close() {
      this.dialog = false;
      this.$refs.formProduct.resetValidation();
      this.imageUrl = "";
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    // Cierra dialogo ELIMINAR producto y resetea los valores de editedItem
    closeDelete() {
      this.dialogDelete = false;
      this.prod_id_to_delete = 0;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
  },
  notifications: {
    showSuccess: {
      title: "Éxito",
      message: "",
      type: "success",
      timeout: 3000,
    },
    showError: { title: "Error", message: "", type: "error", timeout: 3000 },
    showWarning: { title: "Aviso", message: "", type: "warn", timeout: 3000 },
  },
};
</script>
