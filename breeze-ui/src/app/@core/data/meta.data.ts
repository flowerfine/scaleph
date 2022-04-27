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
  systemCode?: string;
  systemName?: string;
}

export class MetaDataElement {
  id?: number;
  elementCode: string;
  elementName: string;
  dataType?: Dict;
  dataLength?: number;
  dataPrecision?: number;
  dataScale?: number;
  nullable?: Dict;
  dataDefault?: string;
  lowValue?: string;
  highValue?: string;
  dataSetType?: MetaDataSetType;
  createTime?: Date;
  updateTime?: Date;
}

export class MetaDataElementParam extends QueryParam {
  elementCode?: string;
  elementName?: string;
}

export class MetaDataSetType {
  id?: number;
  dataSetTypeCode?: string;
  dataSetTypeName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class MetaDataSetTypeParam extends QueryParam {
  dataSetTypeCode?: string;
}

export class MetaDataSet {
  id?: number;
  dataSetType: MetaDataSetType;
  dataSetCode?: string;
  dataSetValue?: string;
  system: MetaSystem;
  isStandard: Dict;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class MetaDataSetParam extends QueryParam {
  dataSetTypeCode?: string;
  dataSetCode?: string;
  dataSetValue?: string;
}
