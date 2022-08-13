/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */

//todo access
export default function access(initialState: { currentUser?: string } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canAdmin: currentUser && currentUser === 'admin',
  };
}
