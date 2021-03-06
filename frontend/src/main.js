import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';

import VeeValidate from 'vee-validate';
import VueNotifications from 'vue-notifications'
import iziToast from 'izitoast'
import 'izitoast/dist/css/iziToast.min.css'

/*Vue.use(VueNotifications, options)
Vue.use(VueAxios, axios)*/

iziToast.settings({
  close: false,
  displayMode: 1
})

function toast({ title, message, type, timeout, cb, mode }) {
  if (type === VueNotifications.types.warn) type = 'warning'
  if (mode == 'override') timeout = 0;
  return iziToast[type]({ title, message, timeout })
}

const options = {
  success: toast,
  error: toast,
  info: toast,
  warn: toast
}

Vue.use(VueNotifications, options)
//Vue.use(VeeValidate);

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
