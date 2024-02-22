import {PageResponse, ResponseBody} from '@/typings';
import {
  WsArtifactFlinkSql,
  WsArtifactFlinkSqlSaveParam,
  WsArtifactFlinkSqlHistoryParam,
  WsArtifactFlinkSqlParam,
  WsArtifactFlinkSqlScriptUpdateParam,
  WsArtifactFlinkSqlSelectListParam
} from './typings';
import {request} from '@umijs/max';

export const FlinkArtifactSqlService = {
  url: '/api/artifact/flink/sql',

  list: async (queryParam: WsArtifactFlinkSqlParam) => {
    return request<PageResponse<WsArtifactFlinkSql>>(`${FlinkArtifactSqlService.url}`, {
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

  listByArtifact: async (queryParam: WsArtifactFlinkSqlHistoryParam) => {
    return request<PageResponse<WsArtifactFlinkSql>>(`${FlinkArtifactSqlService.url}/history`, {
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

  listAll: async (queryParam: WsArtifactFlinkSqlSelectListParam) => {
    return request<ResponseBody<Array<WsArtifactFlinkSql>>>(`${FlinkArtifactSqlService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  selectOne: async (id: number | string) => {
    return request<WsArtifactFlinkSql>(`${FlinkArtifactSqlService.url}/${id}`, {
      method: 'GET',
    });
  },

  add: async (row: WsArtifactFlinkSqlSaveParam) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsArtifactFlinkSqlSaveParam) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateScript: async (param: WsArtifactFlinkSqlScriptUpdateParam) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}/script`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: WsArtifactFlinkSql) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteArtifact: async (artifactId: number) => {
    return request<ResponseBody<any>>(`${FlinkArtifactSqlService.url}/artifact/${artifactId}`, {
      method: 'DELETE',
    });
  },

}
