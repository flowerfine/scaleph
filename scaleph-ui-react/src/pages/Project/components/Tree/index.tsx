import { TreeNodeType } from '@/pages/Project/components/utils';
import classnames from 'classnames';
import { useEffect, useState } from 'react';
import styles from './index.less';

interface TreeProps {
  className: string;
  initialData: any[]; // 初始数据
}

function Tree(props: TreeProps) {
  const { className, initialData } = props;
  const [treeData, setTreeData] = useState<any[]>([]); // 树形数据
  const [searchedTreeData, setSearchedTreeData] = useState<any[] | null>(null); // 搜索到的树形数据

  useEffect(() => {
    setTreeData(initialData); // 初始化树形数据
  }, [initialData]);

  return (
    <div className={classnames(className, styles.box)}>
      {/* 显示搜索到的数据或者原始树形数据 */}
      {(searchedTreeData || treeData)?.map((item, index) => {
        return (
          <TreeNode
            setTreeData={setTreeData}
            key={item.name + index}
            show={true}
            level={0}
            data={item}
          />
        );
      })}
    </div>
  );
}

interface TreeNodeProps {
  setTreeData: React.Dispatch<React.SetStateAction<any[]>>;
  data: any; // 节点数据
  level: number; // 节点级别
  show?: boolean; // 是否显示节点
  showAllChildrenPenetrate?: boolean; // 是否显示所有子节点
  dispatch?: any;
  workspaceModel?: any;
}

const TreeNode: React.FC<TreeNodeProps> = ({
  setTreeData,
  data,
  level,
  show = false,
  showAllChildrenPenetrate = false,
  dispatch,
  workspaceModel,
}) => {
  const [showChildren, setShowChildren] = useState<boolean>(false); // 是否展示子节点
  const [isLoading, setIsLoading] = useState<boolean>(false); // 是否正在加载子节点
  const indentArr = new Array(level).fill('indent'); // 缩进数组

  function loadData(data: any) {
    // 加载数据的逻辑
  }

  useEffect(() => {
    setShowChildren(showAllChildrenPenetrate); // 根据showAllChildrenPenetrate设置showChildren
  }, [showAllChildrenPenetrate]);

  const handleClick = (data: any) => {
    if (!showChildren && !data.children) {
      setIsLoading(true); // 设置isLoading为true，表示正在加载子节点
    }
    if (!data.children) {
      loadData(data); // 加载数据
    } else {
      setShowChildren(!showChildren); // 切换子节点的显示状态
    }
  };

  const iconObject = {
    table: 'https://s.xinc818.com/files/webcill22q7z1dee1wa/数据报告.svg',
    column: 'https://s.xinc818.com/files/webcill227c735hbwl0/目录.svg',
    icon: 'https://s.xinc818.com/files/webcill22cm8i9evk54/24gl-folderMinus (2).svg',
    unfoldIcon: 'https://s.xinc818.com/files/webcill22dacyr0kmrk/24gl-folderOpen.svg',
  };

  const recognizeIcon = (treeNodeType: TreeNodeType) => {
    if (treeNodeType === TreeNodeType.DATA_SOURCE) {
      return 'https://s.xinc818.com/files/webcill0mnbh0t7hz95/刷新.svg';
    } else {
      return treeNodeType
        ? iconObject[treeNodeType]
        : iconObject?.[showChildren ? 'unfoldIcon' : 'icon'];
    }
  };

  function nodeDoubleClick() {
    if (data.treeNodeType === TreeNodeType.TABLE) {
      dispatch({
        type: 'workspace/setDoubleClickTreeNodeData',
        payload: data,
      });
    } else {
      handleClick(data);
    }
  }

  return show ? (
    <>
      {/* 节点内容 */}
      <div className={classnames(styles.treeNode, { [styles.hiddenTreeNode]: !show })}>
        <div className={styles.left}>
          {/* 缩进 */}
          {indentArr.map((item, i) => {
            return <div key={i} className={styles.indent}></div>;
          })}
        </div>
        <div className={styles.right}>
          {!data.isLeaf && (
            <div onClick={() => handleClick(data)} className={styles.arrows}>
              {isLoading ? (
                <div className={styles.loadingIcon}>
                  <img src="https://s.xinc818.com/files/webcill20cuya3gbtdx/刷新.svg" />
                </div>
              ) : (
                <img
                  className={classnames(styles.arrowsIcon, {
                    [styles.rotateArrowsIcon]: showChildren,
                  })}
                  src="https://s.xinc818.com/files/webcill20nstumfa3vs/向下.svg"
                />
              )}
            </div>
          )}
          <div className={styles.dblclickArea} onDoubleClick={nodeDoubleClick}>
            <div className={styles.typeIcon}>
              <img src={recognizeIcon(data.treeNodeType)!} />
            </div>
            <div className={styles.contentText}>
              <div className={styles.name} dangerouslySetInnerHTML={{ __html: data.name }}></div>
              {data.treeNodeType === TreeNodeType.COLUMN && (
                <div className={styles.type}>{data.columnType}</div>
              )}
            </div>
          </div>
        </div>
      </div>
      {/* 子节点 */}
      {data.children?.map((item: any, i: number) => {
        return (
          <TreeNode
            key={i}
            data={item}
            level={level + 1}
            setTreeData={setTreeData}
            showAllChildrenPenetrate={showAllChildrenPenetrate}
            show={showChildren && show}
          ></TreeNode>
        );
      })}
    </>
  ) : (
    <></>
  );
};

export default Tree;
