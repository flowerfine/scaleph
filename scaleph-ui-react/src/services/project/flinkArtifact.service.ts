import { PageResponse, ResponseBody } from '@/app.d';
import { FlinkArtifact, FlinkArtifactListParam } from './typings';
import { request } from 'umi';

export const FlinkArtifactService = {
  url: '/api/flink/artifact',

  list: async (queryParam: FlinkArtifactListParam) => {
    return request<PageResponse<FlinkArtifact>>(`${FlinkArtifactService.url}`, {
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

  add: async (row: FlinkArtifact) => {
    return request<ResponseBody<any>>(`${FlinkArtifactService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: FlinkArtifact) => {
    return request<ResponseBody<any>>(`${FlinkArtifactService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: FlinkArtifact) => {
    return request<ResponseBody<any>>(`${FlinkArtifactService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
};
