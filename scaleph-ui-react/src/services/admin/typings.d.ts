import {Dict, QueryParam} from '@/app.d';

export type SysDictType = {
  code: string;
  name: string;
  remark?: string;
};

export type SysDictTypeParam = QueryParam & {
  code?: string;
  name?: string;
};

export type SysDictData = {
  key: string,
  dictType?: SysDictType;
  value?: string;
  label?: string;
  remark?: string;
  valid?: boolean;
};

export type SysDictDataParam = QueryParam & {
  dictType?: string;
  value?: string;
  label?: string;
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

export type SecRoleParam = QueryParam & {
  roleName?: string;
  roleType?: string;
  roleStatus?: string;
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

export type SecDeptParam = QueryParam & {
  pid?: number;
  deptCode?: string;
  deptName?: string;
};

export type SecDeptTree = {
  id: number;
  deptCode: string;
  deptName: string;
  pid: number;
  deptStatus?: Dict;
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
  id: number;
  privilegeCode?: string;
  privilegeName: string;
  resourceType: Dict;
  resourcePath: string;
  pid: number;
  children?: SecPrivilege[]
};

export type SecPrivilegeParam = QueryParam & {
  pid: number;
  privilegeName?: string;
};

export type SecPrivilegeAddParam = {
  privilegeCode: string;
  privilegeName: string;
  resourceType: string;
  resourcePath?: string;
  pid: number;
};

export type SecPrivilegeUpdateParam = {
  id: number;
  privilegeCode?: string;
  privilegeName?: string;
  resourceType?: string;
  resourcePath?: string;
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
