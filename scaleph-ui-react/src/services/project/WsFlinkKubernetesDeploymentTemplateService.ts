import {PageResponse, ResponseBody} from '@/app.d';
import {request} from '@@/exports';
import {WsFlinkKubernetesDeploymentTemplate, WsFlinkKubernetesDeploymentTemplateParam} from './typings';

export const WsFlinkKubernetesDeploymentTemplateService = {
  url: '/api/flink/job',

  list: async (queryParam: WsFlinkKubernetesDeploymentTemplateParam) => {
    return request<PageResponse<WsFlinkKubernetesDeploymentTemplate>>(`${WsFlinkKubernetesDeploymentTemplateService.url}`, {
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
    return request<ResponseBody<WsFlinkKubernetesDeploymentTemplate>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/` + id, {
      method: 'GET',
    });
  },

  mergeDefault: async (row: any) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/default`, {
      method: 'PATCH',
      data: row,
    });
  },

  add: async (row: WsFlinkKubernetesDeploymentTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsFlinkKubernetesDeploymentTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  delete: async (row: WsFlinkKubernetesDeploymentTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsFlinkKubernetesDeploymentTemplate[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
