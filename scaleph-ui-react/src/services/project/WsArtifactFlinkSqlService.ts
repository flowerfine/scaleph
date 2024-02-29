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

export const WsArtifactFlinkSqlService = {
  url: '/api/artifact/flink/sql',

  list: async (queryParam: WsArtifactFlinkSqlParam) => {
    return request<PageResponse<WsArtifactFlinkSql>>(`${WsArtifactFlinkSqlService.url}`, {
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
    return request<PageResponse<WsArtifactFlinkSql>>(`${WsArtifactFlinkSqlService.url}/history`, {
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
    return request<ResponseBody<Array<WsArtifactFlinkSql>>>(`${WsArtifactFlinkSqlService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  selectOne: async (id: number | string) => {
    return request<WsArtifactFlinkSql>(`${WsArtifactFlinkSqlService.url}/${id}`, {
      method: 'GET',
    });
  },

  add: async (row: WsArtifactFlinkSqlSaveParam) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkSqlService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsArtifactFlinkSqlSaveParam) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkSqlService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateScript: async (param: WsArtifactFlinkSqlScriptUpdateParam) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkSqlService.url}/script`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: WsArtifactFlinkSql) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkSqlService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteArtifact: async (artifactId: number) => {
    return request<ResponseBody<any>>(`${WsArtifactFlinkSqlService.url}/artifact/${artifactId}`, {
      method: 'DELETE',
    });
  },

}
