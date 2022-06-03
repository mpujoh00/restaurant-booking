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
    path: '/login',
    component: () => import(/* webpackChunkName: "login" */ '@/views/Login.vue')
  },
  {
    name: 'userRegistration',
    path: '/registration-:role',
    component: () => import(/* webpackChunkName: "login" */ '@/views/UserRegistration.vue'),
    props: true
  },{
    path: '/restaurant-registration',
    component: () => import(/* webpackChunkName: "login" */ '@/views/RestaurantRegistration.vue'),
  },
  {
    path: '/account',
    component: () => import(/* webpackChunkName: "account" */ '@/views/Account.vue')
  },
  {
    name: 'restaurant',
    path: 'restaurant-:id',
    component: () => import(/* webpackChunkName: "login" */ '@/views/Restaurant.vue'),
    props: true
  },
  {
    path: '/admin/restaurants',
    component: () => import(/* webpackChunkName: "account" */ '@/views/RestaurantsAdmin.vue')
  },
  {
    name: 'restaurantAdmin',
    path: '/admin/restaurant-:id',
    component: () => import(/* webpackChunkName: "account" */ '@/views/RestaurantAdministration.vue'),
    props: true
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
