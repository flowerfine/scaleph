import {Dict, PageResponse} from '@/app.d';
import {request} from 'umi';
import {SysDictData, SysDictDataParam} from './typings';

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

  listDictDataByType2: async (dictTypeCode: string) => {
    return request<Dict[]>(`${DictDataService.url}/v2/` + dictTypeCode, {
      method: 'GET',
    });
  },
};
