import { TreeNode } from '@/app.d';
import { PRIVILEGE_CODE } from '@/constant';
import { DeptService } from '@/services/admin/dept.service';
import { PrivilegeService } from '@/services/admin/privilege.service';
import { RoleService } from '@/services/admin/role.service';
import { SecDeptTreeNode, SecPrivilegeTreeNode, SecRole } from '@/services/admin/typings';
import {
  Button,
  Card,
  Col,
  Empty,
  Input,
  List,
  Row,
  Space,
  Tabs,
  Tree,
  TreeProps,
  Typography,
} from 'antd';
import React, { useEffect, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import styles from './index.less';

const Privilege: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const roleTab: string = 'role';
  const deptTab: string = 'dept';
  const [tabId, setTabId] = useState<string>(roleTab);
  const [privilegeTabId, setPrivilegeTabId] = useState<string>('0');
  const [roleList, setRoleList] = useState<SecRole[]>([]);
  const [checkedItemId, setCheckedItemId] = useState<string>('');
  const [grantRoleList, setGrantRoleList] = useState<SecRole[]>([]);
  const [grantedRoleList, setGrantedRoleList] = useState<SecRole[]>([]);
  const [deptTreeList, setDeptTreeList] = useState<TreeNode[]>([]);
  const [searchValue, setSearchValue] = useState<string>();
  const [expandKeys, setExpandKeys] = useState<React.Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
  const [privilegeTreeList, setPrivilegeTreeList] = useState<TreeNode[]>([]);
  const [checkedPrivilegeList, setCheckedPrivilegeList] = useState<React.Key[]>([]);
  const [expandedPrivilegeKeys, setExpandedPrivilegeKeys] = useState<React.Key[]>([]);
  const [autoExpandPrivilegeParent, setAutoExpandPrivilegeParent] = useState<boolean>(true);
  //init data
  useEffect(() => {
    refreshRoles();
    refreshDepts();
  }, []);

  const refreshRoles = () => {
    RoleService.listAllRole().then((d) => {
      setRoleList(d);
    });
  };

  const refreshDepts = () => {
    DeptService.listAllDept().then((d) => {
      setDeptTreeList(buildDeptTree(d));
    });
  };

  const refreshPrivileges = (roleId: string, resourceType: string) => {
    setCheckedPrivilegeList([]);
    Promise.all([
      PrivilegeService.listAllPrivilege(resourceType),
      PrivilegeService.listPrivilegeByRole(roleId, resourceType),
    ]).then(([resp1, resp2]) => {
      setPrivilegeTreeList(buildPrivilegeTree(resp1));
      setCheckedPrivilegeList(
        resp2.map((value, index) => {
          return value.id as number;
        }),
      );
    });
  };

  const changePrivilegeTypeTab = (activeKey: string) => {
    setPrivilegeTabId(activeKey);
    if (checkedItemId) {
      refreshPrivileges(checkedItemId, activeKey);
    }
  };

  let keys: React.Key[] = [];
  const buildExpandKeys = (data: TreeNode[], value: string): React.Key[] => {
    data.forEach((dept) => {
      if (dept.children) {
        buildExpandKeys(dept.children, value);
      }
      if (dept.title?.toString().includes(value)) {
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
    RoleService.listGrantRoleByDept(deptId).then((d) => {
      setGrantRoleList(d);
    });
    RoleService.listRoleByDept(deptId).then((d) => {
      setGrantedRoleList(d);
    });
  };

  const buildDeptTree = (data: SecDeptTreeNode[]): TreeNode[] => {
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
        node.children = buildDeptTree(dept.children);
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

  const buildPrivilegeTree = (data: SecPrivilegeTreeNode[]): TreeNode[] => {
    let tree: TreeNode[] = [];
    data.forEach((privilege) => {
      const node: TreeNode = {
        key: '',
        title: '',
        origin: {
          id: privilege.privilegeId,
          privilegeCode: privilege.privilegeCode,
          privilegeName: privilege.privilegeName,
          pid: privilege.pid,
        },
      };
      if (privilege.children) {
        node.key = privilege.privilegeId;
        node.title = privilege.privilegeName;
        node.children = buildPrivilegeTree(privilege.children);
      } else {
        node.key = privilege.privilegeId;
        node.title = privilege.privilegeName;
      }
      tree.push(node);
    });
    return tree;
  };

  const onPrivielgeTreeCheck: TreeProps['onCheck'] = (checkedKeys, info) => {
    PrivilegeService.grantPrivilegeToRole(
      checkedItemId,
      checkedKeys?.checked as string[],
      privilegeTabId,
    ).then((d) => {
      if (d.success) {
        refreshPrivileges(checkedItemId, privilegeTabId);
      }
    });
  };

  const onPrivilegeTreeExpand = (expandedKeysValue: React.Key[]) => {
    setExpandedPrivilegeKeys(expandedKeysValue);
    setAutoExpandPrivilegeParent(false);
  };

  return (
    <Row gutter={[12, 12]}>
      <Col span={5}>
        <Card className={styles.leftCard}>
          <Tabs
            type="card"
            onChange={(activeKey) => {
              setTabId(activeKey);
              setCheckedItemId('');
            }}
          >
            {access.canAccess(PRIVILEGE_CODE.roleSelect) && (
              <Tabs.TabPane tab={intl.formatMessage({ id: 'pages.admin.user.role' })} key={roleTab}>
                <List
                  bordered={false}
                  dataSource={roleList}
                  itemLayout="vertical"
                  split={false}
                  renderItem={(item) => (
                    <List.Item
                      className={
                        checkedItemId == item.id + '' ? styles.selected : styles.roleListItem
                      }
                      onClick={() => {
                        setCheckedItemId(item.id + '');
                        refreshPrivileges(item.id + '', privilegeTabId + '');
                      }}
                    >
                      <Typography.Text style={{ paddingRight: 12 }}>
                        {item.roleName}
                      </Typography.Text>
                    </List.Item>
                  )}
                />
              </Tabs.TabPane>
            )}
            {access.canAccess(PRIVILEGE_CODE.deptSelect) && (
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
                  height={680}
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
                          <Typography.Text style={{ paddingRight: 12 }}>
                            {node.title}
                          </Typography.Text>
                        </Col>
                      </Row>
                    );
                  }}
                ></Tree>
              </Tabs.TabPane>
            )}
          </Tabs>
        </Card>
      </Col>
      <Col span={19}>
        {tabId == roleTab && (
          <Card className={styles.rightCard}>
            <Tabs defaultActiveKey="0" centered onChange={changePrivilegeTypeTab}>
              <Tabs.TabPane
                tab={intl.formatMessage({ id: 'pages.admin.user.privilege.menu' })}
                key="0"
              >
                <Tree
                  treeData={privilegeTreeList}
                  showLine={{ showLeafIcon: false }}
                  showIcon={false}
                  checkable={true}
                  checkStrictly={true}
                  height={680}
                  autoExpandParent={autoExpandPrivilegeParent}
                  onExpand={onPrivilegeTreeExpand}
                  expandedKeys={expandedPrivilegeKeys}
                  checkedKeys={checkedPrivilegeList}
                  onCheck={onPrivielgeTreeCheck}
                  titleRender={(node) => {
                    return <Typography.Text>{node.title}</Typography.Text>;
                  }}
                ></Tree>
              </Tabs.TabPane>
              <Tabs.TabPane
                tab={intl.formatMessage({ id: 'pages.admin.user.privilege.opt' })}
                key="1"
              >
                <Tree
                  treeData={privilegeTreeList}
                  showLine={{ showLeafIcon: false }}
                  showIcon={false}
                  checkable={true}
                  checkStrictly={true}
                  height={680}
                  autoExpandParent={autoExpandPrivilegeParent}
                  onExpand={onPrivilegeTreeExpand}
                  expandedKeys={expandedPrivilegeKeys}
                  checkedKeys={checkedPrivilegeList}
                  onCheck={onPrivielgeTreeCheck}
                  titleRender={(node) => {
                    return <Typography.Text>{node.title}</Typography.Text>;
                  }}
                ></Tree>
              </Tabs.TabPane>
              <Tabs.TabPane
                tab={intl.formatMessage({ id: 'pages.admin.user.privilege.data' })}
                key="2"
              >
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
                              RoleService.grantDeptRole(checkedItemId, item.id + '').then((d) => {
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
                              RoleService.revokeDeptRole(checkedItemId, item.id + '').then((d) => {
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
