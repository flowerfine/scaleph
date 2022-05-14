import { Dict, QueryParam } from './app.data';

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

export class MetaDataMap {
  id?: number;
  srcDataSetTypeCode?: string;
  srcDataSetTypeName?: string;
  srcDataSetId?: number;
  srcDataSetCode?: string;
  srcDataSetValue?: string;
  tgtDataSetTypeCode?: string;
  tgtDataSetTypeName?: string;
  tgtDataSetId?: number;
  tgtDataSetCode?: string;
  tgtDataSetValue?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class MetaDataMapParam extends QueryParam {
  srcDataSetTypeCode?: string;
  tgtDataSetTypeCode?: string;
  srcDataSetCode?: string;
  tgtDataSetCode?: string;
  auto?: boolean;
}
