import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {
  WsArtifactSeaTunnel,
  WsArtifactSeaTunnelGraphParam,
  WsArtifactSeaTunnelHistoryParam,
  WsArtifactSeaTunnelParam,
  WsArtifactSeaTunnelSaveParam,
  WsArtifactSeaTunnelSelectListParam
} from "@/services/project/typings";

export const WsArtifactSeaTunnelService = {
  url: '/api/artifact/seatunnel',

  list: async (queryParam: WsArtifactSeaTunnelParam) => {
    return request<PageResponse<WsArtifactSeaTunnel>>(`${WsArtifactSeaTunnelService.url}`, {
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

  listByArtifact: async (queryParam: WsArtifactSeaTunnelHistoryParam) => {
    return request<PageResponse<WsArtifactSeaTunnel>>(`${WsArtifactSeaTunnelService.url}/history`, {
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

  listAll: async (queryParam: WsArtifactSeaTunnelSelectListParam) => {
    return request<ResponseBody<Array<WsArtifactSeaTunnel>>>(`${WsArtifactSeaTunnelService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  selectOne: async (id: number | string) => {
    return request<WsArtifactSeaTunnel>(`${WsArtifactSeaTunnelService.url}/${id}`, {
      method: 'GET',
    });
  },

  preview: async (id: number) => {
    return request<ResponseBody<string>>(`${WsArtifactSeaTunnelService.url}/${id}/preview`, {
      method: 'GET',
    });
  },

  add: async (row: WsArtifactSeaTunnelSaveParam) => {
    return request<ResponseBody<any>>(`${WsArtifactSeaTunnelService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsArtifactSeaTunnelSaveParam) => {
    return request<ResponseBody<any>>(`${WsArtifactSeaTunnelService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateGraph: async (param: WsArtifactSeaTunnelGraphParam) => {
    return request<ResponseBody<any>>(`${WsArtifactSeaTunnelService.url}/graph`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: WsArtifactSeaTunnel) => {
    return request<ResponseBody<any>>(`${WsArtifactSeaTunnelService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteArtifact: async (artifactId: number) => {
    return request<ResponseBody<any>>(`${WsArtifactSeaTunnelService.url}/artifact/${artifactId}`, {
      method: 'DELETE',
    });
  },

  getDnds: async (engine: string) => {
    return request<ResponseBody<Array<Record<string, any>>>>(`${WsArtifactSeaTunnelService.url}/dag/dnd/${engine}`, {
      method: 'GET',
    });
  },
};
