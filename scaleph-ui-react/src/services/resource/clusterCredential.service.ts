import { PageResponse, ResponseBody } from '@/app.d';
import { USER_AUTH } from '@/constant';
import {
  ClusterCredential,
  ClusterCredentialListParam,
  CredentialFile,
  CredentialFileUploadParam,
} from '@/services/resource/typings';
import { request } from '@@/exports';
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
  add: async (row: ClusterCredential) => {
    return request<ResponseBody<any>>(`${ClusterCredentialService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: ClusterCredential) => {
    return request<ResponseBody<any>>(`${ClusterCredentialService.url}`, {
      method: 'POST',
      data: row,
    });
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

  listFiles: async (id: number) => {
    return request<Array<CredentialFile>>(`${ClusterCredentialService.url}/` + id + '/file', {
      method: 'GET',
    }).then((res) => {
      return { data: res };
    });
  },

  uploadFiles: async (uploadParam: CredentialFileUploadParam) => {
    const params: FormData = new FormData();
    uploadParam.files.forEach(function (file: string | Blob) {
      params.append('files', file);
    });
    return request<ResponseBody<any>>(
      `${ClusterCredentialService.url}/` + uploadParam.id + '/file',
      {
        method: 'POST',
        data: params,
      },
    );
  },

  downloadFile: async (id: number, row: CredentialFile) => {
    const a = document.createElement('a');
    a.href =
      `${ClusterCredentialService.url}/` +
      id +
      '/file/' +
      row.name +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    a.download = row.name + '';
    a.click();
    window.URL.revokeObjectURL(ClusterCredentialService.url);
  },
  deleteFiles: async (id: number, files: CredentialFile[]) => {
    const params = files.map((row) => row.name);
    return request<ResponseBody<any>>(`${ClusterCredentialService.url}/` + id + '/file', {
      method: 'DELETE',
      data: params,
    });
  },
};
