import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {WsFlinkKubernetesJob, WsFlinkKubernetesJobAddParam, WsFlinkKubernetesJobParam} from './typings';

export const WsFlinkKubernetesJobService = {
  url: '/api/flink/kubernetes/job',

  list: async (queryParam: WsFlinkKubernetesJobParam) => {
    return request<PageResponse<WsFlinkKubernetesJob>>(`${WsFlinkKubernetesJobService.url}`, {
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

  selectOne: async (id: number) => {
    return request<ResponseBody<WsFlinkKubernetesJob>>(`${WsFlinkKubernetesJobService.url}/` + id, {
      method: 'GET',
    });
  },

  add: async (param: WsFlinkKubernetesJobAddParam) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/`, {
      method: 'PUT',
      data: param,
    });
  },

};
