import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {
  WsFlinkKubernetesJob,
  WsFlinkKubernetesJobAddParam, WsFlinkKubernetesJobInstanceDeployParam, WsFlinkKubernetesJobInstanceShutdownParam,
  WsFlinkKubernetesJobParam,
  WsFlinkKubernetesJobUpdateParam
} from './typings';

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

  asYaml: async (id: number) => {
    return request<ResponseBody<Record<string, any>>>(`${WsFlinkKubernetesJobService.url}/asYaml/` + id, {
      method: 'GET',
    });
  },

  add: async (param: WsFlinkKubernetesJobAddParam) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (param: WsFlinkKubernetesJobUpdateParam) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: WsFlinkKubernetesJob) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/${row.id}`, {
      method: 'DELETE'
    });
  },

  deleteBatch: async (rows: WsFlinkKubernetesJob[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  flinkui: async (row: WsFlinkKubernetesJob) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/${row.id}/flinkui`, {
      method: 'GET',
    }).then((response) => {
      const a = document.createElement('a');
      a.href = response.data;
      a.target = "_blank";
      a.click();
    });
  },

  deploy: async (param: WsFlinkKubernetesJobInstanceDeployParam) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/deploy`, {
      method: 'POST',
      data: param,
    });
  },

  shutdown: async (param: WsFlinkKubernetesJobInstanceShutdownParam) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/shutdown`, {
      method: 'POST',
      data: param,
    });
  },
};
