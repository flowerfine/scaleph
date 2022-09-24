import { PageResponse, ResponseBody } from '@/app.d';
import { USER_AUTH } from '@/constant';
import { Jar, JarListParam, JarUploadParam } from '@/services/resource/typings';
import { request } from 'umi';

export const ResourceJarService = {
  url: '/api/resource/jar',

  list: async (queryParam: JarListParam) => {
    return request<PageResponse<Jar>>(`${ResourceJarService.url}`, {
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
    return request<ResponseBody<Jar>>(`${ResourceJarService.url}/` + id, {
      method: 'GET',
    });
  },

  upload: async (uploadParam: JarUploadParam) => {
    const formData = new FormData();
    formData.append('group', uploadParam.group);
    formData.append('file', uploadParam.file);
    if (uploadParam.remark) {
      formData.append('remark', uploadParam.remark);
    }
    return request<ResponseBody<any>>(`${ResourceJarService.url}/upload`, {
      method: 'POST',
      data: formData,
    });
  },

  download: async (row: Jar) => {
    const a = document.createElement('a');
    a.href =
      `${ResourceJarService.url}/download/` +
      row.id +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    a.download = row.fileName + '';
    a.click();
    window.URL.revokeObjectURL(ResourceJarService.url);
  },

  deleteOne: async (row: Jar) => {
    return request<ResponseBody<any>>(`${ResourceJarService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
  deleteBatch: async (rows: Jar[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${ResourceJarService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
