/**分页参数 */
export const DEFAULT_PAGE_PARAM = {
  pageSize: 10,
  pageIndex: 1,
  pageParams: [10, 20, 50, 100],
  sorter: [{ field: '', direction: '' }],
};

export const USER_AUTH = {
  token: 'u_token',
  userInfo: 'u_info',
  pCodes: 'u_pCode',
  roleSysAdmin: 'sys_super_admin',
  expireTime: 'u_expire_time',
};

export const WORKSPACE_CONF = {
  projectId: 'projectId',
}
