import type { RequestOptions } from "@@/plugin-request/request";
import { message, notification } from "antd";
import { getLocale, history, RequestConfig } from "@umijs/max";
import {USER_AUTH} from "@/constants";

/**
 * @name 错误处理
 * pro 自带的错误处理， 可以在这里做自己的改动
 * @doc https://umijs.org/docs/max/request#配置
 */
export const errorConfig: RequestConfig = {
  errorConfig: {},

  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      // 拦截请求配置，进行个性化处理。
      const headers = {
        ...config.headers,
        "Accept-Language": getLocale(),
        u_token: localStorage.getItem(USER_AUTH.token) || '',
      };
      return { ...config, headers: headers };
    },
  ],

  // 响应拦截器
  responseInterceptors: [
    (response) => {
      // check response status
      if (response.status != 200) {
        switch (response.status) {
          case 401:
            history.push("/user/login");
            break;
          case 403:
            history.push("/403");
            break;
          default:
            break;
        }
      }
      // check response data
      const { data: respData } = response as unknown as Scaleph.ResponseBody<any>;
      if (
        respData &&
        respData.success != null &&
        respData.success != undefined &&
        !respData.success
      ) {
        switch (respData.errorCode) {
          case "401":
            history.push("/user/login");
            break;
          case "403":
            history.push("/403");
            break;
          default:
            handleError(
              respData.errorCode,
              respData.errorMessage,
              respData.showType
            );
            break;
        }
      }
      return response;
    },
  ],
};

const handleError = (
  errorCode: string | undefined,
  errorMessage: string | undefined,
  showType: string | undefined
) => {
  if (showType == "1") {
    message.warning(errorMessage, 2);
  } else if (showType == "2") {
    message.error(errorMessage, 2);
  } else if (showType == "4") {
    notification.error({
      message: "Error:" + errorCode,
      description: errorMessage,
      duration: 3,
    });
  }
};
