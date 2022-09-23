import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { MetaDataSource, MetaDataSourceParam } from './typings';

export const DataSourceService = {
  url: '/api/di/datasource',

  listDataSourceByPage: async (queryParam: MetaDataSourceParam) => {
    return request<PageResponse<MetaDataSource>>(`${DataSourceService.url}`, {
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

  listDataSourceByType: async (datasourceType: string) => {
    return request<Dict[]>(`${DataSourceService.url}/type/` + datasourceType, {
      method: 'GET',
    });
  },

  deleteDataSourceRow: async (row: MetaDataSource) => {
    return request<ResponseBody<any>>(`${DataSourceService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteDataSourceBatch: async (rows: MetaDataSource[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${DataSourceService.url}/` + 'batch', {
      method: 'POST',
      data: { ...params },
    });
  },

  addDataSource: async (row: MetaDataSource) => {
    return request<ResponseBody<any>>(`${DataSourceService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateDataSource: async (row: MetaDataSource) => {
    return request<ResponseBody<any>>(`${DataSourceService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  testConnection: async (row: MetaDataSource) => {
    return request<ResponseBody<any>>(`${DataSourceService.url}/test`, {
      method: 'POST',
      data: row,
    });
  },

  showPassword: async (row: MetaDataSource) => {
    return request<ResponseBody<any>>(`${DataSourceService.url}/passwd/` + row.id, {
      method: 'GET',
    });
  },
};
