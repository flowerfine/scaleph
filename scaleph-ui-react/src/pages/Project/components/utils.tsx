import lodash from 'lodash';
// 模糊匹配树并且高亮
export function approximateTreeNode(treeData:any, target: string, isDelete = true) {
  if (target) {
    const newTree = lodash.cloneDeep(treeData || []);
    newTree.map((item:any, index:any) => {
      // 暂时不递归，只搜索datasource
      // if(item.children?.length){
      //   item.children = approximateTreeNode(item.children, target,false);
      // }
      if (item.name?.toUpperCase()?.indexOf(target?.toUpperCase()) == -1 && isDelete) {
        delete newTree[index];
      } else {
        item.name = item.name?.replace(target, `<span style='color:red;'>${target}</span>`);
      }
    });
    return newTree.filter((i) => i);
  } else {
    return treeData;
  }
}

export enum TreeNodeType {
  DATA_SOURCES = 'dataSources',
  DATA_SOURCE = 'dataSource',
  DATABASE = 'database',
  SCHEMAS = 'schemas',
  TABLES = 'tables',
  TABLE = 'table',
  COLUMNS = 'columns',
  COLUMN = 'column',
  KEYS = 'keys',
  KEY = 'key',
  INDEXES = 'indexes',
  INDEX = 'index',
}

export interface ITreeNode {
  key: string | number;
  name: string;
  isLeaf?: boolean;
  children?: ITreeNode[];
  columnType?: string;
  pinned?: boolean;
}
