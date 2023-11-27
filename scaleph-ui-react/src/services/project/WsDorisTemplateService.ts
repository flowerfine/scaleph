import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {WsDorisTemplate, WsDorisTemplateAddParam, WsDorisTemplateParam, WsDorisTemplateUpdateParam} from './typings';

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
    const value: Record<string, any> = {
      'name': data.name,
      'namespace': data.namespace,
      'remark': data.remark,

      'admin.name': data.admin?.name,
      'admin.password': data.admin?.password,

      'fe.replicas': data.feSpec?.replicas,
      'fe.image': data.feSpec?.image,
      'fe.requests.cpu': data.feSpec?.requests?.cpu,
      'fe.requests.memory': data.feSpec?.requests?.memory,
      'fe.limits.cpu': data.feSpec?.limits?.cpu,
      'fe.limits.memory': data.feSpec?.limits?.memory,

      'be.replicas': data.beSpec?.replicas,
      'be.image': data.beSpec?.image,
      'be.requests.cpu': data.beSpec?.requests?.cpu,
      'be.requests.memory': data.beSpec?.requests?.memory,
      'be.limits.cpu': data.beSpec?.limits?.cpu,
      'be.limits.memory': data.beSpec?.limits?.memory,

      'cn.replicas': data.cnSpec?.replicas,
      'cn.image': data.cnSpec?.image,
      'cn.requests.cpu': data.cnSpec?.requests?.cpu,
      'cn.requests.memory': data.cnSpec?.requests?.memory,
      'cn.limits.cpu': data.cnSpec?.limits?.cpu,
      'cn.limits.memory': data.cnSpec?.limits?.memory,
    }

    return value
  }
};
