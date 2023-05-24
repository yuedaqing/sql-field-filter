import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HelloWorld from "@/components/HelloWorld.vue";
import headerPage from "@/layouts/header/headerPage.vue";
import documentPage from "../pages/insert/documentPage.vue";
import indexPage from "../pages/insert/indexPage.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "headerPage",
    component: headerPage,
    children: [
      {
        path: "/home",
        name: "HelloWorld",
        component: HelloWorld,
      },
      {
        path: "/document",
        name: "documentPage",
        component: documentPage,
      },
      {
        path: "/index",
        name: "indexPage",
        component: indexPage,
        meta: {
          title: "主页",
        },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
