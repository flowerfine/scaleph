import { request } from "@umijs/max";

export const MsgService = {
  url: "/api/msg",

  list: async (param: Scaleph.SysMessageParam) => {
    return request<Scaleph.Page<Scaleph.SysMessage>>(`${MsgService.url}`, {
      method: "GET",
      params: param,
    });
  },
  read: async (ids: string | string[] | number[]) => {
    return request<Scaleph.ResponseBody<any>>(`${MsgService.url}`, {
      method: "POST",
      data: ids,
    });
  },
  countUnReadMsg: async () => {
    return request<Scaleph.ResponseBody<number>>(`${MsgService.url}/count`, {
      method: "GET",
    });
  },
};
