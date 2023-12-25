import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {
  WsFlinkKubernetesSessionCluster,
  WsFlinkKubernetesSessionClusterParam,
  WsFlinkKubernetesSessionClusterSelectListParam
} from './typings';

export const WsFlinkKubernetesSessionClusterService = {
  url: '/api/flink/kubernetes/session-cluster',

  list: async (queryParam: WsFlinkKubernetesSessionClusterParam) => {
    return request<PageResponse<WsFlinkKubernetesSessionCluster>>(`${WsFlinkKubernetesSessionClusterService.url}`, {
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

  listAll: async (queryParam: WsFlinkKubernetesSessionClusterSelectListParam) => {
    return request<ResponseBody<Array<WsFlinkKubernetesSessionCluster>>>(`${WsFlinkKubernetesSessionClusterService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  selectOne: async (id: number) => {
    return request<ResponseBody<WsFlinkKubernetesSessionCluster>>(`${WsFlinkKubernetesSessionClusterService.url}/` + id, {
      method: 'GET',
    });
  },

  asYAML: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/asYAML`, {
      method: 'POST',
      data: {...row, deployed: row.deployed?.value, supportSqlGateway: row.supportSqlGateway?.value},
    });
  },

  fromTemplate: async (templateId: number) => {
    return request<ResponseBody<WsFlinkKubernetesSessionCluster>>(`${WsFlinkKubernetesSessionClusterService.url}/fromTemplate`, {
      method: 'GET',
      params: {templateId: templateId},
    });
  },

  add: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}`, {
      method: 'POST',
      data: {...row, deployed: row.deployed?.value, supportSqlGateway: row.supportSqlGateway?.value},
    });
  },

  enableSqlGateway: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/${row.id}/sql-gateway`, {
      method: 'POST'
    });
  },

  disableSqlGateway: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/${row.id}/sql-gateway`, {
      method: 'DELETE'
    });
  },

  delete: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsFlinkKubernetesSessionCluster[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  flinkui: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/${row.id}/flinkui`, {
      method: 'GET',
    }).then((response) => {
      const a = document.createElement('a');
      a.href = response.data;
      a.target = "_blank";
      a.click();
    });
  },

  deploy: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/deploy/${row.id}`, {
      method: 'POST',
    });
  },

  shutdown: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/shutdown/${row.id}`, {
      method: 'POST',
    });
  },

  status: async (row: WsFlinkKubernetesSessionCluster) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesSessionClusterService.url}/${row.id}/status`, {
      method: 'GET',
    });
  },

  getSqlGatewaySessionClusterId: async (projectId?: string | null) => {
    return request<string>(`${WsFlinkKubernetesSessionClusterService.url}/sql-gateway-session-cluster-id`, {
      method: 'GET',
      params: {projectId: projectId}
    })
  }

};
