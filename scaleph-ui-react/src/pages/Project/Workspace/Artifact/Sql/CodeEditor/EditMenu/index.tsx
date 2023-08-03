import React, {useEffect, useState} from "react";
import {Col, Row, Space, Tag, Tree, TreeDataNode} from "antd";
import "./index.less";
import {useLocation} from "umi";
import {WORKSPACE_CONF} from "@/constant";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {WsFlinkSqlGatewayService} from "@/services/project/WsFlinkSqlGatewayService";
import {
  BorderlessTableOutlined,
  DatabaseOutlined,
  FolderOutlined,
  FunctionOutlined,
  TableOutlined
} from "@ant-design/icons";

interface CatalogNode extends TreeDataNode {
  name: string,
  type?: string;
  children: CatalogNode[],
  parent?: CatalogNode | null
}

const EditMenu: React.FC = ({showLeft}) => {
  const urlParams = useLocation();
  const [sqlScript, setSqlScript] = useState<string>('');

  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const [sessionClusterId, setSessionClusterId] = useState<string>();
  const [sqlGatewaySessionHandleId, setSqlGatewaySessionHandleId] = useState<string>();
  const [treeNodes, setTreeNodes] = useState<CatalogNode[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resSessionClusterId = await WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId);
        setSessionClusterId(resSessionClusterId);
  
        const resSqlGatewaySessionHandleId = await WsFlinkSqlGatewayService.openSession(resSessionClusterId);
        setSqlGatewaySessionHandleId(resSqlGatewaySessionHandleId);
  
        const catalogArray = await WsFlinkSqlGatewayService.listCatalogs(resSessionClusterId, resSqlGatewaySessionHandleId);
        setTreeNodes(catalogArray.map(catalog => ({
          title: catalog,
          key: catalog,
          name: catalog,
          type: 'catalog',
          isLeaf: false,
          children: [],
          parent: null
        })));
      } catch (error) {
        // 处理错误
      }
    }
  
    fetchData();
  }, []);



  const updateTreeData = (list: CatalogNode[], key: string | number, children: CatalogNode[]): CatalogNode[] =>
    list.map((node) => {
      if (node.key === key) {
        return {
          ...node,
          children,
        };
      }
      if (node.children) {
        return {
          ...node,
          children: updateTreeData(node.children, key, children),
        };
      }
      return node;
    });

    const onCatalogLoad = async (catalogNode: CatalogNode) => {
      let children: CatalogNode[] = [];
      switch (catalogNode.type) {
        case 'catalog':
          const databases = await WsFlinkSqlGatewayService.listDatabases(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.name);
          databases.forEach(database => children.push({
            name: database,
            title: database,
            key: `${catalogNode.key}-${database}`,
            type: 'database',
            children: [],
            parent: catalogNode,
            icon: <DatabaseOutlined/>
          }));
          break;
        case 'database':
          children.push(
            {
              title: 'TABLE',
              key: `${catalogNode.key}-TABLE`,
              type: 'table',
              name: catalogNode.name,
              children: [],
              parent: catalogNode,
              icon: <FolderOutlined/>
            },
            {
              title: 'VIEW',
              key: `${catalogNode.key}-VIEW`,
              type: 'view',
              name: catalogNode.name,
              children: [],
              parent: catalogNode,
              icon: <FolderOutlined/>
            },
            {
              title: 'FUNCTION',
              key: `${catalogNode.key}-FUNCTION`,
              type: 'function',
              name: catalogNode.name,
              children: [],
              parent: catalogNode,
              icon: <FolderOutlined/>
            }
          );
          break;
        case 'table':
          const tables = await WsFlinkSqlGatewayService.listTables(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.parent?.parent?.name, catalogNode.name);
          tables.forEach(table => children.push(
            {
              title: table,
              key: `${catalogNode.key}-${table}`,
              type: 'table-object',
              name: table,
              children: [],
              parent: catalogNode,
              isLeaf: true,
              icon: <TableOutlined/>
            }
          ));
          break;
        case 'view':
          const views = await WsFlinkSqlGatewayService.listViews(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.parent?.parent?.name, catalogNode.name);
          views.forEach(view => children.push(
            {
              title: view,
              key: `${catalogNode.key}-${view}`,
              type: 'view-object',
              name: view,
              children: [],
              parent: catalogNode,
              isLeaf: true,
              icon: <BorderlessTableOutlined/>
            }
          ));
          break;
        case 'function':
          const userDefinedFunctions = await WsFlinkSqlGatewayService.listUserDefinedFunctions(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.parent?.parent?.name, catalogNode.name);
          userDefinedFunctions.forEach(udf => children.push(
            {
              title: (
                <Space size="large" wrap>
                  <span>{udf.functionName}</span>
                  <Tag color={"red"} style={{fontSize: 'xx-small'}}>{udf.functionKind}</Tag>
                </Space>
              ),
              key: `${catalogNode.key}-${udf.functionName}`,
              type: 'function-object',
              name: udf.functionName,
              children: [],
              parent: catalogNode,
              isLeaf: true,
              icon: <FunctionOutlined/>
            }
          ));
          break;
        default:
          break;
      }
      setTreeNodes(updateTreeData(treeNodes, catalogNode.key, children));
    };

  return <>
        <Row
          gutter={[12, 12]}
          className="container-left"
          style={{display: showLeft ? "block" : "none"}}
          >
          <Col span={24} style={{paddingLeft: 12, paddingRight: 12}}>
            <Tree treeData={treeNodes}
                  loadData={onCatalogLoad}
                  showIcon={true}
                  showLine={true}/>
          </Col>
        </Row>
  </>;
};

export default EditMenu;
