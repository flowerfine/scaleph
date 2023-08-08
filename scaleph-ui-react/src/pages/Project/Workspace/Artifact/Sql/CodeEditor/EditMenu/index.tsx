import { approximateTreeNode, TreeNodeType } from '@/pages/Project/components/utils';
import LoadingContent from '@/pages/Project/components/Loading/LoadingContent';
import Tree from '@/pages/Project/components/Tree';
import { SearchOutlined } from '@ant-design/icons';
import { Input } from 'antd';
import classnames from 'classnames';
import { useEffect, useRef, useState } from 'react';
import styles from './index.less';

// 渲染 Table 模块的组件
const RenderTableBox: React.FC = () => {
  const [searching, setSearching] = useState<boolean>(false); // 是否正在搜索
  const inputRef = useRef<Input>(null);
  const [searchedTableList, setSearchedTableList] = useState<any[] | undefined>(); // 搜索到的表格列表
  const [tableLoading, setTableLoading] = useState<boolean>(false); // 表格加载状态

  const treeData = [
    {
      name: '表格加载状态表格加载状表格加载状态表格加载状态表格加载状态表格加载状态表格加载状态态表格加载状态表格加载状态表格加载状态',
      key: '0-0',
      treeNodeType: 'table',
      children: [
        {
          name: '0-0-0',
          key: '0-0-0',
          children: [
            { name: '0-0-0-0', key: '0-0-0-0' },
            { name: '0-0-0-1', key: '0-0-0-1' },
            { name: '0-0-0-2', key: '0-0-0-2' },
          ],
        },
        {
          name: '0-0-1',
          key: '0-0-1',
          children: [
            { name: '0-0-1-0', key: '0-0-1-0' },
            { name: '0-0-1-1', key: '0-0-1-1' },
            { name: '0-0-1-2', key: '0-0-1-2' },
          ],
        },
        {
          name: '0-0-2',
          key: '0-0-2',
        },
      ],
    },
    {
      name: '0-1',
      key: '0-1',
      treeNodeType: 'table',
      children: [
        { name: '0-1-0-0', key: '0-1-0-0' },
        { name: '0-1-0-1', key: '0-1-0-1' },
        { name: '0-1-0-2', key: '0-1-0-2' },
      ],
    },
    {
      name: '0-2',
      key: '0-2',
      treeNodeType: 'table',
    },
  ];

  useEffect(() => {
    if (searching) {
      inputRef.current?.focus({ cursor: 'start' }); // 将焦点设置到输入框
    }
  }, [searching]);

  // 打开搜索框
  function openSearch() {
    setSearching(true);
  }

  // 失去焦点时处理
  function onBlur() {
    if (!inputRef.current?.input.value) {
      setSearching(false); // 关闭搜索状态
      setSearchedTableList(undefined); // 清空搜索结果
    }
  }

  // 输入框内容变化时处理
  function onChange(value: string) {
    setSearchedTableList(approximateTreeNode(treeData, value)); // 根据输入内容搜索表格列表并更新状态
  }

  // 刷新表格列表
  function refreshTableList() {
    console.log('刷新');
  }

  return (
    <div className={styles.tableModule}>
      <div className={styles.leftModuleTitle}>
        {searching ? (
          <div className={styles.leftModuleTitleSearch}>
            <Input
              ref={inputRef}
              size="small"
              placeholder="请输入"
              prefix={<SearchOutlined />}
              onBlur={onBlur}
              onChange={(e) => onChange(e.target.value)}
              allowClear
            />
          </div>
        ) : (
          <div className={styles.leftModuleTitleText}>
            <div className={styles.modelName}>Table</div>
            <div className={styles.iconBox}>
              <div className={styles.refreshIcon} onClick={() => refreshTableList()}>
                <img src="https://s.xinc818.com/files/webcill0mnbh0t7hz95/刷新.svg" />
              </div>
              <div className={styles.searchIcon} onClick={() => openSearch()}>
                 <img src="https://s.xinc818.com/files/webcill23gnulb18o7z/搜索.svg" />
              </div>
            </div>
          </div>
        )}
      </div>
        {/* 渲染树形结构 */}
        <Tree className={styles.tree} initialData={searchedTableList || treeData}></Tree>
    </div>
  );
};

const WorkspaceLeft: React.FC<{ showLeft: boolean }> = ({ showLeft }) => {
  return (
    <div className={classnames(styles.boxContent)} style={{display: showLeft ? "block" : "none"}}>
      <RenderTableBox />
    </div>
  );
};

export default WorkspaceLeft;