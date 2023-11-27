import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {
  WsDorisTemplate,
  WsDorisTemplateAddParam,
  WsDorisTemplateParam,
  WsDorisTemplateUpdateParam,
  WsFlinkKubernetesTemplate
} from './typings';

export const WsDorisTemplateService = {
  url: '/api/doris/template',

  list: async (queryParam: WsDorisTemplateParam) => {
    return request<PageResponse<WsDorisTemplate>>(`${WsDorisTemplateService.url}`, {
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

  asYaml: async (param: WsDorisTemplate) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}/asYaml`, {
      method: 'POST',
      data: param,
    });
  },

  add: async (param: WsDorisTemplateAddParam) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (param: WsDorisTemplateUpdateParam) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  delete: async (row: WsDorisTemplate) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsDorisTemplate[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}/` + 'batch', {
      method: 'DELETE',
      data: params,
    });
  },

  formatData: (data: WsDorisTemplate, value: Record<string, any>) => {
    const admin: Record<string, any> = {
      name: value['admin.name'],
      password: value['admin.password'],
    }
    data.admin = admin

    const feSpec: Record<string, any> = {
      replicas: value['fe.replicas'],
      image: value['fe.image']
    }
    const feSpecRequests: Record<string, any> = {
      cpu: value['fe.requests.cpu'],
      memory: value['fe.requests.memory']
    }
    const feSpecLimits: Record<string, any> = {
      cpu: value['fe.limits.cpu'],
      memory: value['fe.limits.memory']
    }
    feSpec.requests = feSpecRequests
    feSpec.limits = feSpecLimits
    data.feSpec = feSpec

    const beSpec: Record<string, any> = {
      replicas: value['be.replicas'],
      image: value['be.image']
    }
    const beSpecRequests: Record<string, any> = {
      cpu: value['be.requests.cpu'],
      memory: value['be.requests.memory']
    }
    const beSpecLimits: Record<string, any> = {
      cpu: value['be.limits.cpu'],
      memory: value['be.limits.memory']
    }
    beSpec.requests = beSpecRequests
    beSpec.limits = beSpecLimits
    data.beSpec = beSpec

    const cnSpec: Record<string, any> = {
      replicas: value['cn.replicas'],
      image: value['cn.image']
    }
    const cnSpecRequests: Record<string, any> = {
      cpu: value['cn.requests.cpu'],
      memory: value['cn.requests.memory']
    }
    const cnSpecLimits: Record<string, any> = {
      cpu: value['cn.limits.cpu'],
      memory: value['cn.limits.memory']
    }
    cnSpec.requests = cnSpecRequests
    cnSpec.limits = cnSpecLimits
    data.cnSpec = cnSpec
    return data
  },

  parseData: (data: WsDorisTemplate) => {
    const value: Record<string, any> = {}

    return value
  }
};
