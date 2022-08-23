import { Dict, QueryParam } from '@/app.d';

export type MetaDataSource = {
  id?: number;
  datasourceName?: string;
  datasourceType?: Dict;
  props?: any;
  propsStr?: string;
  additionalProps?: any;
  additionalPropsStr?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
  passwdChanged?: boolean;
};

export type MetaDataSourceParam = QueryParam & {
  datasourceName?: string;
  datasourceType?: string;
};

export type DiProject = {
  id?: number;
  projectCode?: string;
  projectName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type DiProjectParam = QueryParam & {
  projectCode?: string;
  projectName?: string;
};
