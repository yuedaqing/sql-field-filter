// index.ts
import type { AxiosInstance, AxiosRequestConfig } from "axios";
import axios from "axios";
import { notification } from "ant-design-vue";
import { h } from "vue";
import { CloseCircleOutlined } from "@ant-design/icons-vue";

const openNotification = (message: string) => {
  notification.open({
    message: "消息通知",
    description: message,
    icon: h(CloseCircleOutlined, { style: "color: red" }),
    duration: 1,
  });
};
type Result<T> = {
  code: number;
  message: string;
  data: T;
};

// 导出Request类，可以用来自定义传递配置来创建实例
export class Request {
  // axios 实例
  instance: AxiosInstance;
  // 基础配置，url和超时时间
  baseConfig: AxiosRequestConfig = {
    baseURL: "http://localhost:9123/",
    timeout: 60000,
    // `withCredentials` 表示跨域请求时是否需要使用凭证
    withCredentials: true,
  };

  constructor(config: AxiosRequestConfig) {
    // 使用axios.create创建axios实例
    this.instance = axios.create(Object.assign(this.baseConfig, config));
    this.instance.interceptors.request.use(
      (config) => {
        return config;
      },
      (err: any) => {
        // 请求错误，这里可以用全局提示框进行提示
        return Promise.reject(err);
      }
    );
  }
}

// 定义请求方法
const newRequest = new Request({});

export async function request<T>(config: AxiosRequestConfig): Promise<T> {
  const response = await newRequest.instance.request<T>(config);
  return response.data;
}

export async function get<T>(url: string, params?: any): Promise<T> {
  const response = await newRequest.instance.get<T>(url, { params });
  return response.data;
}

export async function post<T>(
  url: string,
  data?: any,
  config?: any
): Promise<T> {
  const res = await newRequest.instance.post<T>(url, data, config);
  console.log(res);
  return res.data;
}

export async function postFile<T>(
  url: string,
  data?: any,
  config?: any
): Promise<T> {
  const res = await newRequest.instance.post<T>(url, data, config);
  console.log("文件下载啦" + res.data);
  return res.data;
}

export async function put<T>(url: string, data?: any): Promise<T> {
  const response = await newRequest.instance.put<T>(url, data);
  return response.data;
}

export async function del<T>(url: string, params?: any): Promise<T> {
  const response = await newRequest.instance.delete<T>(url, { params });
  return response.data;
}

// 默认导出Request实例
export default new Request({});
