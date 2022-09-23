import { PageResponse, ResponseBody } from '@/app.d';
import { USER_AUTH } from '@/constant';
import {
  FlinkRelease,
  FlinkReleaseListParam,
  FlinkReleaseUploadParam,
} from '@/services/resource/typings';
import { request } from '@@/exports';

export const FlinkReleaseService = {
  url: '/api/resource/flink-release',

  list: async (queryParam: FlinkReleaseListParam) => {
    return request<PageResponse<FlinkRelease>>(`${FlinkReleaseService.url}`, {
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
    return request<ResponseBody<FlinkRelease>>(`${FlinkReleaseService.url}/` + id, {
      method: 'GET',
    });
  },

  upload: async (uploadParam: FlinkReleaseUploadParam) => {
    const formData = new FormData();
    formData.append('version', uploadParam.version);
    formData.append('file', uploadParam.file);
    if (uploadParam.remark) {
      formData.append('remark', uploadParam.remark);
    }
    return request<ResponseBody<any>>(`${FlinkReleaseService.url}/upload`, {
      method: 'POST',
      data: formData,
    });
  },

  download: async (row: FlinkRelease) => {
    const a = document.createElement('a');
    a.href =
      `${FlinkReleaseService.url}/download/` +
      row.id +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    a.download = row.fileName + '';
    a.click();
    window.URL.revokeObjectURL(FlinkReleaseService.url);
  },

  deleteOne: async (row: FlinkRelease) => {
    return request<ResponseBody<any>>(`${FlinkReleaseService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: FlinkRelease[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${FlinkReleaseService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
