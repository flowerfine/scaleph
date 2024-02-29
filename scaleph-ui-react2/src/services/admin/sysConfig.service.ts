import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { BasicConfig, EmailConfig } from './typings';

export const SysConfigService = {
  url: '/api/admin/config',

  getEmailConfig: async () => {
    return request<EmailConfig>(`${SysConfigService.url}` + '/email', {
      method: 'GET',
    });
  },

  configEmail: async (info: EmailConfig) => {
    return request<ResponseBody<any>>(`${SysConfigService.url}` + '/email', {
      method: 'PUT',
      data: info,
    });
  },

  getBasicConfig: async () => {
    return request<BasicConfig>(`${SysConfigService.url}` + '/basic', {
      method: 'GET',
    });
  },

  configBasic: async (info: BasicConfig) => {
    return request<ResponseBody<any>>(`${SysConfigService.url}` + '/basic', {
      method: 'PUT',
      data: info,
    });
  },
};
