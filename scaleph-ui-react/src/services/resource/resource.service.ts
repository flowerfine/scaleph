import { PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
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
