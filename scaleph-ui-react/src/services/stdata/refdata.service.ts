import {request} from 'umi';
import {PageResponse, ResponseBody} from '@/app.d';
import {
  MetaDataMap,
  MetaDataMapParam,
  MetaDataSet,
  MetaDataSetParam,
  MetaDataSetType,
  MetaDataSetTypeParam
} from "@/services/stdata/typings";

export const RefdataService = {
  url: '/api/stdata/ref',

  listDataSetType: async (param: MetaDataSetTypeParam) => {
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

  addDataSetType: async (row: MetaDataSetType) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/type`, {
      method: 'PUT',
      data: row,
    });
  },

  updateDataSetType: async (row: MetaDataSetType) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/type`, {
      method: 'POST',
      data: row,
    });
  },

  deleteDataSetType: async (row: MetaDataSetType) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/type/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteDataSetTypeBatch: async (rows: MetaDataSetType[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${RefdataService.url}/type/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  listDataSet: async (param: MetaDataSetParam) => {
    return request<PageResponse<MetaDataSet>>(`${RefdataService.url}/data`, {
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

  listDataSetByType: async (type: string) => {
    return request<ResponseBody<Array<MetaDataSet>>>(`${RefdataService.url}/data/type/${type}`, {
      method: 'GET'
    });
  },

  addDataSet: async (row: MetaDataSet) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/data`, {
      method: 'PUT',
      data: row,
    });
  },

  updateDataSet: async (row: MetaDataSet) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/data`, {
      method: 'POST',
      data: row,
    });
  },

  deleteDataSet: async (row: MetaDataSet) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/data/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteDataSetBatch: async (rows: MetaDataSet[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${RefdataService.url}/data/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  listDataMap: async (param: MetaDataMapParam) => {
    return request<PageResponse<MetaDataMap>>(`${RefdataService.url}/map`, {
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

  addDataMap: async (row: MetaDataMap) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/map`, {
      method: 'PUT',
      data: row,
    });
  },

  updateDataMap: async (row: MetaDataMap) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/map`, {
      method: 'POST',
      data: row,
    });
  },

  deleteDataMap: async (row: MetaDataMap) => {
    return request<ResponseBody<any>>(`${RefdataService.url}/map/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteDataMapBatch: async (rows: MetaDataMap[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${RefdataService.url}/map/batch`, {
      method: 'DELETE',
      data: params,
    });
  },


}
