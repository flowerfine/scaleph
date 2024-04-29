import {PageResponse, QueryParam} from '@/typings';
import {request} from '@umijs/max';
import {GenericResource} from "@/services/kubernetes/typings";

export const OamDefinitionService = {
  url: '/api/oam/definition',

  listComponents: async (param: QueryParam) => {
    return request<PageResponse<GenericResource>>(`${OamDefinitionService.url}/components`, {
      method: 'GET',
      params: param
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

};
