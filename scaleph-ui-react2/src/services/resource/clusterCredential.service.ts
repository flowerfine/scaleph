import {PageResponse, ResponseBody} from '@/app.d';
import {
  ClusterCredential,
  ClusterCredentialListParam,
  ClusterCredentialUploadParam,
} from '@/services/resource/typings';
import {request} from 'umi';
import {USER_AUTH} from "@/constants/constant";

export const ClusterCredentialService = {
  url: '/api/resource/cluster-credential',

  list: async (queryParam: ClusterCredentialListParam) => {
    return request<PageResponse<ClusterCredential>>(`${ClusterCredentialService.url}`, {
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
    return request<ResponseBody<ClusterCredential>>(`${ClusterCredentialService.url}/` + id, {
      method: 'GET',
    });
  },

  upload: async (uploadParam: ClusterCredentialUploadParam) => {
    const formData = new FormData();
    formData.append('name', uploadParam.name);
    if (uploadParam.context) {
      formData.append('context', uploadParam.context);
    }
    formData.append('file', uploadParam.file);
    if (uploadParam.remark) {
      formData.append('remark', uploadParam.remark);
    }
    return request<ResponseBody<any>>(`${ClusterCredentialService.url}/upload`, {
      method: 'POST',
      data: formData,
    });
  },

  download: async (row: ClusterCredential) => {
    const a = document.createElement('a');
    a.href =
      `${ClusterCredentialService.url}/download/` +
      row.id +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    a.download = row.fileName + '';
    a.click();
    window.URL.revokeObjectURL(ClusterCredentialService.url);
  },

  deleteOne: async (row: ClusterCredential) => {
    return request<ResponseBody<any>>(`${ClusterCredentialService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: ClusterCredential[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${ClusterCredentialService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

};
