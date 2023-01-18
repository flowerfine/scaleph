const files = require.context('.', true, /index\.tsx$/);
const modules: { [key: string]: any } = {};

function isPromise(obj: Promise<any> | null | undefined) {
  return (
    obj !== null &&
    obj !== undefined &&
    typeof obj.then === 'function' &&
    typeof obj.catch === 'function'
  );
}

files.keys().forEach(async (key) => {
  const pathArr = key.replace(/(\.\/|\.tsx)/g, '').split('/');
  pathArr.pop();
  const moduleName = pathArr.join('/').replace(/\/\w{1}/g, function (val) {
    return val.substring(1, 2).toUpperCase();
  });
  const module = isPromise(files(key)) ? await files(key) : files(key);
  modules[moduleName] = module.default;
});
console.log(modules, 9999);
export default modules;
