import {Dict, QueryParam} from "@/app";

export type WorkflowDefinition = {
  id: number;
  type: Dict;
  name: string;
  executeType: Dict;
  param?: any;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type WorkflowDefinitionListParam = QueryParam & {
  type?: string;
  name?: string;
}

export type WorkflowTaskDefinition = {
  id: number;
  workflowDefinitionId: number;
  type?: Dict;
  name?: string;
  handler?: string;
  handler?: string;
  param?: any;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}
