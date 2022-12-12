import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { WsProject, WsProjectParam } from './typings';

export const ProjectService = {
  url: '/api/di/project',

  listProjectByPage: async (queryParam: WsProjectParam) => {
    return request<PageResponse<WsProject>>(`${ProjectService.url}`, {
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
    return request<WsProject>(`${ProjectService.url}/` + id, {
      method: 'GET',
    });
  },

  listAllProject: async () => {
    return request<Dict[]>(`${ProjectService.url}/all`, { method: 'GET' });
  },

  deleteProjectRow: async (row: WsProject) => {
    return request<ResponseBody<any>>(`${ProjectService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteProjectBatch: async (rows: WsProject[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${ProjectService.url}/` + 'batch', {
      method: 'POST',
      data: { ...params },
    });
  },

  addProject: async (row: WsProject) => {
    return request<ResponseBody<any>>(`${ProjectService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateProject: async (row: WsProject) => {
    return request<ResponseBody<any>>(`${ProjectService.url}`, {
      method: 'PUT',
      data: row,
    });
  },
};
