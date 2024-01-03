import { AuthService } from "./services/admin/auth.service";

/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(
  initialState: { currentUser?: Scaleph.SysUser } | undefined
) {
  const { currentUser } = initialState ?? {};

  return {
    canAccess: (code: string) => {
      return Promise.resolve(true);
    },
    normalRouteFilter: (route: any) => {
      return Promise.resolve(true);
    },
  };
}
