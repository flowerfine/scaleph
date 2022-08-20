/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */

import { hasPrivilege } from './services/auth';

export default function access() {
  return {
    canAccess: (code: string) => {
      return hasPrivilege(code);
    },
    normalRouteFilter: (route: any) => {
      return hasPrivilege(route?.pCode);
    },
  };
}
