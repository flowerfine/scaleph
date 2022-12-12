import { PageResponse, ResponseBody } from '@/app.d';
import { USER_AUTH } from '@/constant';
import { FlinkArtifactJar, FlinkArtifactJarParam } from './typings';
import { request } from 'umi';

export const FlinkArtifactJarService = {
  url: '/api/flink/artifact/jar',

  list: async (queryParam: FlinkArtifactJarParam) => {
    return request<PageResponse<FlinkArtifactJar>>(`${FlinkArtifactJarService.url}`, {
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
  update: async (row: FlinkArtifactJar) => {
    return request<ResponseBody<any>>(`${FlinkArtifactJarService.url}`, {
      method: 'POST',
      data: row,
    });
  },
  listByArtifact: async (id: string | number) => {
    return request<FlinkArtifactJar[]>(`${FlinkArtifactJarService.url}/artifact/` + id, {
      method: 'GET',
    });
  },
  selectOne: async (id: number | string) => {
    return request<FlinkArtifactJar>(`${FlinkArtifactJarService.url}/` + id, {
      method: 'GET',
    });
  },
  deleteOne: async (row: FlinkArtifactJar) => {
    return request<ResponseBody<any>>(`${FlinkArtifactJarService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
  upload: async (uploadParam: FlinkArtifactJar) => {
    return request<ResponseBody<any>>(`${FlinkArtifactJarService.url}`, {
      method: 'PUT',
      data: uploadParam,
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  download: async (row: FlinkArtifactJar) => {
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
