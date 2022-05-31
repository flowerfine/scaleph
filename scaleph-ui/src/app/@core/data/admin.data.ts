import { Dict, QueryParam } from './app.data';

export class SysDictType {
  id?: number;
  dictTypeCode?: string;
  dictTypeName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class SysDictTypeParam extends QueryParam {
  dictTypeCode?: string;
  dictTypeName?: string;
}

export class SysDictData {
  id?: number;
  dictType?: SysDictType;
  dictCode?: string;
  dictValue?: string;
  remark?: string;
  isValid?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class SysDictDataParam extends QueryParam {
  dictTypeCode?: string;
  dictCode?: string;
  dictValue?: string;
  isValid?: string;
}

export class SecRole {
  id?: number;
  roleCode?: string;
  roleName?: string;
  roleType?: Dict;
  roleStatus?: Dict;
  roleDesc?: string;
  showOpIcon?: boolean;
}

export class SecDept {
  id?: number;
  deptCode?: string;
  deptName?: string;
  pid?: string;
  deptStatus?: Dict;
}

export class SecUser {
  id?: number;
  userName?: string;
  nickName?: string;
  email?: string;
  password?: string;
  realName?: string;
  idCardType?: Dict;
  idCardNo?: string;
  gender?: Dict;
  nation?: string;
  birthday?: number;
  qq?: string;
  wechat?: string;
  mobilePhone?: string;
  userStatus?: Dict;
  summary?: string;
  registerChannel?: Dict;
  registerTime?: Date;
  registerIp?: string;
  roleCode?: string;
  deptCode?: string;
}

export class SecUserParam extends QueryParam {
  userName?: string;
  nickName?: string;
  email?: string;
  userStatus?: string;
  roleId?: string;
  deptId?: string;
}

export class SecPrivilege {
  id?: number;
}

export class LogLogin {
  id?: bigint;
  userName?: string;
  loginTime?: Date;
  ipAddress?: string;
  loginType?: Dict;
  clientInfo?: string;
  osInfo?: string;
  browserInfo?: string;
}

export class LogMessage {
  id?: bigint;
  title?: string;
  messageType?: Dict;
  receiver?: string;
  sender?: string;
  content?: string;
  isRead?: Dict;
  isDelete?: Dict;
  createTime?: Date;
}

export class LogMessageParam extends QueryParam {
  receiver?: string;
  sender?: string;
  messageType?: string;
  isRead?: string;
}

export class EmailConfig {
  email: string;
  password: string;
  host: string;
  port: number;
}

export class BasicConfig {
  seatunnelHome: string;
}
