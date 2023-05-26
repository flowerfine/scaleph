import {PageResponse, ResponseBody} from '@/app.d';
import {
  WsFlinkArtifactJar,
  WsFlinkArtifactJarHistoryParam,
  WsFlinkArtifactSql,
  WsFlinkArtifactSqlAddParam,
  WsFlinkArtifactSqlParam, WsFlinkArtifactSqlScriptUpdateParam
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
    return request<PageResponse<WsFlinkArtifactJar>>(`${FlinkArtifactSqlService.url}/page`, {
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

  listAllByArtifact: async (id: string | number) => {
    return request<WsFlinkArtifactJar[]>(`${FlinkArtifactSqlService.url}/all`, {
      method: 'GET',
      params: {id: id}
    });
  },

  selectOne: async (id: number | string) => {
    return request<WsFlinkArtifactJar>(`${FlinkArtifactSqlService.url}/` + id, {
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

  deleteOne: async (row: WsFlinkArtifactJar) => {
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
