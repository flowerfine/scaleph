import React, {useEffect, useState} from 'react';
import {Tree} from 'antd';
import SearchInput from './search';
import {Props} from "@/typings";
import {WsArtifactSeaTunnel} from "@/services/project/typings";
import {WsArtifactSeaTunnelService} from "@/services/project/WsArtifactSeaTunnelService";
import styles from './dnd.less';
import {DndNode} from "../node/dnd-node";

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

const Dnd: React.FC<Props<WsArtifactSeaTunnel>> = ({data}) => {
  const [treeItems, setTreeItems] = useState<ComponentTreeItem[]>([]);
  const [searchComponents, setSearchComponents] = useState<ComponentTreeItem[]>([]);

  useEffect(() => {
    WsArtifactSeaTunnelService.getDnds(data.seaTunnelEngine?.value).then((response) => {
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
      return <DndNode data={treeNode}/>
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
        <Tree
          rootClassName={styles.tree}
          blockNode
          showIcon={false}
          key={"key"}
          defaultExpandAll
          titleRender={(node) => treeNodeRender(node)}
          treeData={searchComponents.length ? searchComponents : treeItems}
        />
      )}
    </div>
  );
};

export {Dnd};
