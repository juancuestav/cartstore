<template>
  <v-container>
    <v-app-bar app color="blue-grey darken-3" dark elevation="6">
      <v-toolbar-title class="mr-10">E-COMMERCE OPTATIVA</v-toolbar-title>
      <v-btn text redounded :to="{ name: 'Home' }" exact>
        <v-icon left small> fas fa-home </v-icon>
        Home
      </v-btn>
      <v-btn text redounded :to="{ name: 'Carshop' }">
        <v-icon left small> fas fa-shopping-cart </v-icon>
        Carrito
      </v-btn>
      <!---<v-spacer></v-spacer>
      <v-text-field
        class="mt-4"
        label="Búsqueda"
        append-icon="fas fa-search"
        mt-10
      ></v-text-field>-->
      <v-spacer></v-spacer>
      <!-- SI ESTA LOGUEADO -->
      <div v-if="loggedIn">
        <!-- SI ES ADMINISTRADOR -->
        <div v-if="currentDataUser.us_rol == 'ADMINISTRADOR'">
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn text v-bind="attrs" v-on="on">
                <v-icon left dense class="mb-1"> fas fa-user </v-icon>
                {{ currentDataUser.us_nombres }}
                <v-icon>mdi-chevron-down</v-icon>
              </v-btn>
            </template>

            <v-list>
              <v-list-item-group>
                <div v-if="currentDataUser.us_rol == 'ADMINISTRADOR'">
                  <v-list-item dense :to="{ name: 'Products' }">
                    <v-list-item-icon>
                      <v-icon left dense>fas fa-shopping-basket</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title style="font-size: 1rem"
                        >Gestión de productos</v-list-item-title
                      >
                    </v-list-item-content>
                  </v-list-item>
                </div>
                <v-list-item dense @click="Salir">
                  <v-list-item-icon>
                    <v-icon left dense>fas fa-sign-out-alt</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title style="font-size: 1rem"
                      >Salir</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-menu>
        </div>

        <!-- SI ES UN CLIENTE -->
        <div v-else>
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn text v-bind="attrs" v-on="on">
                <v-icon left dense class="mb-1"> fas fa-user </v-icon>
                {{ currentDataUser.us_nombres }}
                {{ currentDataUser.us_apellidos }}
                <v-icon>mdi-chevron-down</v-icon>
              </v-btn>
            </template>

            <v-list>
              <v-list-item-group>
                <v-list-item dense :to="{ name: 'Pedidos' }">
                  <v-list-item-icon>
                    <v-icon left dense>fas fa-shopping-basket</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title style="font-size: 1rem"
                      >Tus pedidos</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>

                <v-list-item dense :to="{ name: 'Cuenta' }">
                  <v-list-item-icon>
                    <v-icon left dense>fas fa-user-cog</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title style="font-size: 1rem"
                      >Configuración de tu cuenta</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>

                <v-list-item dense @click="Salir">
                  <v-list-item-icon>
                    <v-icon left>fas fa-sign-out-alt</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title
                      style="font-size: 1rem; padding-left: 2px"
                      >Salir</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-menu>
        </div>
      </div>

      <!-- SI NO ESTA LOGUEADO -->
      <div v-else>
        <v-btn
          text
          redounded
          @click="
            dialog = true;
            LimpiarFormLogin();
            LimpiarFormRegistro();
            step = 1;
            resetValidation();
          "
          >Iniciar sesión</v-btn
        >
      </div>
    </v-app-bar>

    <!-- DIALOG INICIAR SESION -->
    <v-dialog v-model="dialog" width="60%" eager>
      <v-card class="elevation-12 mx-auto">
        <v-window v-model="step">
          <!-- LOGIN -->
          <v-window-item :value="1">
            <v-row class="ml-0 mr-0">
              <!-- FORMULARIO DE LOGIN -->
              <v-col cols="12" md="8">
                <v-card-text class="mt-12">
                  <h1
                    class="text-center display-2 blue-grey--text text--accent-3"
                  >
                    Inicio de sesión
                  </h1>
                  <v-form ref="formLogin" v-model="isValidLogin">
                    <v-text-field
                      v-model="userL.us_dni"
                      :rules="[rules.campoVacio(userL.us_dni)]"
                      label="DNI o email"
                      prepend-icon="fas fa-user"
                      color="blue-grey accent-3"
                    ></v-text-field>
                    <v-text-field
                      v-model="userL.us_password"
                      :rules="[rules.campoVacio(userL.us_password)]"
                      label="Contraseña"
                      :type="showPassword ? 'text' : 'password'"
                      prepend-icon="fas fa-lock"
                      :append-icon="
                        showPassword ? 'fas fa-eye' : 'fas fa-eye-slash'
                      "
                      @click:append="showPassword = !showPassword"
                      color="blue-grey accent-3"
                      v-on:keyup.enter="LoginIsValid"
                    >
                    </v-text-field>
                  </v-form>
                </v-card-text>
                <div class="text-center mt-3">
                  <v-btn
                    rounded
                    color="blue-grey accent-3 white--text"
                    @click="Login"
                    :disabled="!isValidLogin"
                    >Entrar</v-btn
                  >
                </div>
              </v-col>

              <!-- ENLACE A FORMULARIO DE REGISTRO -->
              <v-col cols="12" md="4" class="blue-grey accent-3">
                <v-card-text class="white--text mt-12">
                  <h1 class="text-center display-1">Bienvenido!</h1>
                  <h5 class="text-center">
                    Registra tus datos para inciar sesión en el sistema
                  </h5>
                </v-card-text>
                <div class="text-center">
                  <v-btn
                    rounded
                    outlined
                    dark
                    @click="
                      step++;
                      LimpiarFormLogin();
                      resetValidation();
                    "
                    >Registrarse</v-btn
                  >
                </div>
              </v-col>
            </v-row>
          </v-window-item>

          <!-- REGISTRO DE USUARIO -->
          <v-window-item :value="2" eager>
            <v-row class="ml-0 mr-0">
              <!-- ENLACE A FORMULARIO DE LOGIN -->
              <v-col cols="12" md="4" class="blue-grey accent-3">
                <v-card-text class="white--text mt-12">
                  <h1 class="text-center display-1">Bienvenido!</h1>
                  <h5 class="text-center">
                    Ingresa tus datos para realizar tus compras
                  </h5>
                </v-card-text>
                <div class="text-center">
                  <v-btn
                    rounded
                    outlined
                    dark
                    @click="
                      step--;
                      LimpiarFormRegistro();
                      resetValidation();
                    "
                    >Iniciar sesión</v-btn
                  >
                </div>
              </v-col>

              <!-- FORMULARIO DE REGISTRO -->
              <v-col cols="12" md="8">
                <v-card-text class="mt-12">
                  <h1
                    class="text-center display-2 blue-grey--text text--accent-3"
                  >
                    Registrar cuenta
                  </h1>
                  <v-form ref="formRegister" v-model="isValidRegister">
                    <v-text-field
                      v-model="user.us_dni"
                      :rules="[
                        rules.campoVacio(user.us_dni),
                        rules.diezCaracteres(user.us_dni),
                        rules.soloNumeros(user.us_dni),
                      ]"
                      error-count="3"
                      label="DNI"
                      color="blue-grey accent-3"
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
                          :rules="[rules.campoVacio(user.us_password)]"
                          label="Contraseña"
                          :type="showPassword ? 'text' : 'password'"
                          :append-icon="
                            showPassword ? 'fas fa-eye' : 'fas fa-eye-slash'
                          "
                          @click:append="showPassword = !showPassword"
                          color="blue-grey accent-3"
                          v-on:keyup.enter="RegistroIsValid"
                        >
                        </v-text-field>
                      </v-col>
                    </v-row>
                  </v-form>
                </v-card-text>
                <div class="text-center">
                  <v-btn
                    rounded
                    color="blue-grey accent-3 white--text"
                    @click="RegistrarUsuario"
                    :disabled="!isValidRegister"
                    >Registrar</v-btn
                  >
                </div>
              </v-col>
            </v-row>
          </v-window-item>
        </v-window>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import UserL from "../models/user_l";
import User from "../models/user";

export default {
  name: "NavigationBar",

  data() {
    return {
      userL: new UserL("", ""),
      user: new User("", "", "", "", "", ""),
      dialog: false,
      showPassword: false,
      step: 1,
      isValidLogin: true,
      isValidRegister: true,
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
      },
    };
  },
  props: {
    source: String,
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
    Login() {
      if (this.userL.us_dni && this.userL.us_password) {
        this.$store.dispatch("auth/login", this.userL).then(
          (response) => {
            this.dialog = false;
            this.GetDataCurrentUser();
            this.LimpiarFormLogin();
          },
          (error) => {
            this.showError({ message: error.error });
          }
        );
      } else {
        this.showWarning({ message: "Llene los campos del formulario" });
      }
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
    Salir() {
      this.$store.dispatch("auth/logout");
      this.$store.state.datosUsuario = {};
      if (this.$route.name != "Home") {
        this.$router.push({ name: "Home" });
      }
    },
    RegistrarUsuario() {
      if (
        this.user.us_dni &&
        this.user.us_nombres &&
        this.user.us_apellidos &&
        this.user.us_email &&
        this.user.us_password
      ) {
        this.user.us_rol = "cliente";
        this.$store.dispatch("auth/register", this.user).then(
          (response) => {
            this.showSuccess({ message: response.exito });
            this.$refs.formRegister.resetValidation();
            this.$refs.formRegister.reset();
            this.LimpiarFormRegistro();
            // remover focus de todos los elementos
            document.activeElement.blur();
          },
          (error) => {
            this.showError({ message: error.error });
          }
        );
      } else {
        this.showWarning({ message: "Llene los campos del formulario" });
      }
    },
    LimpiarFormRegistro() {
      this.user = new User("", "", "", "", "", "");
    },
    LimpiarFormLogin() {
      this.userL = new UserL("", "");
    },
    resetValidation() {
      this.$refs.formLogin.resetValidation();
      this.$refs.formRegister.resetValidation();
    },
    LoginIsValid() {
      if (this.isValidLogin) {
        this.Login();
      }
    },
    RegistroIsValid() {
      if (this.isValidRegister) {
        this.RegistrarUsuario();
      }
    },
  },
  mounted() {
    if (this.loggedIn) {
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
    }
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
