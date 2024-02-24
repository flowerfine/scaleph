import lodash from 'lodash';
// 模糊匹配树并且高亮
export function approximateTreeNode(treeData: any, target: string) {
  if (target) {
    const newTree: any[] = [];
    treeData.forEach((item: any) => {
      const newItem = { ...item };
      if (newItem.children?.length) {
        newItem.children = approximateTreeNode(newItem.children, target);
        if(newItem.children.length > 0) {
          newTree.push(newItem);
        }
      } else if (newItem.name?.toUpperCase()?.indexOf(target?.toUpperCase()) !== -1) {
        newTree.push({
          ...newItem,
          name: newItem.name?.replace(target, `<span style='color:red;'>${target}</span>`)
        });
      }
    });
    return newTree;
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
