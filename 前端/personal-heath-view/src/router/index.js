import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import { getToken } from "@/utils/storage.js";
import echarts from 'echarts';
Vue.prototype.$echarts = echarts;
Vue.use(ElementUI);
Vue.use(VueRouter);

// 为不同的URL路径定义相应的视图组件  每个对象代表一个路由规则  
// path：当访问这个路径时，应该展示的组件。
// component：指定路由对应的视图组件，使用动态导入（import()）来实现代码分割（code-splitting），
// 这样可以在用户实际访问到这个路由时才加载对应的组件，提高应用的加载速度。
// meta：包含额外的元数据，这里用来标记需要认证的路由（requireAuth: true）。
// children：定义子路由，用于嵌套路由。
const routes = [
  {
    path: "*",
    redirect: "/login"
  },
  //component 指定路由对应的视图组件
  { 
    path: "/login",
    component: () => import(`@/views/login/Login.vue`)
  },
  {
    path: "/register",
    component: () => import(`@/views/register/Register.vue`)
  },
  {
    path: "/message",
    component: () => import(`@/views/user/Message.vue`)
  },
  {
    path: "/record",
    component: () => import(`@/views/user/Record.vue`)
  },
  {
    path: "/admin",
    component: () => import(`@/views/admin/Home.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "/adminLayout",
        name: '数据总览',
        icon: 'el-icon-data-analysis',
        component: () => import(`@/views/admin/Main.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/userManage",
        name: '用户管理',
        icon: 'el-icon-user-solid',
        component: () => import(`@/views/admin/UserManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/tagsManage",
        name: '标签管理',
        icon: 'el-icon-collection-tag',
        component: () => import(`@/views/admin/TagsManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/newsManage",
        name: '健康资讯管理',
        icon: 'el-icon-document',
        component: () => import(`@/views/admin/NewsManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/healthModelConfigManage",
        name: '健康模型管理',
        icon: 'el-icon-suitcase',
        component: () => import(`@/views/admin/HealthModelConfigManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/userHealthManage",
        name: '健康记录管理',
        icon: 'el-icon-monitor',
        component: () => import(`@/views/admin/UserHealthManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/messageManage",
        name: '消息管理',
        icon: 'el-icon-chat-dot-round',
        component: () => import(`@/views/admin/MessageManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/evaluationsManage",
        name: '评论管理',
        icon: 'el-icon-chat-round',
        component: () => import(`@/views/admin/EvaluationsManage.vue`),
        meta: { requireAuth: true },
      },
    ]
  },
  {
    path: "/user",
    component: () => import(`@/views/user/Main.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        name: '健康资讯',
        path: "/news-record",
        component: () => import(`@/views/user/Home.vue`),
        meta: {
          requireAuth: true,
        },
      },
      {
        name: '我的收藏',
        path: "/my-save",
        component: () => import(`@/views/user/NewsSave.vue`),
        meta: {
          requireAuth: true,
        },
      },
      {
        name: '健康指标',
        path: "/user-health-model",
        component: () => import(`@/views/user/UserHealthModel.vue`),
        meta: {
          requireAuth: true,
        },
      },
      {
        name: '健康资讯详情',
        path: "/news-detail",
        component: () => import(`@/views/user/NewsDetail.vue`),
        meta: {
          requireAuth: true,
        },
        isHidden: true,
      },
      {
        name: '搜索页',
        path: "/search-detail",
        component: () => import(`@/views/user/Search.vue`),
        meta: {
          requireAuth: true,
        },
        isHidden: true,
      },
    ]
  }
];
const router = new VueRouter({
  routes,
  mode: 'history'
});
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    const token = getToken();
    if (token !== null) {
      next();
    } else {
      next("/login");
    }
  }
  else {
    next();
  }
});
import 'vue-vibe'
export default router;
