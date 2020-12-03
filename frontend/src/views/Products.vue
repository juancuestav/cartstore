<template>
  <v-container fluid class="pa-8">
    <v-row>
      <v-col cols="12" md="5" xs="12">
        <v-card class="elevation-5 pa-5">
          <v-card-title>Registro/Actualización de productos</v-card-title>
          <v-card-text>
            <v-form>
              <v-text-field
                label="Nombre"
                color="blue-grey accent-3"
              ></v-text-field>
              <v-row>
                <v-col cols="12" md="6" xs="12">
                  <v-textarea
                    autocomplete="email"
                    label="Descripción"
                    rows="3"
                    color="blue-grey accent-3"
                  ></v-textarea>
                </v-col>
                <v-col
                  cols="12"
                  md="6"
                  xs="12"
                  class="d-flex flex-column align-center"
                >
                  <v-img :src="imageUrl" max-width="145" class="mb-1"></v-img>
                  <v-btn
                    max-height="20"
                    block
                    x-small
                    color="grey lighten-2 accent-3"
                    @click="onPickFile"
                    class="mb-5"
                    >Subir imagen</v-btn
                  >
                  <input
                    type="file"
                    style="display: none"
                    ref="fileInput"
                    accept="image/*"
                    @change="onFilePicked"
                  />
                </v-col>
              </v-row>
              <v-row style="margin-top: -10px">
                <v-col>
                  <v-text-field
                    label="Precio"
                    type="number"
                    color="blue-grey accent-3"
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    label="Stock"
                    type="number"
                    color="blue-grey accent-3"
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-btn color="green lighten-2 accent-3" text @click="guardar">
              Guardar
            </v-btn>
            <v-btn color="light-blue darken-1 accent-2" text> Limpiar </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>

      <v-col cols="12" md="7" xs="12">
        <v-card>
          <v-card-title>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
              color="blue-grey accent-3"
            ></v-text-field>
          </v-card-title>
          <v-data-table
            :headers="headers"
            :items="product_list"
            :search="search"
          >
            <template v-slot:item.actions="{ item }">
              <v-icon class="mr-2" color="blue darken-2"> mdi-pencil </v-icon>
              <v-icon color="red"> mdi-delete </v-icon>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      imageUrl: "",
      image_file: "",

      search: "",
      product_list: [
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
          align: "center",
        },
        {
          text: "Imagen",
          value: "prod_image",
          align: "center",
          sortable: false,
        },
        {
          text: "Precio",
          value: "prod_precio",
          align: "center",
          sortable: false,
        },
        {
          text: "Stock",
          value: "prod_stock",
          align: "center",
          sortable: false,
        },
        {
          text: "Acciones",
          value: "actions",
          align: "center",
          sortable: false,
        },
      ],
    };
  },
  methods: {
    onPickFile() {
      this.$refs.fileInput.click();
    },
    onFilePicked(event) {
      // Almacena archivo en variable
      this.image_file = this.$refs.fileInput.files[0];

      let filename = this.image_file.name;
      if (filename.lastIndexOf(".") <= 0) {
        return alert("Por favor carga una imagen");
      }
      const fileReader = new FileReader();
      fileReader.addEventListener("load", () => {
        this.imageUrl = fileReader.result;
      });
      fileReader.readAsDataURL(this.image_file);
    },
    guardar() {
      let formData = new FormData();

      formData.append("file", this.image_file);
    },
  },
};
</script>
