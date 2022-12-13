import { PageResponse, ResponseBody } from '@/app.d';
import { USER_AUTH } from '@/constant';
import { WsFlinkArtifactJar, WsFlinkArtifactJarParam } from './typings';
import { request } from 'umi';

export const FlinkArtifactJarService = {
  url: '/api/flink/artifact/jar',

  list: async (queryParam: WsFlinkArtifactJarParam) => {
    return request<PageResponse<WsFlinkArtifactJar>>(`${FlinkArtifactJarService.url}`, {
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
  update: async (row: WsFlinkArtifactJar) => {
    return request<ResponseBody<any>>(`${FlinkArtifactJarService.url}`, {
      method: 'POST',
      data: row,
    });
  },
  listByArtifact: async (id: string | number) => {
    return request<WsFlinkArtifactJar[]>(`${FlinkArtifactJarService.url}/artifact/` + id, {
      method: 'GET',
    });
  },
  selectOne: async (id: number | string) => {
    return request<WsFlinkArtifactJar>(`${FlinkArtifactJarService.url}/` + id, {
      method: 'GET',
    });
  },
  deleteOne: async (row: WsFlinkArtifactJar) => {
    return request<ResponseBody<any>>(`${FlinkArtifactJarService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
  upload: async (uploadParam: WsFlinkArtifactJar) => {
    return request<ResponseBody<any>>(`${FlinkArtifactJarService.url}`, {
      method: 'PUT',
      data: uploadParam,
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  download: async (row: WsFlinkArtifactJar) => {
    const a = document.createElement('a');
    a.href =
      `${FlinkArtifactJarService.url}/download/` +
      row.id +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    a.download = row.fileName + '';
    a.click();
    window.URL.revokeObjectURL(FlinkArtifactJarService.url);
  },
};
