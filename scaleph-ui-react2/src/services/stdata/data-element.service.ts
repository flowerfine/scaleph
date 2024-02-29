import {request} from 'umi';
import {PageResponse, ResponseBody} from '@/app.d';
import {MetaDataElement, MetaDataElementParam} from "@/services/stdata/typings";

export const MetaDataElementService = {
  url: '/api/stdata/element',

  list: async (param: MetaDataElementParam) => {
    return request<PageResponse<MetaDataElement>>(`${MetaDataElementService.url}`, {
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

  add: async (row: MetaDataElement) => {
    return request<ResponseBody<any>>(`${MetaDataElementService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: MetaDataElement) => {
    return request<ResponseBody<any>>(`${MetaDataElementService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: MetaDataElement) => {
    return request<ResponseBody<any>>(`${MetaDataElementService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: MetaDataElement[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${MetaDataElementService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
