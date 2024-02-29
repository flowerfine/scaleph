import {PageResponse, ResponseBody} from '@/typings';
import {USER_AUTH} from '@/constants/constant';
import {
  WsArtifactFlinkJar,
  WsArtifactFlinkJarHistoryParam,
  WsArtifactFlinkJarParam,
  WsArtifactFlinkJarSelectListParam,
  WsArtifactFlinkJarUpdateParam,
  WsArtifactFlinkJarUploadParam
} from './typings';
import {request} from '@umijs/max';

export const WsArtifactFlinkJarService = {
  url: '/api/artifact/flink/jar',

  list: async (queryParam: WsArtifactFlinkJarParam) => {
    return request<PageResponse<WsArtifactFlinkJar>>(`${WsArtifactFlinkJarService.url}`, {
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

  listByArtifact: async (queryParam: WsArtifactFlinkJarHistoryParam) => {
    return request<PageResponse<WsArtifactFlinkJar>>(`${WsArtifactFlinkJarService.url}/history`, {
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

  listAll: async (queryParam: WsArtifactFlinkJarSelectListParam) => {
    return request<ResponseBody<Array<WsArtifactFlinkJar>>>(`${WsArtifactFlinkJarService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  selectOne: async (id: number | string) => {
    return request<WsArtifactFlinkJar>(`${WsArtifactFlinkJarService.url}/${id}`, {
      method: 'GET',
    });
  },

  deleteOne: async (row: WsArtifactFlinkJar) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkJarService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteArtifact: async (artifactId: number) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkJarService.url}/artifact/${artifactId}`, {
      method: 'DELETE',
    });
  },

  update: async (row: WsArtifactFlinkJar) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkJarService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  upload: async (uploadParam: WsArtifactFlinkJarUploadParam) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkJarService.url}`, {
      method: 'PUT',
      data: uploadParam,
      headers: {'Content-Type': 'multipart/form-data'},
    });
  },

  updateJar: async (uploadParam: WsArtifactFlinkJarUpdateParam) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkJarService.url}/jar`, {
      method: 'POST',
      data: uploadParam,
      headers: {'Content-Type': 'multipart/form-data'},
    });
  },

  download: async (row: WsArtifactFlinkJar) => {
    const a = document.createElement('a');
    a.href = `${WsArtifactFlinkJarService.url}/download/${row.id}?${USER_AUTH.token}=${localStorage.getItem(USER_AUTH.token)}`;
    a.download = row.fileName + '';
    a.click();
    window.URL.revokeObjectURL(WsArtifactFlinkJarService.url);
  },
};
