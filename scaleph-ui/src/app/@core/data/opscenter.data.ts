import { Dict, QueryParam } from './app.data';
import { DiClusterConfig, DiProject } from './datadev.data';

export class DiJobLog {
  id?: number;
  projectId: number;
  project?: DiProject;
  jobId: number;
  jobCode: string;
  clusterId: number;
  cluster?: DiClusterConfig;
  jobInstanceId: string;
  jobLogUrl: string;
  startTime?: Date;
  endTime?: Date;
  duration?: number;
  jobInstanceState?: Dict;
  createTime?: Date;
  updateTime?: Date;
}

export class DiJobLogParam extends QueryParam {
  projectId?: string;
  jobCode?: string;
  clusterId?: string;
  jobInstanceId?: string;
  startTime?: number;
  endTime?: number;
  jobInstanceState?: string;
  jobType: string;
}
