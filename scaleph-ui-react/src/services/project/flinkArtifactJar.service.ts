import { PageResponse } from '@/app.d';
import { USER_AUTH } from '@/constant';
import {
  FlinkArtifactJar,
  FlinkArtifactJarListParam,
  FlinkArtifactJarUploadParam,
} from '@/services/dev/typings';
import { request } from 'umi';

export const FlinkArtifactJarService = {
  url: '/api/flink/artifact/jar',

  list: async (queryParam: FlinkArtifactJarListParam) => {
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
  selectOne: async (id: number) => {
    return request<FlinkArtifactJar>(`${FlinkArtifactJarService.url}/` + id, {
      method: 'GET',
    });
  },
  upload: async (uploadParam: FlinkArtifactJarUploadParam) => {
    const formData = new FormData();
    formData.append('flinkArtifactId', uploadParam.flinkArtifactId);
    if (uploadParam.version) {
      formData.append('version', uploadParam.version);
    }
    formData.append('flinkVersion', uploadParam.flinkVersion);
    formData.append('entryClass', uploadParam.entryClass);
    formData.append('file', uploadParam.file);
    return request<ResponseBody<any>>(`${FlinkArtifactJarService.url}`, {
      method: 'POST',
      data: formData,
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
