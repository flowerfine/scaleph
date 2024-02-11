import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {
  WsFlinkArtifactCDC,
  WsFlinkArtifactCDCAddParam,
  WsFlinkArtifactCDCParam,
  WsFlinkArtifactCDCSelectListParam,
  WsFlinkArtifactCDCUpdateParam
} from "@/services/project/typings";

export const WsFlinkCDCService = {
  url: '/api/flink-cdc',

  getDnds: async () => {
    return request<ResponseBody<Array<Record<string, any>>>>(`${WsFlinkCDCService.url}/dag/dnd`, {
      method: 'GET',
    });
  },

  list: async (queryParam: WsFlinkArtifactCDCParam) => {
    return request<PageResponse<WsFlinkArtifactCDC>>(`${WsFlinkCDCService.url}`, {
      method: 'GET',
      params: queryParam,
    }).then((res) => {
      const result = {
        data: res.records,
        total: res.total,
        pageSize: res.size,
        current: res.current,
      };
      return result;
    });
  },

  listAll: async (queryParam: WsFlinkArtifactCDCSelectListParam) => {
    return request<ResponseBody<Array<WsFlinkArtifactCDC>>>(`${WsFlinkCDCService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  add: async (row: WsFlinkArtifactCDCAddParam) => {
    return request<ResponseBody<any>>(`${WsFlinkCDCService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsFlinkArtifactCDCUpdateParam) => {
    return request<ResponseBody<any>>(`${WsFlinkCDCService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: WsFlinkArtifactCDC) => {
    return request<ResponseBody<any>>(`${WsFlinkCDCService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsFlinkArtifactCDC[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsFlinkCDCService.url}/batch`, {
      method: 'DELETE',
      data: params
    });
  },
};
