import { request } from 'umi';
import { PageResponse, ResponseBody } from '@/app.d';
import { WsFlinkClusterInstance, WsFlinkClusterInstanceParam } from './typings';

export const FlinkCLusterInstanceService = {
  url: '/api/flink/cluster-instance',

  list: async (queryParam: WsFlinkClusterInstanceParam) => {
    return request<PageResponse<WsFlinkClusterInstance>>(`${FlinkCLusterInstanceService.url}`, {
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

  selectOne: async (id: number) => {
    return request<WsFlinkClusterInstance>(`${FlinkCLusterInstanceService.url}/` + id, {
      method: 'GET',
    });
  },

  newSession: async (row: WsFlinkClusterInstanceParam) => {
    return request<ResponseBody<any>>(`${FlinkCLusterInstanceService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  shutdown: async (row: WsFlinkClusterInstance) => {
    return request<ResponseBody<any>>(`${FlinkCLusterInstanceService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  shutdownBatch: async (rows: WsFlinkClusterInstance[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${FlinkCLusterInstanceService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
