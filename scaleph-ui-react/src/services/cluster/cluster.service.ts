import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { DiClusterConfig, DiClusterConfigParam } from './typings';

export const ClusterService = {
  url: '/api/di/cluster',

  listClusterByPage: async (queryParam: DiClusterConfigParam) => {
    return request<PageResponse<DiClusterConfig>>(`${ClusterService.url}`, {
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
  listAllCluster: async () => {
    return request<Dict[]>(`${ClusterService.url}/all`, {
      method: 'GET',
    });
  },
  deleteClusterRow: async (row: DiClusterConfig) => {
    return request<ResponseBody<any>>(`${ClusterService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
  deleteClusterBatch: async (rows: DiClusterConfig[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${ClusterService.url}/` + 'batch', {
      method: 'POST',
      data: { ...params },
    });
  },

  addCluster: async (row: DiClusterConfig) => {
    return request<ResponseBody<any>>(`${ClusterService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateCluster: async (row: DiClusterConfig) => {
    return request<ResponseBody<any>>(`${ClusterService.url}`, {
      method: 'PUT',
      data: row,
    });
  },
};
