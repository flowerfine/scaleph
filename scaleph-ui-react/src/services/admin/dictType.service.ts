import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SysDictType, SysDictTypeParam } from './typings';

export const DictTypeService = {
  url: '/api/admin/dict/type',

  listDictTypeByPage: async (queryParam: SysDictTypeParam) => {
    return request<PageResponse<SysDictType>>(`${DictTypeService.url}`, {
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

  listAllDictType: async () => {
    return request<Dict[]>(`${DictTypeService.url}/all`, {
      method: 'GET',
    });
  },
  deleteDictTypeRow: async (row: SysDictType) => {
    return request<ResponseBody<any>>(`${DictTypeService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteDictTypeBatch: async (rows: SysDictType[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${DictTypeService.url}/` + 'batch', {
      method: 'POST',
      data: { ...params },
    });
  },
  addDictType: async (row: SysDictType) => {
    return request<ResponseBody<any>>(`${DictTypeService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateDictType: async (row: SysDictType) => {
    return request<ResponseBody<any>>(`${DictTypeService.url}`, {
      method: 'PUT',
      data: row,
    });
  },
};
