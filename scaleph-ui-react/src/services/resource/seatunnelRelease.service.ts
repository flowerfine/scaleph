import { PageResponse, ResponseBody } from '@/app.d';
import { USER_AUTH } from '@/constant';
import {
  SeaTunnelRelease,
  SeaTunnelReleaseListParam,
  SeaTunnelReleaseUploadParam,
} from '@/services/resource/typings';
import { request } from '@@/exports';

export const SeatunnelReleaseService = {
  url: '/api/resource/seatunnel-release',

  list: async (queryParam: SeaTunnelReleaseListParam) => {
    return request<PageResponse<SeaTunnelRelease>>(`${SeatunnelReleaseService.url}`, {
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
    return request<ResponseBody<SeaTunnelRelease>>(`${SeatunnelReleaseService.url}/` + id, {
      method: 'GET',
    });
  },

  upload: async (uploadParam: SeaTunnelReleaseUploadParam) => {
    const formData = new FormData();
    formData.append('version', uploadParam.version);
    formData.append('file', uploadParam.file);
    if (uploadParam.remark) {
      formData.append('remark', uploadParam.remark);
    }
    return request<ResponseBody<any>>(`${SeatunnelReleaseService.url}/upload`, {
      method: 'POST',
      data: formData,
    });
  },
  download: async (row: SeaTunnelRelease) => {
    const a = document.createElement('a');
    a.href =
      `${SeatunnelReleaseService.url}/download/` +
      row.id +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    a.download = row.fileName + '';
    a.click();
    window.URL.revokeObjectURL(SeatunnelReleaseService.url);
  },
  deleteOne: async (row: SeaTunnelRelease) => {
    return request<ResponseBody<any>>(`${SeatunnelReleaseService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: SeaTunnelRelease[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${SeatunnelReleaseService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
