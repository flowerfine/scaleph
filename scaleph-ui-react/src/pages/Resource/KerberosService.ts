import {PageResponse, ResponseBody} from '@/app.d';
import {USER_AUTH} from '@/constant';
import {request} from 'umi';
import {Kerberos, KerberosListParam, KerberosUploadParam} from "@/pages/Resource/typings";

export const KerberosService = {
  url: '/api/resource/kerberos',

  list: async (queryParam: KerberosListParam) => {
    return request<PageResponse<Kerberos>>(`${KerberosService.url}`, {
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
    return request<ResponseBody<Kerberos>>(`${KerberosService.url}/` + id, {
      method: 'GET',
    });
  },

  upload: async (uploadParam: KerberosUploadParam) => {
    const formData = new FormData();
    formData.append('name', uploadParam.name);
    formData.append('principal', uploadParam.principal);
    formData.append('file', uploadParam.file);
    if (uploadParam.remark) {
      formData.append('remark', uploadParam.remark);
    }
    return request<ResponseBody<any>>(`${KerberosService.url}/upload`, {
      method: 'POST',
      data: formData,
    });
  },

  download: async (row: Kerberos) => {
    const a = document.createElement('a');
    a.href =
      `${KerberosService.url}/download/` +
      row.id +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    a.download = row.fileName + '';
    a.click();
    window.URL.revokeObjectURL(KerberosService.url);
  },

  deleteOne: async (row: Kerberos) => {
    return request<ResponseBody<any>>(`${KerberosService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
  deleteBatch: async (rows: Kerberos[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${KerberosService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
