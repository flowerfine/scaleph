import React, {useEffect, useState} from 'react';
import {Tree} from 'antd';
import SearchInput from './search';
import {Props} from "@/typings";
import {WsDiJob} from "@/services/project/typings";
import {WsSeaTunnelService} from "@/services/project/SeaTunnelService";
import styles from './dnd.less';
import {BaseNode} from "../node/base-node";

const {DirectoryTree} = Tree;

type ComponentTreeItem = {
  category: string;
  docString: string;
  isLeaf: boolean;
  key: string;
  title: string;
  children?: ComponentTreeItem[];
  ports?: {
    id: string;
    group: string;
  }[];
};

const Dnd: React.FC<Props<WsDiJob>> = ({data}) => {
  const [treeItems, setTreeItems] = useState<ComponentTreeItem[]>([]);
  const [searchComponents, setSearchComponents] = useState<ComponentTreeItem[]>([]);

  useEffect(() => {
    WsSeaTunnelService.getDnds(data.jobEngine?.value).then((response) => {
      if (response.success) {
        setTreeItems(response.data)
      }
    })
  }, []);

  const handleSearchComponent = (keyword?: string) => {
    if (!keyword) {
      setSearchComponents([]);
      return;
    }
    const searchResult = treeItems.flatMap((group) =>
      group.children.filter((child) =>
        child.title.toLowerCase().includes(keyword.toLowerCase()),
      ),
    );
    setSearchComponents(searchResult);
  };

  const treeNodeRender = (treeNode: ComponentTreeItem) => {
    const {isLeaf, docString, title} = treeNode;
    if (isLeaf) {
      return <BaseNode data={treeNode}/>
    } else {
      return <span className={styles.dir}>{title}</span>;
    }
  };

  return (
    <div className={styles.components}>
      <div className={styles.action}>
        <SearchInput
          className={styles.search}
          placeholder="请输入搜索关键字"
          onSearch={(key) => handleSearchComponent(key)}
        />
      </div>
      {treeItems.length && (
        <DirectoryTree
          rootClassName={styles.tree}
          blockNode
          showIcon={false}
          defaultExpandAll
          titleRender={(node) => treeNodeRender(node)}
          treeData={searchComponents.length ? searchComponents : treeItems}
        />
      )}
    </div>
  );
};

export {Dnd};
