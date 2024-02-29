import { PageResponse, ResponseBody } from '@/typings';
import { request } from '@umijs/max';
import { ResourceListParam } from './typings';

export const ResourceService = {
  url: '/api/resource',

  supportedResourceTypes: async () => {
    return request<ResponseBody<Array<string>>>(`${ResourceService.url}`, {
      method: 'GET',
    });
  },

  list: async (param: ResourceListParam) => {
    return request<PageResponse<any>>(`${ResourceService.url}/` + param.resourceType, {
      method: 'GET',
      params: param,
    });
  },
};
