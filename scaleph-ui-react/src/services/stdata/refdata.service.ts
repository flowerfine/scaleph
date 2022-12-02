import {request} from 'umi';
import {PageResponse, ResponseBody} from '@/app.d';
import {MetaDataSetType, MetaDataSetTypeParam} from "@/services/stdata/typings";

export const RefdataService = {
  url: '/api/stdata/ref',

  list: async (param: MetaDataSetTypeParam) => {
    return request<PageResponse<MetaDataSetType>>(`${RefdataService.url}/type`, {
      method: 'GET',
      params: param,
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

  add: async (row: MetaDataSetType) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/type`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: MetaDataSetType) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/type`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: MetaDataSetType) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/type/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: MetaDataSetType[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${RefdataService.url}/type/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
}
