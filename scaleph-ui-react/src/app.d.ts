import { extend } from 'lodash';

export type AuthCode = {
  uuid: string;
  img: string;
};

export interface ResponseBody<T> {
  success: boolean;
  data?: T;
  errorCode?: string;
  errorMessage: string;
  showType?: string;
}

export type PageResponse<T> = {
  size: number;
  current: number;
  total: number;
  records: T[];
};

export type TransferData = {
  name: string;
  value: string;
};

export type RegisterInfo = {
  userName: string;
  email: string;
  password: string;
  confirmPassword: string;
  authCode: string;
  uuid: string;
};

/**登录参数 */
export type LoginInfo = {
  userName: string;
  password: string;
  authCode: string;
  uuid: string;
  remember: boolean;
};

export type OnlineUserInfo = {
  userName?: string;
  email?: string;
  token?: string;
  privileges?: string[];
  roles?: string[];
  expireTime?: bigint;
};

export type SoterField = {
  field: string;
  direction: string;
};

/**分页参数 */
export type QueryParam = {
  pageSize?: number;
  current?: number;
  sorter?: SoterField[];
};
/**
 * 枚举的key和value
 */
export type Dict = {
  label?: string;
  value?: string | number;
};

export type ModalFormProps<T> = {
  data: T;
  visible: boolean;
  onVisibleChange?: (visible: boolean) => void;
  onCancel: () => void;
  onOK?: (values: any) => void;
};

export type TreeNode = {
  key: React.Key;
  title: React.ReactNode;
  children?: TreeNode[];
  origin?: any;
  showOpIcon?: boolean;
};

export type CascaderOption = {
  value: string | number;
  label?: React.ReactNode;
  disabled?: boolean;
  children?: Option[];
  isLeaf?: boolean;
};
