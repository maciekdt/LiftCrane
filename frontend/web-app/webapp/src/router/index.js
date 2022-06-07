import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Devices from "../views/Devices";
import Raports from "../views/Raports";
import Profiles from "../views/Profiles";
import Login from "../views/Login";
import { auth } from "@/fb";

Vue.use(VueRouter);
const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/About",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
  },
  {
    path: "/Devices",
    name: "Devices",
    component: Devices,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/Raports",
    name: "Raports",
    component: Raports,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/Profiles",
    name: "Profiles",
    component: Profiles,
    meta: {
      requiresAuth: true,
    },
  },

  {
    path: "/Login",
    name: "Login",
    component: Login,
  },
];

const router = new VueRouter({
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  // Check for requiresAuth guard
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    // Check if NO logged user
    if (!auth.currentUser) {
      // Go to login
      next({
        path: "/",
        query: {
          redirect: to.fullPath,
        },
      });
    } else {
      // Proceed to route
      next();
    }
  } else if (to.matched.some((record) => record.meta.requiresGuest)) {
    // Check if NO logged user
    if (auth.currentUser) {
      // Go to login
      next({
        path: "/",
        query: {
          redirect: to.fullPath,
        },
      });
    } else {
      // Proceed to route
      next();
    }
  } else {
    // Proceed to route
    next();
  }
});

export default router;
