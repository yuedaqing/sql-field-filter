// index.ts
import type { AxiosInstance, AxiosRequestConfig } from "axios";
import axios from "axios";
import { message, notification } from "ant-design-vue";
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
    this.instance.interceptors.response.use(
      (res: any) => {
        // 直接返回res，当然你也可以只返回res.data
        // 系统如果有自定义code也可以在这里处理
        // const { data, config } = res; // 解构

        console.log("响应拦截器" + res);
        console.log(res);
        if (res.code === 40000) {
          message.error(res.message);
        }
        return res;
      },
      (err: any) => {
        // 这里用来处理http常见错误，进行全局提示
        let message = "";
        switch (err.response.status) {
          case 400:
            message = "请求错误(400)";
            break;
          case 401:
            message = "未授权，请重新登录(401)";
            // 这里可以做清空storage并跳转到登录页的操作
            break;
          case 403:
            message = "拒绝访问(403)";
            break;
          case 404:
            message = "请求出错(404)";
            break;
          case 408:
            message = "请求超时(408)";
            break;
          case 500:
            message = "服务器错误(500)";
            break;
          case 501:
            message = "服务未实现(501)";
            break;
          case 502:
            message = "网络错误(502)";
            break;
          case 503:
            message = "服务不可用(503)";
            break;
          case 504:
            message = "网络超时(504)";
            break;
          case 505:
            message = "HTTP版本不受支持(505)";
            break;
          default:
            message = `连接出错(${err.response.status})!`;
        }
        openNotification(message);
        return Promise.reject(err.response);
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
