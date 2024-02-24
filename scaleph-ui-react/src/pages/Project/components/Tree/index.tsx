import { useEffect, useState } from 'react';
import { Col, Popover, Row } from 'antd';
import classnames from 'classnames';
import { TreeNodeType } from '@/pages/Project/components/utils';
import styles from './index.less';

// 定义 Tree 组件的属性类型
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
            key={item?.name + index}
            show={true}
            level={0}
            data={item}
          />
        );
      })}
    </div>
  );
}

// 定义 TreeNode 组件的属性类型
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
  const indentArr = new Array(level).fill('indent'); // 缩进数组

  function loadData(data: any) {
    // 加载数据的逻辑
  }

  useEffect(() => {
    setShowChildren(showAllChildrenPenetrate); // 根据 showAllChildrenPenetrate 设置 showChildren
  }, [showAllChildrenPenetrate]);

  const handleClick = (data: any) => {
    if (!data.children) {
      loadData(data); // 加载数据
    } else {
      setShowChildren(!showChildren); // 切换子节点的显示状态
    }
  };

  const iconObject = {
    table: '/images/EditorResult/目录.svg',
    column: '/images/EditorResult/分类.svg',
    threeLevel: '/images/EditorResult/柜子.svg',
    icon: '/images/EditorResult/folderMinus.svg',
    unfoldIcon: '/images/EditorResult/folderOpen.svg',
  };

  const recognizeIcon = (treeNodeType: TreeNodeType) => {
    if (treeNodeType === TreeNodeType.DATA_SOURCE) {
      return '/images/EditorResult/刷新.svg';
    } else {
      return treeNodeType
        ? iconObject[treeNodeType]
        : iconObject?.[showChildren ? 'unfoldIcon' : 'icon'];
    }
  };

  const content = (data: any) => {
    return (
      <div style={{ width: 350 }}>
        {data?.tooltip?.map((res: any, index: number) => (
          <Row key={index}>
            <Col span={9} className={styles.livegoodsTitle}>
              {res?.title}
            </Col>
            <Popover content={res?.value}>
              <Col span={15} className={styles.livegoodsContent}>
                {res?.value}
              </Col>
            </Popover>
          </Row>
        ))}
      </div>
    );
  };

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
              <img
                className={classnames(styles.arrowsIcon, {
                  [styles.rotateArrowsIcon]: showChildren,
                })}
                src="/images/EditorResult/向下.svg"
              />
            </div>
          )}
          <div className={styles.dblclickArea}>
            <div className={styles.typeIcon}>
              <img src={recognizeIcon(data.treeNodeType)!} />
            </div>
            <div className={styles.contentText}>
              {!data?.isLeaf ? (
                <div className={styles.name} dangerouslySetInnerHTML={{ __html: data?.name }}></div>
              ) : (
                <Popover content={content(data)} placement="left">
                  <div
                    className={styles.name}
                    dangerouslySetInnerHTML={{ __html: data?.name }}
                  ></div>
                </Popover>
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
