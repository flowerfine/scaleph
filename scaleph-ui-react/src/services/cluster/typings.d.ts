import { Dict, QueryParam } from "@/app.d";

export type DiClusterConfig = {
  id?: number;
  clusterName?: string;
  clusterType?: Dict;
  clusterHome?: string;
  clusterVersion?: string;
  clusterConf?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DiClusterConfigParam = QueryParam & {
  clusterName?: string;
  clusterType?: string;
}