import Tree from '@/pages/Project/components/Tree';
import { approximateTreeNode } from '@/pages/Project/components/utils';
import { WsFlinkKubernetesSessionClusterService } from '@/services/project/WsFlinkKubernetesSessionClusterService';
import { WsFlinkSqlGatewayService } from '@/services/project/WsFlinkSqlGatewayService';
import { SearchOutlined } from '@ant-design/icons';
import { Input, Spin } from 'antd';
import classnames from 'classnames';
import { useEffect, useRef, useState } from 'react';
import styles from './index.less';

// 定义树形结构节点数据类型
interface TreeNodeData {
  name: string;
  key: string;
  treeNodeType: string;
  children?: TreeNodeData[];
  isLeaf?: boolean;
  tooltip?: { title: string; value: string }[];
}

// 渲染 Table 模块的组件
const RenderTableBox: React.FC = () => {
  const [searching, setSearching] = useState<boolean>(false); // 是否正在搜索
  const inputRef = useRef<Input>(null);
  const [searchedTableList, setSearchedTableList] = useState<TreeNodeData[] | undefined>(); // 搜索到的表格列表
  const [treeData, setTreeData] = useState<TreeNodeData[]>([]);
  const [loading, setLoading] = useState(false); //加载

  // 将原始数据转换为树形结构数据
  function convertToTreeData(data: any[]): TreeNodeData[] {
    return data?.map((item) => ({
      name: item?.catalogName,
      key: item?.catalogName,
      treeNodeType: '',
      children: item?.databases.map((database: any) => ({
        name: database?.databaseName,
        key: database?.databaseName,
        treeNodeType: 'column',
        children: [
          {
            name: 'tables',
            key: 'tables',
            treeNodeType: 'threeLevel',
            children: database?.tables.map((table: any) => ({
              name: table?.tableName,
              key: table?.tableName,
              treeNodeType: 'table',
              isLeaf: true,
              tooltip: Object.entries(table.properties).map(([title, value]) => ({ title, value })),
            })),
          },
          {
            name: 'views',
            key: 'views',
            treeNodeType: 'threeLevel',
            children: database.views.map((table: any) => ({
              name: table.tableName,
              key: table.tableName,
              treeNodeType: 'table',
              isLeaf: true,
              tooltip: Object.entries(table.properties).map(([title, value]) => ({ title, value })),
            })),
          },
        ],
      })),
    }));
  }

  // 获取列表数据
  const getListData = async () => {
    setLoading(true);
    const projectId = localStorage.getItem('projectId'); // 从本地存储中获取项目ID
    const resSessionClusterId =
      await WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId); // 获取会话集群ID
    const catalogArray = await WsFlinkSqlGatewayService.leftMenuList(resSessionClusterId); // 获取菜单列表返回的原始数据
    setLoading(false);
    if (Array.isArray(catalogArray)) {
      const treeData = convertToTreeData(catalogArray); // 将原始数据转换为树形结构
      setTreeData(treeData); // 设置树形结构数据
    }
  };

  useEffect(() => {
    if (searching) {
      inputRef.current?.focus({ cursor: 'start' }); // 如果处于搜索状态，则设置输入框焦点
    }
    getListData(); // 获取列表数据
  }, []);

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
    getListData();
  }

  return (
    <Spin spinning={loading} delay={500}>
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
                  <img src="/images/EditorResult/刷新.svg" />
                </div>
                <div className={styles.searchIcon} onClick={() => openSearch()}>
                  <img src="/images/EditorResult/搜索.svg" />
                </div>
              </div>
            </div>
          )}
        </div>
        {/* 渲染树形结构 */}
        <Tree className={styles.tree} initialData={searchedTableList || treeData} />
      </div>
    </Spin>
  );
};

const WorkspaceLeft: React.FC<{ showLeft: boolean }> = ({ showLeft }) => {
  return (
    <div className={classnames(styles.boxContent)} style={{ display: showLeft ? 'block' : 'none' }}>
      <RenderTableBox />
    </div>
  );
};

export default WorkspaceLeft;
