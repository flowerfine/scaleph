import {Dict, QueryParam} from "@/app";

export type MetaSystem = {
  id?: number;
  systemCode?: string;
  systemName?: string;
  contacts?: string;
  contactsPhone?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type MetaSystemParam = QueryParam & {
  systemCode?: string;
  systemName?: string;
}

export type MetaDataElement = {
  id?: number;
  elementCode?: string;
  elementName?: string;
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

export type MetaDataElementParam = QueryParam & {
  elementCode?: string;
  elementName?: string;
}

export type MetaDataSetType = {
  id?: number;
  dataSetTypeCode?: string;
  dataSetTypeName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type MetaDataSetTypeParam = QueryParam & {
  dataSetTypeCode?: string;
}

export type MetaDataSet = {
  id?: number;
  dataSetType?: MetaDataSetType;
  dataSetCode?: string;
  dataSetValue?: string;
  system?: MetaSystem;
  isStandard?: Dict;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type MetaDataSetParam = QueryParam & {
  dataSetTypeCode?: string;
  dataSetCode?: string;
  dataSetValue?: string;
}

export type MetaDataMap = {
  id?: number;
  srcDataSetTypeId?: number;
  srcDataSetTypeCode?: string;
  srcDataSetTypeName?: string;
  srcDataSetId?: number;
  srcDataSetCode?: string;
  srcDataSetValue?: string;
  tgtDataSetTypeId?: number;
  tgtDataSetTypeCode?: string;
  tgtDataSetTypeName?: string;
  tgtDataSetId?: number;
  tgtDataSetCode?: string;
  tgtDataSetValue?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type MetaDataMapParam = QueryParam & {
  srcDataSetTypeCode?: string;
  tgtDataSetTypeCode?: string;
  srcDataSetCode?: string;
  tgtDataSetCode?: string;
  auto?: boolean;
}
