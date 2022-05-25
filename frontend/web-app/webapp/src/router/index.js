import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Devices from '../views/Devices'
import Raports from '../views/Raports'
import Profiles from '../views/Profiles'
import Login from '../views/Login'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/About',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/Devices',
    name: 'Devices',
    component: Devices,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/Raports',
    name: 'Raports',
    component: Raports
  },
  {
    path: '/Profiles',
    name: 'Profiles',
    component: Profiles
  },
  {
    path: '/Login',
    name: 'Login',
    component: Login
  },
]

const router = new VueRouter({
  base: process.env.BASE_URL,
  routes
})

export default router
