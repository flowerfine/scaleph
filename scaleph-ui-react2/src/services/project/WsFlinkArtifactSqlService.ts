import {PageResponse, ResponseBody} from '@/app.d';
import {
  WsFlinkArtifactJarHistoryParam,
  WsFlinkArtifactSql,
  WsFlinkArtifactSqlAddParam,
  WsFlinkArtifactSqlParam,
  WsFlinkArtifactSqlScriptUpdateParam,
  WsFlinkArtifactSqlSelectListParam
} from './typings';
import {request} from 'umi';

export const FlinkArtifactSqlService = {
  url: '/api/flink/artifact/sql',

  list: async (queryParam: WsFlinkArtifactSqlParam) => {
    return request<PageResponse<WsFlinkArtifactSql>>(`${FlinkArtifactSqlService.url}`, {
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

  listPageByArtifact: async (queryParam: WsFlinkArtifactJarHistoryParam) => {
    return request<PageResponse<WsFlinkArtifactSql>>(`${FlinkArtifactSqlService.url}/page`, {
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

  listAll: async (queryParam: WsFlinkArtifactSqlSelectListParam) => {
    return request<ResponseBody<Array<WsFlinkArtifactSql>>>(`${FlinkArtifactSqlService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  selectOne: async (id: number | string) => {
    return request<WsFlinkArtifactSql>(`${FlinkArtifactSqlService.url}/` + id, {
      method: 'GET',
    });
  },

  add: async (row: WsFlinkArtifactSqlAddParam) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsFlinkArtifactSqlAddParam) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateScript: async (param: WsFlinkArtifactSqlScriptUpdateParam) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}/script`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: WsFlinkArtifactSql) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteAll: async (flinkArtifactId: number) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}/all/` + flinkArtifactId, {
      method: 'DELETE',
    });
  },

}
