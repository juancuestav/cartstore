<template>
  <div>
    <v-container fluid>
      <v-container>
        <v-row>
          <v-col cols="11">
            <h1>Tus direcciones</h1>
          </v-col>
          <v-col cols="1">
            <v-btn
              small
              fab
              dark
              color="blue-grey accent-3"
              @click="
                dialogDireccion = true;
                titleDialogDireccion = 'Registrar';
                limpiarForm();
              "
            >
              <v-icon dark> mdi-plus </v-icon>
            </v-btn>
          </v-col>
        </v-row>
        <v-layout wrap>
          <v-flex v-for="direccion in direcciones" :key="direccion.id">
            <v-card
              class="mx-auto"
              min-width="320"
              max-width="320"
              style="margin: 10px"
            >
              <v-card-text>
                <div>
                  <FONT SIZE="3"
                    ><strong>Dirección: </strong>
                    {{ direccion.dir_direccion }}</FONT
                  >
                </div>
                <div>
                  <FONT SIZE="3"
                    ><strong>Referencia: </strong>
                    {{ direccion.dir_referencia }}</FONT
                  >
                </div>
                <div>
                  <FONT SIZE="3"
                    ><strong>Teléfono: </strong>
                    {{ direccion.dir_telefono }}</FONT
                  >
                </div>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions class="justify-center">
                <v-btn
                  color="blue"
                  text
                  small
                  @click="cargarDireccionAeditar(direccion)"
                >
                  Editar
                </v-btn>
                <v-btn
                  color="red"
                  text
                  small
                  @click="eliminar(direccion.dir_id)"
                >
                  Eliminar
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-container>

    <!-- REGISTRO DE DIRECCIONES -->
    <v-dialog v-model="dialogDireccion" width="500">
      <v-card class="elevation-12 mx-auto">
        <v-row class="ml-0 mr-0">
          <v-col cols="12" md="12" class="white">
            <v-card-text>
              <h1 class="text-center display-1 blue-grey--text text--accent-3">
                {{ titleDialogDireccion }} dirección
              </h1>
              <v-form class="px-8 pt-2">
                <v-text-field
                  v-model="direccion.dir_direccion"
                  label="Dirección"
                  color="blue-grey accent-3"
                ></v-text-field>
                <v-text-field
                  v-model="direccion.dir_referencia"
                  label="Referencia"
                  color="blue-grey accent-3"
                ></v-text-field>
                <v-text-field
                  v-model="direccion.dir_telefono"
                  label="Teléfono"
                  color="blue-grey accent-3"
                  v-on:keyup.enter="agregar"
                ></v-text-field>
              </v-form>
            </v-card-text>
            <div v-if="titleDialogDireccion == 'Registrar'">
              <div class="text-center">
                <v-btn color="blue-grey accent-3" dark @click="agregar"
                  >Agregar</v-btn
                >
              </div>
            </div>
            <div v-else>
              <div class="text-center">
                <v-btn color="blue-grey accent-3" dark @click="actualizar"
                  >Actualizar</v-btn
                >
              </div>
            </div>
          </v-col>
        </v-row>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import Direccion from "../models/direccion";
import DireccionService from "../services/direccion.service";

export default {
  name: "Carrito",

  data() {
    return {
      direccion: new Direccion("", "", ""),
      dialogDireccion: false,
      titleDialogDireccion: "",
      valid: false,
      direcciones: [],
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  mounted() {
    this.cargarDirecciones();
  },
  methods: {
    closeDialog() {
      this.dialogDireccion = false;
    },
    agregar() {
      if (
        this.direccion.dir_direccion &&
        this.direccion.dir_referencia &&
        this.direccion.dir_telefono
      ) {
        this.direccion.dir_usu_id = this.currentUser.id;
        DireccionService.add_direccion(this.direccion).then((response) => {
          if (response.data.error) {
            this.showError({ message: response.data.error });
          } else {
            this.showSuccess({ message: response.data.exito });
            this.closeDialog();
            this.limpiarForm();
            this.cargarDirecciones();
          }
        });
      } else {
        this.showWarning({ message: "Llene los campos del formulario" });
      }
    },
    cargarDireccionAeditar(direccionCargar) {
      this.dialogDireccion = true;
      this.titleDialogDireccion = "Actualizar";
      this.direccion.dir_direccion = direccionCargar.dir_direccion;
      this.direccion.dir_referencia = direccionCargar.dir_referencia;
      this.direccion.dir_telefono = direccionCargar.dir_telefono;
      this.direccion.dir_id = direccionCargar.dir_id;
    },
    actualizar() {
      DireccionService.update_direccion(this.direccion).then((response) => {
        if (response.data.error) {
          this.showError({ message: response.data.error });
        } else {
          this.showSuccess({ message: response.data.exito });
          this.closeDialog();
          this.limpiarForm();
          this.cargarDirecciones();
        }
      });
    },
    eliminar(dir_id) {
      DireccionService.delete_direccion(dir_id).then((response) => {
        if (response.data.error) {
          this.showError({ message: response.data.error });
        } else {
          this.showSuccess({ message: response.data.exito });
          this.cargarDirecciones();
        }
      });
    },
    limpiarForm() {
      this.direccion = new Direccion("", "", "");
    },
    cargarDirecciones() {
      DireccionService.get_direcciones(this.currentUser.id).then((response) => {
        this.direcciones = response.data;
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

<style>
</style>