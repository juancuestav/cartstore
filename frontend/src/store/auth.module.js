import AuthService from '../services/auth.service';

const user = JSON.parse(localStorage.getItem('user'));
const initialState = user
    ? { status: { loggedIn: true }, user }
    : { status: { loggedIn: false }, user: null };

export const auth = {
    namespaced: true,
    state: initialState,
    actions: {
        login({ commit }, user) {
            return AuthService.login(user).then(
                response => {
                    if (response.error) {
                        commit('loginFailure');
                        return Promise.reject(response);
                    } else {
                        commit('loginSuccess', response);
                        return Promise.resolve(response)
                    }
                },
            );
        },
        logout({ commit }) {
            AuthService.logout();
            commit('logout');
        },
        register({ commit }, user) {
            return AuthService.register(user).then(
                response => {
                    if (response.data.error) {                        
                        commit('registerFailure');
                        return Promise.reject(response.data);
                    } else {
                        commit('registerSuccess');
                        return Promise.resolve(response.data)
                    }
                },
            );
        },
        getCurrentUser({ commit }, id) {
            return AuthService.getUser(id).then(
                response => {
                    if (response.error) {
                        return Promise.reject(response);
                    } else {                        
                        return Promise.resolve(response)
                    }
                },
            );
        },
    },
    mutations: {
        loginSuccess(state, user) {
            state.status.loggedIn = true;
            state.user = user;
        },
        loginFailure(state) {
            state.status.loggedIn = false;
            state.user = null;
        },
        logout(state) {
            state.status.loggedIn = false;
            state.user = null;
        },
        registerSuccess(state) {
            state.status.loggedIn = false;
        },
        registerFailure(state) {
            state.status.loggedIn = false;
        }
    }
};