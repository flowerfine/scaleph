declare namespace Studio {
  type JobParams = {
    jobType: string;
  }; 
}

export type topBatch100Source = {
  id?: number;
  project?: any;
  jobId?: number;
  jobCode?: string;
  clusterId?: number;
  cluster?: any;
  jobInstanceId?: string;
  jobLogUrl?: string;
  startTime?: Date;
  endTime?: Date;
  duration?: number;
  jobInstanceState?: any;
}
