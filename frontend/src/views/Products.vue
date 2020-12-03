<template>
  <v-container>
    <!--<v-row>
      <v-col cols="12" md="5" class="pa-4">
        
        <v-form class="elevation-1">
          <h4 pa-4>Registro/Actualización de productos</h4>
          <v-text-field v-model="nombre" label="Nombre"></v-text-field>
          <v-textarea v-model="descripcion" autocomplete="email" label="Descripción" rows="3"></v-textarea>
          
          <v-row>
            <v-col cols="12" md="6">
              <v-text-field v-model="precio" label="Precio" type="number" ></v-text-field>
            </v-col>
            <v-col cols="12" md="6">
              <v-text-field v-model="stock" label="Stock" type="number" ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" md="6">
              <v-file-input v-model="foto" type="file" accept="image/*" label="Subir foto" @change="obtenerImagen"></v-file-input>
            </v-col>
            <v-col cols="12" md="6" align-center>
              <v-img :src="imageUrl" max-width="145" max-height="120" class="d-flex align-center"></v-img>
            </v-col>
          </v-row>
          
          <v-btn color="green lighten-2 accent-3" text @click="guardar">Guardar</v-btn>
          <v-btn color="light-blue darken-1 accent-2" text> Limpiar </v-btn>

        </v-form>

      </v-col>

      <v-col cols="12" md="7" xs="12">
        
            <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" single-line hide-details color="blue-grey accent-3"></v-text-field>
          
          <v-data-table :headers="headers" :items="lista_productos" class="elevation-1">
            <v-dialog v-model="dialog" max-width="500px" >
              <template v-slot:activator="{ on, attrs }">
                <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">New Item</v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-text-field
                      v-model="editedItem.name"
                      label="Dessert name"
                    ></v-text-field>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-text-field
                      v-model="editedItem.calories"
                      label="Calories"
                    ></v-text-field>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-text-field
                      v-model="editedItem.fat"
                      label="Fat (g)"
                    ></v-text-field>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-text-field
                      v-model="editedItem.carbs"
                      label="Carbs (g)"
                    ></v-text-field>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                    <v-text-field
                      v-model="editedItem.protein"
                      label="Protein (g)"
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue darken-1"
                text
                @click="close"
              >
                Cancel
              </v-btn>
              <v-btn
                color="blue darken-1"
                text
                @click="save"
              >
                Save
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
            <template v-slot:item.actions="{ item }">
              <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
              <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
            </template>
          </v-data-table>
       
      </v-col>

    </v-row>-->

  <v-data-table :headers="headers" :items="lista_productos"  class="elevation-1" >
    <template v-slot:top>

      <v-toolbar flat >

        <v-toolbar-title>Productos</v-toolbar-title>

        <v-divider class="mx-4" inset vertical ></v-divider>

        <v-spacer></v-spacer>

        <!-- Dialogo con formulario para crear productos-->
        <v-dialog v-model="dialog" max-width="500px" >

          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">Nuevo producto</v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-text-field v-model="editedItem.nombre" label="Nombre"></v-text-field>
                <v-textarea v-model="editedItem.descripcion" autocomplete="email" label="Descripción" rows="3"></v-textarea>
          
                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field v-model="editedItem.precio" label="Precio" type="number" ></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field v-model="editedItem.stock" label="Stock" type="number" ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6">
                    <v-file-input v-model="editedItem.foto" type="file" accept="image/*" label="Subir foto" @change="obtenerImagen"></v-file-input>
                  </v-col>
                  <v-col cols="12" md="6" align-center>
                    <v-img :src="imageUrl" max-width="145" max-height="120" class="d-flex align-center"></v-img>
                  </v-col>
                </v-row>
          
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="close" >Cancelar</v-btn>
              <v-btn color="green lighten-2 accent-3" text @click="save" >Guardar</v-btn>
            </v-card-actions>
            
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="headline">Are you sure you want to delete this item?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Cancelar</v-btn>
              <v-btn color="green lighten-2 accent-3" text @click="deleteItemConfirm">Eliminar</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>

      </v-toolbar>

    </template>

    <template v-slot:item.actions="{ item }">
      <v-icon small class="mr-2" @click="editItem(item)" >mdi-pencil</v-icon>
      <v-icon small @click="deleteItem(item)"> mdi-delete</v-icon>
    </template>
    
  </v-data-table>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      dialog: false,
      dialogDelete: false,
      editedIndex: -1,
      imageUrl: "",
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
        { text: "Precio", value: "precio", align: "center" },
        { text: "Stock", value: "stock", align: "center"},
        { text: "Acciones", value: "actions", align: "center", sortable: false},
      ],
    };
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Nuevo producto' : 'Editar producto'
    },
  },

  watch: {
    dialog (val) {
      val || this.close()
    },
    dialogDelete (val) {
      val || this.closeDelete()
    },
  },

  created: function () {
    axios.get("http://localhost:8080/productos/listar").then((response) => {
      this.lista_productos = response.data.filter((p) => p != null);
      this.lista_productos.forEach((elem) => {
        console.log(elem);
      });
    });
  },

  methods: {
    // Muestra la imagen cargada en un v-img
    obtenerImagen(event) {
      let filename = this.editedItem.foto.name;
      if (filename.lastIndexOf(".") <= 0) {
        return alert("Por favor carga una imagen");
      }
      const fileReader = new FileReader();
      fileReader.addEventListener("load", () => {
        this.imageUrl = fileReader.result;
      });
      fileReader.readAsDataURL(this.editedItem.foto);
    },

    // ENVIA solicitud POST a servidor
    guardar() {
      var formdata = new FormData();
      formdata.append("nombre", this.editedItem.nombre);
      formdata.append("descripcion", this.editedItem.descripcion);
      formdata.append("precio", this.editedItem.precio);
      formdata.append("stock", this.editedItem.stock);
      formdata.append("foto", this.editedItem.foto);

      axios
        .post("http://localhost:8080/productos/guardar", formdata)
        .then((response) => {
          console.log("Guardado");
        });
    },

    eliminar_axios() {
      this.$nextTick(() => {
      console.log('Indice a del: ' + this.editedItem.id);
      axios
        .delete("http://localhost:8080/productos/eliminar/" + this.editedItem.id)
        .then((response) => {
          console.log("Eliminado!");
        });
      })
    },

    // Muestra dialogo principal y guarda en memoria el indice del producto al que hizo clic
    editItem (item) {
      console.log("Estas editando")
      console.log(item)
      this.editedIndex = this.lista_productos.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    // Muestra dialogo ELIMINAR y guarda en memoria el indice del producto al que hizo clic
    deleteItem (item) {
        this.editedIndex = this.lista_productos.indexOf(item);
        this.editedItem = Object.assign({}, item);
        this.dialogDelete = true;
    },

    // ELIMINA producto de la tabla de acuerdo a su indice
    deleteItemConfirm () {
      this.lista_productos.splice(this.editedIndex, 1);
      this.closeDelete();
      
      console.log("Item a eliminar2");
      console.log(this.editedItem.id);
      this.eliminar_axios();
    },

    // Cierra dialogo y resetea los valores de editedItem
    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem) 
        this.editedIndex = -1
      })
    },

    // Cierra dialogo ELIMINAR producto y resetea los valores de editedItem
    closeDelete () {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    // GUARDA un producto en la tabla
    save () {
      console.log("Dentro de save");
      console.log(this.editedItem)

      if (this.editedIndex > -1) {
        Object.assign(this.lista_productos[this.editedIndex], this.editedItem);
      } else {
        this.lista_productos.push(this.editedItem);
        this.guardar();
      }
      this.close()
      
    },
  },
};
</script>
