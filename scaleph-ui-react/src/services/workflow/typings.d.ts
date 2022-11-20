import {Dict, QueryParam} from "@/app";

export type WorkflowDefinition = {
  id: number;
  type: Dict;
  name: string;
  executeType: Dict;
  param?: any;
  remark?: string;
  schedule?: WorkflowSchedule;
  createTime: Date;
  updateTime: Date;
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
  param?: any;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type WorkflowSchedule = {
  id: number;
  workflowDefinitionId: number;
  timezone: string;
  crontab: string;
  startTime: Date;
  endTime: Date;
  status: Dict;
  remark: string;
  createTime: Date;
  updateTime: Date;
}

export type WorkflowScheduleListParam = {
  workflowDefinitionId: number;
  status?: string;
}

export type WorkflowScheduleAddParam = {
  workflowDefinitionId: number;
  timezone: string;
  crontab: string;
  startTime: string;
  endTime: string;
  remark: string;
}

export type WorkflowScheduleUpdateParam = {
  timezone: string;
  crontab: string;
  startTime: string;
  endTime: string;
  remark: string;
}
