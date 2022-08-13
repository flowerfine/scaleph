import { TreeNode } from '@/app.d';
import { listAllDept } from '@/services/admin/dept.service';
import {
  grantDeptRole,
  listAllRole,
  listGrantRoleByDept,
  listRoleByDept,
  revokeDeptRole,
} from '@/services/admin/role.service';
import { SecDeptTreeNode, SecRole } from '@/services/admin/typings';
import { Button, Card, Col, Empty, Input, List, Row, Space, Tabs, Tree, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';
import styles from './index.less';

const Privilege: React.FC = () => {
  const intl = useIntl();
  const roleTab: string = 'role';
  const deptTab: string = 'dept';
  const [tabId, setTabId] = useState<string>(roleTab);
  const [roleList, setRoleList] = useState<SecRole[]>([]);
  const [checkedItemId, setCheckedItemId] = useState<string>('');
  const [grantRoleList, setGrantRoleList] = useState<SecRole[]>([]);
  const [grantedRoleList, setGrantedRoleList] = useState<SecRole[]>([]);

  const [deptTreeList, setDeptTreeList] = useState<TreeNode[]>([]);
  const [searchValue, setSearchValue] = useState<string>();
  const [expandKeys, setExpandKeys] = useState<React.Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
  const [targetKeys, setTargetKeys] = useState<string[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
  // const [privilegeList, setPrivilegeList] = useState();
  // privilegeList: ITreeItem[];
  //init data
  useEffect(() => {
    refreshRoles();
    refreshDepts();
  }, []);

  const refreshRoles = () => {
    listAllRole().then((d) => {
      setRoleList(d);
    });
  };

  const refreshDepts = () => {
    listAllDept().then((d) => {
      setDeptTreeList(buildTree(d));
    });
  };

  let keys: React.Key[] = [];
  const buildExpandKeys = (data: TreeNode[], value: string): React.Key[] => {
    data.forEach((dept) => {
      if (dept.children) {
        buildExpandKeys(dept.children, value);
      }
      if (dept.title?.toString().includes(value)) {
        console.log(dept.title?.toString());
        keys.push(dept.key + '');
      }
    });
    return keys;
  };

  const searchDeptTree = (value: string) => {
    keys = [];
    setExpandKeys(buildExpandKeys(deptTreeList, value));
    setSearchValue(value);
    setAutoExpandParent(true);
  };

  const onExpand = (newExpandedKeys: React.Key[]) => {
    setExpandKeys(newExpandedKeys);
    setAutoExpandParent(false);
  };

  const refreshGrantRole = (deptId: string) => {
    listGrantRoleByDept(deptId).then((d) => {
      setGrantRoleList(d);
    });
    listRoleByDept(deptId).then((d) => {
      setGrantedRoleList(d);
    });
  };

  const buildTree = (data: SecDeptTreeNode[]): TreeNode[] => {
    let tree: TreeNode[] = [];
    data.forEach((dept) => {
      const node: TreeNode = {
        key: '',
        title: '',
        origin: {
          id: dept.deptId,
          deptCode: dept.deptCode,
          deptName: dept.deptName,
          pid: dept.pid,
        },
      };
      if (dept.children) {
        node.key = dept.deptId;
        node.title = dept.deptName;
        node.children = buildTree(dept.children);
        node.showOpIcon = false;
      } else {
        node.key = dept.deptId;
        node.title = dept.deptName;
        node.showOpIcon = false;
      }
      tree.push(node);
    });
    return tree;
  };

  return (
    <Row gutter={[12, 12]}>
      <Col span={5}>
        <Card className={styles.leftCard}>
          <Tabs
            type="card"
            onChange={(activeKey) => {
              setTabId(activeKey);
            }}
          >
            <Tabs.TabPane tab={intl.formatMessage({ id: 'pages.admin.user.role' })} key={roleTab}>
              <List
                bordered={false}
                dataSource={roleList}
                itemLayout="vertical"
                split={false}
                renderItem={(item) => (
                  <List.Item className={styles.roleListItem}>
                    <Typography.Text style={{ paddingRight: 12 }}>{item.roleName}</Typography.Text>
                  </List.Item>
                )}
              />
            </Tabs.TabPane>
            <Tabs.TabPane tab={intl.formatMessage({ id: 'pages.admin.user.dept' })} key={deptTab}>
              <Input.Search
                style={{ marginBottom: 8 }}
                allowClear={true}
                onSearch={searchDeptTree}
                placeholder={intl.formatMessage({ id: 'app.common.operate.search.label' })}
              ></Input.Search>
              <Tree
                treeData={deptTreeList}
                showLine={{ showLeafIcon: false }}
                blockNode={true}
                showIcon={false}
                defaultExpandAll={true}
                expandedKeys={expandKeys}
                autoExpandParent={autoExpandParent}
                onExpand={onExpand}
                onSelect={(selectedKeys, { selected, selectedNodes, node, event }) => {
                  if (selected) {
                    setCheckedItemId(node.key + '');
                    refreshGrantRole(node.key + '');
                  } else {
                    setGrantRoleList([]);
                    setGrantedRoleList([]);
                  }
                }}
                titleRender={(node) => {
                  return (
                    <Row
                      className={
                        node.title?.toString().includes(searchValue + '') && searchValue != ''
                          ? styles.siteTreeSearchValue
                          : ''
                      }
                    >
                      <Col span={24}>
                        <Typography.Text style={{ paddingRight: 12 }}>{node.title}</Typography.Text>
                      </Col>
                    </Row>
                  );
                }}
              ></Tree>
            </Tabs.TabPane>
          </Tabs>
        </Card>
      </Col>
      <Col span={19}>
        {tabId == roleTab && (
          <Card className={styles.rightCard}>
            <Tabs defaultActiveKey="menu" centered>
              <Tabs.TabPane
                tab={intl.formatMessage({ id: 'pages.admin.user.privilege.menu' })}
                key="menu"
              >
                {intl.formatMessage({ id: 'pages.admin.user.privilege.menu' })}
              </Tabs.TabPane>
              <Tabs.TabPane
                tab={intl.formatMessage({ id: 'pages.admin.user.privilege.opt' })}
                key="opt"
              >
                {intl.formatMessage({ id: 'pages.admin.user.privilege.opt' })}
              </Tabs.TabPane>
              <Tabs.TabPane
                tab={intl.formatMessage({ id: 'pages.admin.user.privilege.data' })}
                key="data"
              >
                {/* {intl.formatMessage({ id: 'pages.admin.user.privilege.data' })} */}
                {/* <Typography.Text style={{ paddingRight: 12 }} >{node.title}</Typography.Text> */}
                <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
              </Tabs.TabPane>
            </Tabs>
          </Card>
        )}
        {tabId == deptTab && (
          <Card className={styles.rightCard}>
            <Row gutter={[12, 0]}>
              <Col span={12}>
                <Tabs centered>
                  <Tabs.TabPane tab={intl.formatMessage({ id: 'pages.admin.user.awaitGrant' })}>
                    <Space>
                      {grantRoleList.map(function (item, i) {
                        return (
                          <Button
                            type="default"
                            size="large"
                            onClick={() => {
                              grantDeptRole(checkedItemId, item.id + '').then((d) => {
                                if (d.success) {
                                  refreshGrantRole(checkedItemId + '');
                                }
                              });
                            }}
                          >
                            {item.roleName}
                          </Button>
                        );
                      })}
                    </Space>
                  </Tabs.TabPane>
                </Tabs>
              </Col>
              <Col span={12}>
                <Tabs centered>
                  <Tabs.TabPane tab={intl.formatMessage({ id: 'pages.admin.user.granted' })}>
                    <Space>
                      {grantedRoleList.map(function (item, i) {
                        return (
                          <Button
                            type="primary"
                            size="large"
                            onClick={() => {
                              revokeDeptRole(checkedItemId, item.id + '').then((d) => {
                                if (d.success) {
                                  refreshGrantRole(checkedItemId + '');
                                }
                              });
                            }}
                          >
                            {item.roleName}
                          </Button>
                        );
                      })}
                    </Space>
                  </Tabs.TabPane>
                </Tabs>
              </Col>
            </Row>
          </Card>
        )}
      </Col>
    </Row>
  );
};

export default Privilege;
