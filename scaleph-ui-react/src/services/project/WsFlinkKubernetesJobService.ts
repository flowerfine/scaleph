import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {
  WsFlinkKubernetesJob,
  WsFlinkKubernetesJobAddParam,
  WsFlinkKubernetesJobInstance,
  WsFlinkKubernetesJobInstanceDeployParam,
  WsFlinkKubernetesJobInstanceParam, WsFlinkKubernetesJobInstanceSavepoint,
  WsFlinkKubernetesJobInstanceSavepointParam,
  WsFlinkKubernetesJobInstanceShutdownParam,
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

  flinkui: async (jobInstanceId: string) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/${jobInstanceId}/flinkui`, {
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

  restart: async (jobInstanceId: number) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/restart/${jobInstanceId}`, {
      method: 'POST'
    });
  },

  triggerSavepoint: async (jobInstanceId: number) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/triggerSavepoint/${jobInstanceId}`, {
      method: 'POST'
    });
  },

  suspend: async (jobInstanceId: number) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/suspend/${jobInstanceId}`, {
      method: 'POST'
    });
  },

  resume: async (jobInstanceId: number) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesJobService.url}/resume/${jobInstanceId}`, {
      method: 'POST'
    });
  },

  listInstances: async (queryParam: WsFlinkKubernetesJobInstanceParam) => {
    return request<PageResponse<WsFlinkKubernetesJobInstance>>(`${WsFlinkKubernetesJobService.url}/instances`, {
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

  listSavepoints: async (queryParam: WsFlinkKubernetesJobInstanceSavepointParam) => {
    return request<PageResponse<WsFlinkKubernetesJobInstanceSavepoint>>(`${WsFlinkKubernetesJobService.url}/instances/savepoint`, {
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

};
