import { PageResponse, ResponseBody } from '@/app.d';
import {
  FlinkClusterInstance,
  FlinkClusterInstanceParam,
  FlinkSessionClusterNewParam,
} from './typings';
import { request } from 'umi';

export const FlinkCLusterInstanceService = {
  url: '/api/flink/cluster-instance',

  list: async (queryParam: FlinkClusterInstanceParam) => {
    return request<PageResponse<FlinkClusterInstance>>(`${FlinkCLusterInstanceService.url}`, {
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
    return request<FlinkClusterInstance>(`${FlinkCLusterInstanceService.url}/` + id, {
      method: 'GET',
    });
  },

  newSession: async (row: FlinkSessionClusterNewParam) => {
    return request<ResponseBody<any>>(`${FlinkCLusterInstanceService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  shutdown: async (row: FlinkClusterInstance) => {
    return request<ResponseBody<any>>(`${FlinkCLusterInstanceService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  shutdownBatch: async (rows: FlinkClusterInstance[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${FlinkCLusterInstanceService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
