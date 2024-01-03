import {Dict, PageResponse} from '@/typings';
import {request} from '@umijs/max';
import {SysDictType, SysDictTypeParam} from './typings';

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

};
