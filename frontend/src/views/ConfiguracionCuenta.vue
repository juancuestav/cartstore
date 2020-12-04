<template>
  <div>
    <v-card-text>
      <v-container>
        <v-row>
          <v-col cols="12" class="text-center">
            <h1>Mi cuenta</h1>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6" style="margin-top: 10px">
            <v-card>
              <v-card-text>
                <div class="text-center">
                  <v-avatar color="blue-grey accent-3" size="70">
                    <span class="white--text headline"
                      >{{ currentDataUser.us_nombres_abr
                      }}{{ currentDataUser.us_apellidos_abr }}</span
                    >
                  </v-avatar>
                </div>
                <br />
                <div>
                  <FONT SIZE="3"
                    ><strong>Cédula: </strong>
                    {{ currentDataUser.us_dni }}</FONT
                  >
                </div>
                <div>
                  <FONT SIZE="3"
                    ><strong>Nombres: </strong>
                    {{ currentDataUser.us_nombres }}
                    {{ currentDataUser.us_apellidos }}</FONT
                  >
                </div>
                <div>
                  <FONT SIZE="3"
                    ><strong>Correo electrónico: </strong>
                    {{ currentDataUser.us_email }}</FONT
                  >
                </div>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions>
                <v-btn
                  small
                  color="blue-grey"
                  class="white--text"
                  block
                  @click="cargarInformacion(currentDataUser)"
                  >Actualizar información</v-btn
                >
              </v-card-actions>
            </v-card>
          </v-col>
          <v-col cols="12" md="2"></v-col>
          <v-col cols="12" md="4">
            <v-row>
              <v-col cols="12">
                <v-card class="mx-auto" max-width="300">
                  <v-card-title
                    class="blue-grey lighten-4"
                    style="padding-top: 5px; padding-bottom: 5px"
                    >Tus direcciones</v-card-title
                  >
                  <v-divider></v-divider>
                  <v-card-text class="text--primary text-justify">
                    <div>Dirección de domicilio para recibir sus pedidos</div>
                  </v-card-text>
                  <v-card-actions style="margin-top: -10px">
                    <v-btn
                      small
                      :to="{ name: 'Direcciones' }"
                      block
                      class="white--text"
                      color="blue-grey accent-3"
                      >Ver</v-btn
                    >
                  </v-card-actions>
                </v-card>
              </v-col>
              <v-col cols="12">
                <div v-if="metodoPago.length == 0">
                  <v-card class="mx-auto" max-width="300">
                    <v-card-title
                      class="blue-grey lighten-4"
                      style="padding-top: 5px; padding-bottom: 5px"
                      >Método de pago</v-card-title
                    >
                    <v-divider></v-divider>
                    <v-card-text class="text--primary text-justify">
                      <div>
                        No posee un método de pago registrado, por favor ingrese
                        uno
                      </div>
                    </v-card-text>
                    <v-card-actions style="margin-top: -10px">
                      <v-btn
                        small
                        block
                        class="white--text"
                        color="blue-grey accent-3"
                        @click="
                          dialogPago = true;
                          titleDialogPago = 'Registrar';
                          buttonDialogPago = 'Agregar';
                          accionDialogPago = 'Registrar';
                        "
                        >Agregar pago</v-btn
                      >
                    </v-card-actions>
                  </v-card>
                </div>
                <div v-else>
                  <v-card class="mx-auto" max-width="300">
                    <v-card-title
                      class="blue-grey lighten-4"
                      style="padding-top: 5px; padding-bottom: 5px"
                      >Método de pago</v-card-title
                    >
                    <v-divider></v-divider>
                    <v-card-text>
                      <div>
                        <FONT SIZE="2"
                          ><strong>Nombre: </strong>
                          {{ pago.pago_nombre_tarj }}</FONT
                        >
                      </div>
                      <div>
                        <FONT SIZE="2"
                          ><strong>Número de tarjeta: </strong>
                          {{ pago.pago_numero_tarj }}</FONT
                        >
                      </div>
                      <div>
                        <FONT SIZE="2"
                          ><strong>MM/YYYY de vencimiento: </strong>
                          {{ pago.pago_mes_venc }}/{{
                            pago.pago_anio_venc
                          }}</FONT
                        >
                      </div>
                    </v-card-text>
                    <v-divider></v-divider>
                    <v-card-actions class="justify-center">
                      <v-btn color="blue" text small @click="cargarInfoPago()">
                        Editar
                      </v-btn>
                      <v-btn color="red" text small @click="eliminarPago()">
                        Eliminar
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </div>
              </v-col>
            </v-row>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>

    <!-- ACTUALIZACION DE INFORMACION -->
    <v-dialog v-model="dialogInfoUsuario" width="500" eager>
      <v-card class="elevation-12 mx-auto">
        <v-row class="ml-0 mr-0">
          <v-col cols="12" md="12" class="white">
            <v-card-text>
              <h1 class="text-center display-1 blue-grey--text text--accent-3">
                Actualizar información
              </h1>
              <v-form ref="formUpdate" v-model="isValidUpdate">
                <v-text-field
                  v-model="user.us_dni"
                  label="DNI"
                  color="blue-grey accent-3"
                  :rules="[
                    rules.campoVacio(user.us_dni),
                    rules.diezCaracteres(user.us_dni),
                    rules.soloNumeros(user.us_dni),
                  ]"
                  error-count="3"
                  readonly
                ></v-text-field>
                <v-row style="margin-top: -15px; margin-bottom: -15px">
                  <v-col>
                    <v-text-field
                      v-model="user.us_nombres"
                      :rules="[rules.campoVacio(user.us_nombres)]"
                      label="Nombres"
                      color="blue-grey accent-3"
                    ></v-text-field>
                  </v-col>
                  <v-col>
                    <v-text-field
                      v-model="user.us_apellidos"
                      :rules="[rules.campoVacio(user.us_apellidos)]"
                      label="Apellidos"
                      color="blue-grey accent-3"
                    ></v-text-field>
                  </v-col>
                </v-row>
                <v-row style="margin-top: -25px; margin-bottom: -15px">
                  <v-col>
                    <v-text-field
                      v-model="user.us_email"
                      :rules="[
                        rules.campoVacio(user.us_email),
                        rules.email(user.us_email),
                      ]"
                      error-count="2"
                      label="Correo electrónico"
                      color="blue-grey accent-3"
                    ></v-text-field
                  ></v-col>
                  <v-col>
                    <v-text-field
                      v-model="user.us_password"
                      label="Contraseña"
                      :rules="[rules.campoVacio(user.us_password)]"
                      :type="showPassword ? 'text' : 'password'"
                      :append-icon="
                        showPassword ? 'fas fa-eye' : 'fas fa-eye-slash'
                      "
                      @click:append="showPassword = !showPassword"
                      color="blue-grey accent-3"
                      v-on:keyup.enter="UpdateIsValid"
                    >
                    </v-text-field>
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
            <div class="text-center">
              <v-btn
                color="blue-grey accent-3 white--text"
                @click="actualizar"
                :disabled="!isValidUpdate"
                >Actualizar</v-btn
              >
            </div>
          </v-col>
        </v-row>
      </v-card>
    </v-dialog>

    <!-- REGISTRO / ACTUALIZACION DE PAGO -->
    <v-dialog v-model="dialogPago" width="500">
      <v-card class="elevation-12 mx-auto">
        <v-row class="ml-0 mr-0">
          <v-col cols="12" md="12" class="white">
            <v-card-text>
              <h1 class="text-center display-1 blue-grey--text text--accent-3">
                {{ titleDialogPago }} método de pago
              </h1>
              <v-form v-model="isValidFormPago" ref="formPago">
                <v-text-field
                  label="Número de tarjeta"
                  counter="16"
                  v-model="pagoUpdate.pago_numero_tarj"
                  :rules="[
                    rules.soloNumeros(pagoUpdate.pago_numero_tarj),
                    rules.campoVacio(pagoUpdate.pago_numero_tarj),
                    rules.diezSeisCaracteres(pagoUpdate.pago_numero_tarj),
                  ]"
                  error-count="3"
                ></v-text-field>
                <v-text-field
                  label="Nombre del titular"
                  type="text"
                  v-model="pagoUpdate.pago_nombre_tarj"
                  :rules="[rules.campoVacio(pagoUpdate.pago_nombre_tarj)]"
                ></v-text-field>
                <v-row>
                  <v-col cols="12" sm="12">
                    <v-menu
                      ref="menu"
                      v-model="menu"
                      :close-on-content-click="false"
                      :return-value.sync="date"
                      transition="scale-transition"
                      offset-y
                      max-width="290px"
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="date"
                          label="YYYY/MM"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="date"
                        locale="es-419"
                        type="month"
                        :show-current="date"
                        scrollable
                      >
                        <v-spacer></v-spacer>
                        <v-btn text color="primary" @click="menu = false">
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.menu.save(date)"
                        >
                          OK
                        </v-btn>
                      </v-date-picker>
                    </v-menu>
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
            <div class="text-center">
              <v-btn
                color="blue-grey accent-3 white--text"
                :disabled="!isValidFormPago"
                @click="ActionMetodoPago(accionDialogPago)"
                >{{ buttonDialogPago }}</v-btn
              >
            </div>
          </v-col>
        </v-row>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import User from "../models/user";
import AuthService from "../services/auth.service";
import PagoService from "../services/pago.service";
import Pago from "../models/pago";

export default {
  name: "Carrito",
  data() {
    return {
      pago: new Pago("", "", "", ""),
      pagoUpdate: new Pago("", "", "", ""),
      dialogInfoUsuario: false,
      dialogPago: false,
      showPassword: false,
      user: new User("", "", "", "", "", ""),
      isValidUpdate: true,
      isValidFormPago: true,
      metodoPago: [],
      titleDialogPago: "",
      buttonDialogPago: "",
      accionDialogPago: "",
      date: new Date().toISOString().substr(0, 7),
      menu: false,
      rules: {
        campoVacio: (texto) => !!texto || "Campo requerido!",
        diezCaracteres: (texto) =>
          (texto.length <= 10 && texto.length >= 10) ||
          "Debe tener 10 caracteres",
        soloNumeros: (texto) =>
          Number.isInteger(Number(texto)) || "Ingresar sólo números",
        email: (texto) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(texto) || "Ingrese un correo electrónico válido!";
        },
        diezSeisCaracteres: (texto) =>
          (texto.length <= 16 && texto.length >= 16) || "Debe tener 16 números",
      },
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
    currentDataUser() {
      return this.$store.state.datosUsuario;
    },
  },
  methods: {
    closeDialog() {
      this.dialogInfoUsuario = false;
    },
    closeDialogPago() {
      this.dialogPago = false;
    },
    actualizar() {
      this.user.us_id = this.currentUser.id;
      AuthService.updateUser(this.user).then((response) => {
        if (response.data.error) {
          this.showError({ message: response.data.error });
        } else {
          this.showSuccess({ message: response.data.exito });
          this.closeDialog();
          this.GetDataCurrentUser();
        }
      });
    },
    cargarInformacion(usuario) {
      this.dialogInfoUsuario = true;
      this.user.us_dni = usuario.us_dni;
      this.user.us_nombres = usuario.us_nombres;
      this.user.us_apellidos = usuario.us_apellidos;
      this.user.us_email = usuario.us_email;
      this.user.us_password = usuario.us_password;
    },
    cargarInfoPago() {
      this.dialogPago = true;
      this.titleDialogPago = "Actualizar";
      this.buttonDialogPago = "Actualizar";
      this.accionDialogPago = "Actualizar";
      this.ListarMetodoPago();
    },
    limpiarFormulario() {
      this.user = new User("", "", "", "", "", "");
    },
    GetDataCurrentUser() {
      this.$store.dispatch("auth/getCurrentUser", this.currentUser.id).then(
        (userData) => {
          this.$store.state.datosUsuario = userData;
          this.$store.state.datosUsuario.us_nombres_abr = userData.us_nombres.charAt(
            0
          );
          this.$store.state.datosUsuario.us_apellidos_abr = userData.us_apellidos.charAt(
            0
          );
        },
        (error) => {
          this.showError({ message: error.error });
        }
      );
    },
    ActionMetodoPago(accion) {
      if (accion == "Registrar") {
        this.pagoUpdate.pago_mes_venc = Number(this.date.split("-")[1]);
        this.pagoUpdate.pago_anio_venc = Number(this.date.split("-")[0]);
        this.pagoUpdate.pago_usu_id = this.currentUser.id;
        PagoService.add_pago(this.pagoUpdate).then((response) => {
          if (response.data.error) {
            this.showError({ message: response.data.error });
          } else {
            this.showSuccess({ message: response.data.exito });
            this.closeDialogPago();
            this.ListarMetodoPago();
          }
        });
      } else if (accion == "Actualizar") {
        this.pagoUpdate.pago_mes_venc = Number(this.date.split("-")[1]);
        this.pagoUpdate.pago_anio_venc = Number(this.date.split("-")[0]);
        this.pagoUpdate.pago_usu_id = this.currentUser.id;
        PagoService.update_pago(this.pagoUpdate).then((response) => {
          if (response.data.error) {
            this.showError({ message: response.data.error });
          } else {
            this.showSuccess({ message: response.data.exito });
            this.closeDialogPago();
            this.titleDialogPago = "";
            this.ListarMetodoPago();
          }
        });
      }
    },
    ListarMetodoPago() {
      PagoService.get_pago(this.currentUser.id).then((response) => {
        this.metodoPago = response.data;
        if (this.metodoPago.length > 0) {
          if (this.titleDialogPago == "Actualizar") {
            this.metodoPago = response.data;
            this.pagoUpdate = this.metodoPago[0];
            this.date = `${this.pago.pago_anio_venc}-${this.pago.pago_mes_venc}`;
          } else {
            this.metodoPago = response.data;
            this.pago = this.metodoPago[0];
            this.date = `${this.pago.pago_anio_venc}-${this.pago.pago_mes_venc}`;
          }
        }
      });
    },
    eliminarPago() {
      PagoService.delete_pago(this.currentUser.id).then((response) => {
        if (response.data.error) {
          this.showError({ message: response.data.error });
        } else {
          this.showSuccess({ message: response.data.exito });
          this.ListarMetodoPago();
          this.pagoUpdate = new Pago("", "", "", "");
          this.pago = new Pago("", "", "", "");
          this.date = new Date().toISOString().substr(0, 7);
          this.$refs.formPago.resetValidation();
        }
      });
    },
    UpdateIsValid() {
      if (this.isValidUpdate) {
        this.actualizar();
      }
    },
    resetValidation() {
      this.$refs.formUpdate.resetValidation();
    },
  },
  mounted() {
    this.ListarMetodoPago();
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