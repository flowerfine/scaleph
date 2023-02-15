import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {WsFlinkKubernetesDeployment, WsFlinkKubernetesDeploymentParam} from './typings';

export const WsFlinkKubernetesDeploymentService = {
  url: '/api/flink/kubernetes/deployment',

  list: async (queryParam: WsFlinkKubernetesDeploymentParam) => {
    return request<PageResponse<WsFlinkKubernetesDeployment>>(`${WsFlinkKubernetesDeploymentService.url}`, {
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
    return request<ResponseBody<WsFlinkKubernetesDeployment>>(`${WsFlinkKubernetesDeploymentService.url}/` + id, {
      method: 'GET',
    });
  },

  asYaml: async (id: number) => {
    return request<ResponseBody<WsFlinkKubernetesDeployment>>(`${WsFlinkKubernetesDeploymentService.url}/asYaml/` + id, {
      method: 'GET',
    });
  },

  add: async (row: WsFlinkKubernetesDeployment) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsFlinkKubernetesDeployment) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  delete: async (row: WsFlinkKubernetesDeployment) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsFlinkKubernetesDeployment[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

};
