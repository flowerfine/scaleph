import {request} from 'umi';
import {PageResponse, ResponseBody} from '@/app.d';
import {WorkflowDefinition, WorkflowDefinitionListParam, WorkflowTaskDefinition} from "@/services/workflow/typings";

export const WorkflowService = {
  url: '/api/workflow',

  listWorkflowDefinitions: async (param: WorkflowDefinitionListParam) => {
    return request<PageResponse<WorkflowDefinition>>(`${WorkflowService.url}`, {
      method: 'GET',
      params: param,
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

  listWorkflowTaskDefinitions: async (workflowDefinitionId: number) => {
    return request<ResponseBody<Array<WorkflowTaskDefinition>>>(`${WorkflowService.url}/` + workflowDefinitionId, {
      method: 'GET'
    });
  },
};
