import { Dict, QueryParam } from './app.data';

export class DataSourceMeta {
  id?: number;
  dataSourceName?: string;
  dataSourceType?: Dict;
  connectionType?: Dict;
  hostName?: string;
  databaseName?: string;
  port?: number;
  userName?: string;
  password?: string;
  remark?: string;
  generalProps?: string;
  jdbcProps?: string;
  // poolProps?: string;
  passwdChanged?: boolean;
  createTime?: Date;
  updateTime?: Date;
}

export class DataSourceMetaParam extends QueryParam {
  dataSourceName?: string;
  dataSourceType?: string;
  hostName?: string;
  databaseName?: string;
}

export class MetaSystem {
  id?: number;
  systemCode: string;
  systemName?: string;
  contacts?: string;
  contactsPhone?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class MetaSystemParam extends QueryParam {
  systemCode: string;
  systemName?: string;
}
