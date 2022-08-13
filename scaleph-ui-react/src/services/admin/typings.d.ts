import { Dict, QueryParam } from '@/app.d';

export type SysDictType = {
  id?: number;
  dictTypeCode?: string;
  dictTypeName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type SysDictTypeParam = QueryParam & {
  dictTypeCode?: string;
  dictTypeName?: string;
};

export type SysDictData = {
  id?: number;
  dictType?: SysDictType;
  dictCode?: string;
  dictValue?: string;
  remark?: string;
  isValid?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type SysDictDataParam = QueryParam & {
  dictTypeCode?: string;
  dictCode?: string;
  dictValue?: string;
  isValid?: string;
};

export type SecRole = {
  id?: number;
  roleCode?: string;
  roleName?: string;
  roleType?: Dict;
  roleStatus?: Dict;
  roleDesc?: string;
  showOpIcon?: boolean;
};

export type SecDept = {
  id?: number;
  deptCode?: string;
  deptName?: string;
  pid?: string;
  deptStatus?: Dict;
};

export type SecDeptTreeNode = {
  deptId: number;
  deptCode: string;
  deptName: string;
  pid: number;
  children: SecDeptTreeNode[];
};

export type SecUser = {
  id?: number;
  userName?: string;
  nickName?: string;
  email?: string;
  password?: string;
  realName?: string;
  idCardType?: Dict;
  idCardNo?: string;
  gender?: Dict;
  nation?: Dict;
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
};

export type SecUserParam = QueryParam & {
  userName?: string;
  nickName?: string;
  email?: string;
  userStatus?: string;
  roleId?: string;
  deptId?: string;
};

export type SecPrivilege = {
  id?: number;
  privilegeCode?: string;
  privilegeName: string;
  resourceType: Dict;
  resourcePath: string;
  pid: number;
};

export type SecPrivilegeTreeNode = {
  privilegeId: number;
  privilegeCode: string;
  privilegeName: string;
  pid: number;
  children: SecPrivilegeTreeNode[];
};

export type LogLogin = {
  id?: bigint;
  userName?: string;
  loginTime?: Date;
  ipAddress?: string;
  loginType?: Dict;
  clientInfo?: string;
  osInfo?: string;
  browserInfo?: string;
};

export type LogMessage = {
  id?: bigint;
  title?: string;
  messageType?: Dict;
  receiver?: string;
  sender?: string;
  content?: string;
  isRead?: Dict;
  isDelete?: Dict;
  createTime?: Date;
};

export type LogMessageParam = QueryParam & {
  receiver?: string;
  sender?: string;
  messageType?: string;
  isRead?: string;
};

export type EmailConfig = {
  email: string;
  password: string;
  host: string;
  port: number;
};

export type BasicConfig = {
  seatunnelHome: string;
};
