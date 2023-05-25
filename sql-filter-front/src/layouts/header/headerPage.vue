<template>
  <a-layout
    class="layout"
    style="display: flex; flex-direction: column; width: 100%; min-height: 100%"
  >
    <a-layout-header>
      <div style="display: flex">
        <div class="logo">
          <img
            src="../../../src/assets/logo.png"
            style="width: 32px; height: 32px"
          />
        </div>
        <a-menu
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
          @click="changePage"
        >
          <a-menu-item key="index">主页</a-menu-item>
          <a-menu-item key="document">使用说明</a-menu-item>
        </a-menu>
        <div style="height: 64px; width: 100%; padding-left: 400px">
          <h1 style="line-height: 64px; color: #fff">SQL-Filter</h1>
        </div>
      </div>
    </a-layout-header>
    <a-layout-content>
      <div
        :style="{
          background: '#fff',
          padding: '24px',
          minHeight: 'calc(100vh - 146px)',
        }"
      >
        <router-view />
      </div>
    </a-layout-content>
    <!--    style="background: #fff;padding: 24px;min-width: 100%; min-height: 100%"-->
    <a-layout-footer style="text-align: center">
      SQL Filter ©2023 Created by YueYue
    </a-layout-footer>
  </a-layout>
</template>
<script setup lang="ts">
import { ref } from "vue";
import router from "@/router";

const selectedKeys = ref<string[]>(["index"]);
const changePage = ({ item, key, keyPath }: any) => {
  const jumpPath = ref<string>(keyPath + "");
  if (jumpPath.value.includes(",")) {
    jumpPath.value = jumpPath.value.replace(",", "/");
  }
  router.push("/" + jumpPath.value);
};
</script>
<style scoped>
.logo {
  padding: 0 16px;
}
</style>
