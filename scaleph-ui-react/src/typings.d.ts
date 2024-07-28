import {SecResourceWeb, SecRole} from "@/services/admin/typings";

declare module "slash2";
declare module "*.css";
declare module "*.less";
declare module "*.scss";
declare module "*.sass";
declare module "*.svg";
declare module "*.png";
declare module "*.jpg";
declare module "*.jpeg";
declare module "*.gif";
declare module "*.bmp";
declare module "*.tiff";
declare module "omit.js";
declare module "numeral";
declare module "@antv/data-set";
declare module "mockjs";
declare module "react-fittext";
declare module "bizcharts-plugin-slider";
declare module "react-cookies";

declare const REACT_APP_ENV: "dev" | "pre" | false;

export type QueryParam = {
  pageSize?: number;
  current?: number;
  sorter?: SoterField[];
};

export type SoterField = {
  field: string;
  direction: string;
};

type Dict = {
  label?: string;
  value?: string | number;
  remark?: string;
};

type ResponseBody<T> = {
  success: boolean;
  data?: T;
  errorCode?: string;
  errorMessage?: string;
  showType?: string;
};

export type PageResponse<T> = {
  size: number;
  current: number;
  total: number;
  records: T[];
};

type Page<T> = {
  pageSize: number;
  current: number;
  total: number;
  data: T[];
};

export type Props<T> = {
  data: T;
};

type ModalProps<T> = {
  open?: boolean;
  data?: T;
  handleOk?: (isOpen: boolean, value?: any) => void;
  handleCancel?: () => void;
};

export type ModalFormProps<T> = {
  data: T;
  visible: boolean;
  onVisibleChange?: (visible: boolean) => void;
  onCancel: () => void;
  onOK?: (values: any) => void;
};

type AuthCode = {
  uuid: string;
  img: string;
};

type LoginInfo = {
  userName: string;
  password: string;
  authCode: string;
  uuid: string;
  autoLogin: boolean;
};

type RegisterInfo = {
  userName: string;
  email: string;
  password: string;
  confirmPassword: string;
  authCode: string;
  uuid: string;
};

export type OnlineUserInfo = {
  token: string;
  userId?: number;
  type?: string;
  status?: string;
  userName?: string;
  nickName?: string;
  roles?: Array<SecRole>;
  resourceWebs?: Array<SecResourceWeb>;

  email?: string;
  privileges?: string[];
  roles?: string[];
  expireTime?: bigint;
};


export type TreeNode = {
  key: React.Key;
  title: React.ReactNode;
  children?: TreeNode2[];
  origin?: any;
  showOpIcon?: boolean;
};

export type TransferData = {
  name: string;
  value: string;
};
