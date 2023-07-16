import Split from "react-split";
import React, {useEffect, useState} from "react";
import {Editor} from "@monaco-editor/react";
import {Button, Col, Row, Space, Tag, Tree, TreeDataNode} from "antd";
import "./index.less";
import {useLocation} from "umi";
import {WsFlinkArtifactSql} from "@/services/project/typings";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";
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

const CodeEditor: React.FC = () => {
  const urlParams = useLocation();
  const [showLeft, setShowLeft] = useState<boolean>(true);
  const [sqlScript, setSqlScript] = useState<string>('');
  const [horizontalSplitSizes, setHorizontalSplitSizes] = useState<number[]>([
    20, 80,
  ]);
  const flinkArtifactSql = urlParams.state as WsFlinkArtifactSql;

  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const [sessionClusterId, setSessionClusterId] = useState<string>();
  const [sqlGatewaySessionHandleId, setSqlGatewaySessionHandleId] = useState<string>();
  const [treeNodes, setTreeNodes] = useState<CatalogNode[]>([])

  useEffect(() => {
    WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId)
      .then(resSessionClusterId => {
        setSessionClusterId(resSessionClusterId);
        WsFlinkSqlGatewayService.openSession(resSessionClusterId)
          .then(resSqlGatewaySessionHandleId => {
            setSqlGatewaySessionHandleId(resSqlGatewaySessionHandleId);
            WsFlinkSqlGatewayService.listCatalogs(resSessionClusterId, resSqlGatewaySessionHandleId)
              .then(catalogArray => {
                setTreeNodes(catalogArray.map(catalog => {
                  return {
                    title: catalog,
                    key: catalog,
                    name: catalog,
                    type: 'catalog',
                    isLeaf: false,
                    children: [],
                    parent: null
                  }
                }))
              })
          })
      });
  }, [])

  useEffect(() => {
    setSqlScript(flinkArtifactSql.script);
  }, [])

  const onSave = () => {
    FlinkArtifactSqlService.updateScript({id: flinkArtifactSql.id, script: sqlScript});
  }

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
    return new Promise<void>(async resolve => {
      let children: CatalogNode[] = [];
      switch (catalogNode.type) {
        case 'catalog':
          await WsFlinkSqlGatewayService.listDatabases(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.name)
            .then(databases => databases.forEach(database => children.push({
              name: database,
              title: database,
              key: `${catalogNode.key}-${database}`,
              type: 'database',
              children: [],
              parent: catalogNode,
              icon: <DatabaseOutlined/>
            })))
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
          await WsFlinkSqlGatewayService.listTables(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.parent?.parent?.name, catalogNode.name)
            .then(tables => tables.forEach(table => children.push(
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
            )));
          break;
        case 'view':
          await WsFlinkSqlGatewayService.listViews(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.parent?.parent?.name, catalogNode.name)
            .then(views => views.forEach(view => children.push(
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
            )));
          break;
        case 'function':
          await WsFlinkSqlGatewayService.listUserDefinedFunctions(sessionClusterId, sqlGatewaySessionHandleId, catalogNode.parent?.parent?.name, catalogNode.name)
            .then(userDefinedFunctions => userDefinedFunctions.forEach(udf => children.push(
              {
                title: <Space size="large" wrap>
                  <span>{udf.functionName}</span>
                  <Tag color={"red"} style={{fontSize: 'xx-small'}}>{udf.functionKind}</Tag>
                </Space>,
                key: `${catalogNode.key}-${udf.functionName}`,
                type: 'function-object',
                name: udf.functionName,
                children: [],
                parent: catalogNode,
                isLeaf: true,
                icon: <FunctionOutlined/>
              }
            )));
          break;
        default:
          break;
      }
      setTreeNodes(updateTreeData(treeNodes, catalogNode.key, children))
      resolve();
      return;
    });
  }

  return <>
    <Split
      className={"split-horizontal"}
      direction="horizontal"
      gutterSize={4}
      sizes={horizontalSplitSizes}
      minSize={[0, 0]}
      maxSize={[680, Infinity]}
      snapOffset={100}
      onDrag={(sizes: number[]) => {
        if (sizes[0] <= 6) {
          setShowLeft(false);
        } else {
          setShowLeft(true);
        }
        setHorizontalSplitSizes(sizes);
      }}
    >
      <div>
        <Row
          gutter={[12, 12]}
          className="container-left"
          style={{display: showLeft ? "block" : "none"}}>
          <Col span={24} style={{paddingLeft: 12, paddingRight: 12}}>
            {/* TODO: Add search in the future  */}
            {/*<Input.Search style={{marginBottom: 8}} onSearch={onSearchWordChange}/>*/}
            <Tree treeData={treeNodes}
                  loadData={onCatalogLoad}
                  showIcon={true}
                  showLine={true}/>
          </Col>
        </Row>
      </div>
      <div>
        <Row gutter={[12, 12]}>
          <Col span={24}>
            <Space style={{marginLeft: 12, marginBottom: 6}}>
              <Button onClick={onSave}>保存</Button>
            </Space>
          </Col>

        </Row>
        <Row>
          <Col span={24}>
            <Editor
              language="sql"
              value={sqlScript}
              height="calc(100vh - 175px)"
              width="100%"
              theme="vs"
              options={{minimap: {autohide: true, side: "right"}}}
              onChange={(value, event) => {
                setSqlScript(value)
              }}
            >
            </Editor>
          </Col>
        </Row>
      </div>
    </Split>
  </>;
};

export default CodeEditor;
