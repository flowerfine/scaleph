import {QueryParam} from "@/app";

export type Kerberos = {
  id?: number;
  name?: string;
  principal?: string;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type KerberosListParam = QueryParam & {
  name?: string;
  fileName?: string;
}

export type KerberosUploadParam = QueryParam & {
  name: string;
  principal: string;
  file: File;
  remark?: String;
}
