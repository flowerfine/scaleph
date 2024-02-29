import { request } from "@umijs/max";

export const LogService = {
  url: "/api/log",

  list: async (param: Scaleph.QueryParam) => {
    return request<Scaleph.Page<Scaleph.LogLogin>>(`${LogService.url}`, {
      method: "GET",
      params: param,
    });
  },
};
