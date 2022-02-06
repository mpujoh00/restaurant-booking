import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/home',
    component: () => import(/* webpackChunkName: "home" */ '@/views/Home.vue')  // only loads components when accessed (faster app)
  },
  {
    path: '/about',
    component: () => import(/* webpackChunkName: "about" */ '@/views/About.vue')
  },
  {
    path: '/login',
    component: () => import(/* webpackChunkName: "login" */ '@/views/Login.vue')
  },
  {
    path: '/account',
    component: () => import(/* webpackChunkName: "account" */ '@/views/Account.vue')
  },
  {
    // "wildcard path" that matches everything, used as a Catch-all or Fallback (important to place it the last, if not it might redirect even if route is available)
    path: '*',
    redirect: '/login'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  store.dispatch('fetchToken')
  if(to.fullPath === '/account') {
    if(!store.state.token) { // not authenticated
      next('/login')
    }
  }
  if(to.fullPath === '/login') {
    if(store.state.token) { // can't login if already logged
      next('/account')
    }
  }
  next()
})

export default router
