/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */

import { AuthService } from './services/auth';

export default function access() {
  return {
    canAccess: (code: string) => {
      return AuthService.hasPrivilege(code);
    },
    normalRouteFilter: (route: any) => {
      return AuthService.hasPrivilege(route?.pCode);
    },
  };
}
