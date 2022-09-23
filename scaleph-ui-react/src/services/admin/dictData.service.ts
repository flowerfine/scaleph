import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SysDictData, SysDictDataParam } from './typings';

export const DictDataService = {
  url: '/api/admin/dict/data',
  listDictDataByPage: async (queryParam: SysDictDataParam) => {
    return request<PageResponse<SysDictData>>(`${DictDataService.url}`, {
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

  deleteDictDataRow: async (row: SysDictData) => {
    return request<ResponseBody<any>>(`${DictDataService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteDictDataBatch: async (rows: SysDictData[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${DictDataService.url}/` + 'batch', {
      method: 'POST',
      data: { ...params },
    });
  },
  addDictData: async (row: SysDictData) => {
    return request<ResponseBody<any>>(`${DictDataService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateDictData: async (row: SysDictData) => {
    return request<ResponseBody<any>>(`${DictDataService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  listDictDataByType: async (dictTypeCode: string) => {
    return request<Dict[]>(`${DictDataService.url}/` + dictTypeCode, {
      method: 'GET',
    });
  },
};
