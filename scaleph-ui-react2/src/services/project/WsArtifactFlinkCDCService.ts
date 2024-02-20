import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {
  WsArtifactFlinkCDC,
  WsArtifactFlinkCDCAddParam,
  WsArtifactFlinkCDCHistoryParam,
  WsArtifactFlinkCDCParam,
  WsArtifactFlinkCDCSelectListParam,
  WsArtifactFlinkCDCUpdateParam
} from "@/services/project/typings";

export const WsArtifactFlinkCDCService = {
  url: '/api/artifact/flink/cdc',

  list: async (queryParam: WsArtifactFlinkCDCParam) => {
    return request<PageResponse<WsArtifactFlinkCDC>>(`${WsArtifactFlinkCDCService.url}`, {
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

  listByArtifact: async (queryParam: WsArtifactFlinkCDCHistoryParam) => {
    return request<PageResponse<WsArtifactFlinkCDC>>(`${WsArtifactFlinkCDCService.url}/history`, {
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

  listAll: async (queryParam: WsArtifactFlinkCDCSelectListParam) => {
    return request<ResponseBody<Array<WsArtifactFlinkCDC>>>(`${WsArtifactFlinkCDCService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  selectOne: async (id: number | string) => {
    return request<WsArtifactFlinkCDC>(`${WsArtifactFlinkCDCService.url}/${id}`, {
      method: 'GET',
    });
  },

  add: async (row: WsArtifactFlinkCDCAddParam) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkCDCService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsArtifactFlinkCDCUpdateParam) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkCDCService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: WsArtifactFlinkCDC) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkCDCService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsArtifactFlinkCDC[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsArtifactFlinkCDCService.url}/batch`, {
      method: 'DELETE',
      data: params
    });
  },

  deleteArtifact: async (artifactId: number) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkCDCService.url}/artifact/${artifactId}`, {
      method: 'DELETE',
    });
  },

  getDnds: async () => {
    return request<ResponseBody<Array<Record<string, any>>>>(`${WsArtifactFlinkCDCService.url}/dag/dnd`, {
      method: 'GET',
    });
  },
};
