import {Dict, QueryParam} from "@/typings";

export type SysDictDefinition = {
  code: string;
  name: string;
  provider: string;
  remark?: string;
};

export type SysDictDefinitionParam = QueryParam & {
  code?: string;
  name?: string;
};

export type SysDictInstance = {
  value?: string;
  label?: string;
  remark?: string;
  valid?: boolean;
};

export type SysDictInstanceParam = QueryParam & {
  dictDefinitionCode?: string;
  value?: string;
  label?: string;
};

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

export type SecUser = {
  id?: number;
  type?: Dict;
  userName?: string;
  nickName?: string;
  avatar?: string;
  email?: string;
  phone?: string;
  password?: string;
  order?: number;
  status?: Dict;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type SecUserParam = QueryParam & {
  userName?: string;
  nickName?: string;
  email?: string;
  userStatus?: string;
  roleId?: string;
  deptId?: string;
};

export type SecRole = {
  id?: number;
  type?: Dict;
  code?: string;
  name?: string;
  order?: number;
  status?: Dict;
  remark?: string;
  createTime: Date;
  updateTime: Date;
};

export type SecRoleParam = QueryParam & {
  type?: string;
  name?: string;
  status?: string;
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

export type SecResourceWeb = {
  id: number;
  type: Dict;
  pid: number;
  value: string;
  label: string;
  path: string;
  order?: number;
  status: Dict;
  remark?: string;
  children?: SecResourceWeb[];
  authorized?: Dict
};

export type SecResourceWebParam = QueryParam & {
  pid: number;
  name?: string;
};

export type SecResourceWebAddParam = {
  type: string;
  pid: number;
  name?: string;
  path?: string;
  redirect?: string;
  layout?: boolean;
  icon?: string;
  component?: string;
  remark?: string;
};

export type SecResourceWebUpdateParam = {
  id: number;
  type: string;
  pid: number;
  name?: string;
  path?: string;
  redirect?: string;
  layout?: boolean;
  icon?: string;
  component?: string;
  remark?: string;
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
