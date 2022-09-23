import { Dict, TreeNode } from '@/app.d';
import { DICT_TYPE, PRIVILEGE_CODE } from '@/constant';
import { DeptService } from '@/services/admin/dept.service';
import { DictDataService } from '@/services/admin/dictData.service';
import { RoleService } from '@/services/admin/role.service';
import { SecDept, SecDeptTreeNode, SecRole, SecUser } from '@/services/admin/typings';
import { UserService } from '@/services/admin/user.service';
import {
  DeleteOutlined,
  EditOutlined,
  PlusOutlined,
  RedoOutlined,
  StopOutlined,
  UserSwitchOutlined,
} from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import {
  Button,
  Card,
  Col,
  Input,
  List,
  message,
  Modal,
  Row,
  Select,
  Space,
  Tabs,
  Tooltip,
  Tree,
  Typography,
} from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import DeptForm from './components/DeptForm';
import DeptGrant from './components/DeptGrant';
import RoleForm from './components/RoleForm';
import RoleGrant from './components/RoleGrant';
import UserForm from './components/UserForm';
import styles from './index.less';

const User: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const roleTab: string = 'role';
  const deptTab: string = 'dept';
  const [tabId, setTabId] = useState<string>(roleTab);
  const [roleList, setRoleList] = useState<SecRole[]>([]);
  const [deptTreeList, setDeptTreeList] = useState<TreeNode[]>([]);
  const [searchValue, setSearchValue] = useState<string>();
  const [expandKeys, setExpandKeys] = useState<React.Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<SecUser[]>([]);
  const [userStatusList, setUserStatusList] = useState<Dict[]>([]);
  const [selectDept, setSelectDept] = useState<React.Key>('');
  const [selectRole, setSelectRole] = useState<string>('');
  const [userFormData, setUserFormData] = useState<{ visible: boolean; data: SecUser }>({
    visible: false,
    data: {},
  });
  const [roleFormData, setRoleFormData] = useState<{ visible: boolean; data: SecRole }>({
    visible: false,
    data: {},
  });
  const [roleGrantData, setRoleGrantData] = useState<{ visible: boolean; data: SecRole }>({
    visible: false,
    data: {},
  });
  const [deptFormData, setDeptFormData] = useState<{
    visible: boolean;
    data: SecDept;
    isUpdate: boolean;
  }>({
    visible: false,
    data: {},
    isUpdate: false,
  });
  const [deptGrantData, setDeptGrantData] = useState<{ visible: boolean; data: SecDept }>({
    visible: false,
    data: {},
  });

  const tableColumns: ProColumns<SecUser>[] = [
    {
      title: intl.formatMessage({ id: 'pages.admin.user.userName' }),
      dataIndex: 'userName',
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.user.nickName' }),
      dataIndex: 'nickName',
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.user.email' }),
      dataIndex: 'email',
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.user.realName' }),
      dataIndex: 'realName',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.user.mobilePhone' }),
      dataIndex: 'mobilePhone',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.user.gender' }),
      dataIndex: 'gender',
      hideInSearch: true,
      render: (text, record, index) => {
        return record.gender?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.user.userStatus' }),
      dataIndex: 'userStatus',
      renderFormItem: (item, { defaultRender, ...rest }, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {userStatusList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        );
      },
      render: (text, record, index) => {
        return record.userStatus?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'app.common.operate.label' }),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.userEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    setUserFormData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}

            {record.userStatus?.value?.substring(0, 1) != '9' &&
              access.canAccess(PRIVILEGE_CODE.userDelete) && (
                <Tooltip title={intl.formatMessage({ id: 'app.common.operate.forbid.label' })}>
                  <Button
                    shape="default"
                    type="link"
                    icon={<StopOutlined />}
                    onClick={() => {
                      Modal.confirm({
                        title: intl.formatMessage({
                          id: 'app.common.operate.forbid.confirm.title',
                        }),
                        content: intl.formatMessage({
                          id: 'app.common.operate.forbid.confirm.content',
                        }),
                        okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                        okButtonProps: { danger: true },
                        cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                        onOk() {
                          UserService.deleteUserRow(record).then((d) => {
                            if (d.success) {
                              message.success(
                                intl.formatMessage({ id: 'app.common.operate.forbid.success' }),
                              );
                              actionRef.current?.reload();
                            }
                          });
                        },
                      });
                    }}
                  ></Button>
                </Tooltip>
              )}
            {record.userStatus?.value?.substring(0, 1) == '9' &&
              access.canAccess(PRIVILEGE_CODE.userDelete) && (
                <Tooltip title={intl.formatMessage({ id: 'app.common.operate.enable.label' })}>
                  <Button
                    shape="default"
                    type="link"
                    icon={<RedoOutlined />}
                    onClick={() => {
                      let user: SecUser = {
                        userName: record.userName,
                        id: record.id,
                        userStatus: { value: '10', label: '' },
                        email: record.email,
                      };
                      UserService.updateUser(user).then((resp) => {
                        if (resp.success) {
                          message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                          actionRef.current?.reload();
                        }
                      });
                    }}
                  ></Button>
                </Tooltip>
              )}
          </Space>
        </>
      ),
    },
  ];

  const tabBarButtonOperations = (type: string) => {
    const intl = useIntl();
    if (type == 'role' && access.canAccess(PRIVILEGE_CODE.roleAdd)) {
      return (
        <Tooltip
          title={
            intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.admin.user.role' })
          }
        >
          <Button
            shape="default"
            type="link"
            icon={<PlusOutlined />}
            onClick={() => {
              setRoleFormData({ visible: true, data: {} });
            }}
          ></Button>
        </Tooltip>
      );
    } else if (type == 'dept' && access.canAccess(PRIVILEGE_CODE.deptAdd)) {
      return (
        <Tooltip
          title={
            intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.admin.user.dept' })
          }
        >
          <Button
            shape="default"
            type="link"
            icon={<PlusOutlined />}
            onClick={() => {
              setDeptFormData({ visible: true, data: {}, isUpdate: false });
            }}
          ></Button>
        </Tooltip>
      );
    } else {
      return <></>;
    }
  };

  //init data
  useEffect(() => {
    refreshRoles();
    refreshDepts();
    DictDataService.listDictDataByType(DICT_TYPE.userStatus).then((d) => {
      setUserStatusList(d);
    });
  }, []);

  const refreshRoles = () => {
    RoleService.listAllRole().then((d) => {
      setRoleList(d);
    });
  };

  const refreshDepts = () => {
    DeptService.listAllDept().then((d) => {
      setDeptTreeList(buildTree(d));
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

  return (
    <Row gutter={[12, 12]}>
      <Col span={5}>
        <Card className={styles.leftCard}>
          <Tabs
            tabBarExtraContent={tabBarButtonOperations(tabId)}
            type="card"
            onChange={(activeKey) => {
              setTabId(activeKey);
              setSelectRole('');
              setSelectDept('');
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
                      className={styles.roleListItem}
                      onMouseEnter={() => {
                        item.showOpIcon = true;
                        setRoleList([...roleList]);
                      }}
                      onMouseLeave={() => {
                        item.showOpIcon = false;
                        setRoleList([...roleList]);
                      }}
                      onClick={() => {
                        setSelectRole(item.id + '');
                        actionRef.current?.reload();
                      }}
                    >
                      <Typography.Text style={{ paddingRight: 12 }}>
                        {item.roleName}
                      </Typography.Text>
                      {item.showOpIcon && (
                        <Space size={2}>
                          {access.canAccess(PRIVILEGE_CODE.roleEdit) && (
                            <Tooltip
                              title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}
                            >
                              <Button
                                shape="default"
                                type="text"
                                icon={<EditOutlined />}
                                onClick={() => {
                                  setRoleFormData({ visible: true, data: item });
                                }}
                              ></Button>
                            </Tooltip>
                          )}
                          {access.canAccess(PRIVILEGE_CODE.roleDelete) && (
                            <Tooltip
                              title={intl.formatMessage({ id: 'app.common.operate.delete.label' })}
                            >
                              <Button
                                shape="default"
                                type="text"
                                size="small"
                                icon={<DeleteOutlined />}
                                onClick={() => {
                                  Modal.confirm({
                                    title: intl.formatMessage({
                                      id: 'app.common.operate.delete.confirm.title',
                                    }),
                                    content: intl.formatMessage({
                                      id: 'app.common.operate.delete.confirm.content',
                                    }),
                                    okText: intl.formatMessage({
                                      id: 'app.common.operate.confirm.label',
                                    }),
                                    okButtonProps: { danger: true },
                                    cancelText: intl.formatMessage({
                                      id: 'app.common.operate.cancel.label',
                                    }),
                                    onOk() {
                                      RoleService.deleteRole(item).then((d) => {
                                        if (d.success) {
                                          message.success(
                                            intl.formatMessage({
                                              id: 'app.common.operate.delete.success',
                                            }),
                                          );
                                          refreshRoles();
                                        }
                                      });
                                    },
                                  });
                                }}
                              ></Button>
                            </Tooltip>
                          )}
                          {access.canAccess(PRIVILEGE_CODE.roleGrant) && (
                            <Tooltip
                              title={intl.formatMessage({ id: 'app.common.operate.grant.label' })}
                            >
                              <Button
                                shape="default"
                                type="text"
                                size="small"
                                icon={<UserSwitchOutlined />}
                                onClick={() => {
                                  setRoleGrantData({ visible: true, data: item });
                                }}
                              ></Button>
                            </Tooltip>
                          )}
                        </Space>
                      )}
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
                  onSelect={(selectedKeys, e: { selected: boolean }) => {
                    if (e.selected) {
                      setSelectDept(selectedKeys[0]);
                    } else {
                      setSelectDept('');
                    }
                    actionRef.current?.reload();
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
                        <Col
                          span={24}
                          onMouseEnter={() => {
                            node.showOpIcon = true;
                            setDeptTreeList([...deptTreeList]);
                          }}
                          onMouseLeave={() => {
                            node.showOpIcon = false;
                            setDeptTreeList([...deptTreeList]);
                          }}
                        >
                          <Typography.Text style={{ paddingRight: 12 }}>
                            {node.title}
                          </Typography.Text>
                          {node.showOpIcon && (
                            <Space size={2}>
                              {access.canAccess(PRIVILEGE_CODE.deptAdd) && (
                                <Tooltip
                                  title={intl.formatMessage({ id: 'app.common.operate.new.label' })}
                                >
                                  <Button
                                    shape="default"
                                    type="text"
                                    size="small"
                                    icon={<PlusOutlined />}
                                    onClick={() => {
                                      setDeptFormData({
                                        visible: true,
                                        data: {
                                          pid: node.origin.id,
                                        },
                                        isUpdate: false,
                                      });
                                    }}
                                  ></Button>
                                </Tooltip>
                              )}
                              {access.canAccess(PRIVILEGE_CODE.deptEdit) && (
                                <Tooltip
                                  title={intl.formatMessage({
                                    id: 'app.common.operate.edit.label',
                                  })}
                                >
                                  <Button
                                    shape="default"
                                    type="text"
                                    size="small"
                                    icon={<EditOutlined />}
                                    onClick={() => {
                                      setDeptFormData({
                                        visible: true,
                                        data: {
                                          id: node.origin.id,
                                          deptCode: node.origin.deptCode,
                                          deptName: node.origin.deptName,
                                          pid: node.origin.pid == '0' ? undefined : node.origin.pid,
                                        },
                                        isUpdate: true,
                                      });
                                    }}
                                  ></Button>
                                </Tooltip>
                              )}
                              {access.canAccess(PRIVILEGE_CODE.deptDelete) && (
                                <Tooltip
                                  title={intl.formatMessage({
                                    id: 'app.common.operate.delete.label',
                                  })}
                                >
                                  <Button
                                    shape="default"
                                    type="text"
                                    size="small"
                                    icon={<DeleteOutlined />}
                                    onClick={() => {
                                      Modal.confirm({
                                        title: intl.formatMessage({
                                          id: 'app.common.operate.delete.confirm.title',
                                        }),
                                        content: intl.formatMessage({
                                          id: 'app.common.operate.delete.confirm.content',
                                        }),
                                        okText: intl.formatMessage({
                                          id: 'app.common.operate.confirm.label',
                                        }),
                                        okButtonProps: { danger: true },
                                        cancelText: intl.formatMessage({
                                          id: 'app.common.operate.cancel.label',
                                        }),
                                        onOk() {
                                          DeptService.deleteDept(node.origin).then((d) => {
                                            if (d.success) {
                                              message.success(
                                                intl.formatMessage({
                                                  id: 'app.common.operate.delete.success',
                                                }),
                                              );
                                              refreshDepts();
                                            }
                                          });
                                        },
                                      });
                                    }}
                                  ></Button>
                                </Tooltip>
                              )}
                              {access.canAccess(PRIVILEGE_CODE.deptGrant) && (
                                <Tooltip
                                  title={intl.formatMessage({
                                    id: 'app.common.operate.grant.label',
                                  })}
                                >
                                  <Button
                                    shape="default"
                                    type="text"
                                    size="small"
                                    icon={<UserSwitchOutlined />}
                                    onClick={() => {
                                      setDeptGrantData({ visible: true, data: node.origin });
                                    }}
                                  ></Button>
                                </Tooltip>
                              )}
                            </Space>
                          )}
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
        <ProTable<SecUser>
          headerTitle={intl.formatMessage({ id: 'pages.admin.user' })}
          search={{
            labelWidth: 'auto',
            span: { xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4 },
          }}
          scroll={{ x: 800 }}
          rowKey="id"
          actionRef={actionRef}
          formRef={formRef}
          options={false}
          toolbar={{
            actions: [
              access.canAccess(PRIVILEGE_CODE.userAdd) && (
                <Button
                  key="new"
                  type="primary"
                  onClick={() => {
                    setUserFormData({ visible: true, data: {} });
                  }}
                >
                  {intl.formatMessage({ id: 'app.common.operate.new.label' })}
                </Button>
              ),
              access.canAccess(PRIVILEGE_CODE.userDelete) && (
                <Button
                  key="del"
                  type="default"
                  disabled={selectedRows.length < 1}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({ id: 'app.common.operate.forbid.confirm.title' }),
                      content: intl.formatMessage({
                        id: 'app.common.operate.forbid.confirm.content',
                      }),
                      okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                      okButtonProps: { danger: true },
                      cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                      onOk() {
                        UserService.deleteUserBatch(selectedRows).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.forbid.success' }),
                            );
                            actionRef.current?.reload();
                          }
                        });
                      },
                    });
                  }}
                >
                  {intl.formatMessage({ id: 'app.common.operate.forbid.label' })}
                </Button>
              ),
            ],
          }}
          columns={tableColumns}
          request={(params, sorter, filter) => {
            return UserService.listUserByPage({
              ...params,
              deptId: selectDept as string,
              roleId: selectRole,
            });
          }}
          pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
          rowSelection={{
            fixed: true,
            onChange(selectedRowKeys, selectedRows, info) {
              setSelectedRows(selectedRows);
            },
          }}
          tableAlertRender={false}
          tableAlertOptionRender={false}
        ></ProTable>
      </Col>
      {roleFormData.visible ? (
        <RoleForm
          visible={roleFormData.visible}
          onCancel={() => {
            setRoleFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setRoleFormData({ visible: visible, data: {} });
            refreshRoles();
          }}
          data={roleFormData.data}
        />
      ) : null}
      {roleGrantData.visible ? (
        <RoleGrant
          visible={roleGrantData.visible}
          onCancel={() => {
            setRoleGrantData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setRoleGrantData({ visible: visible, data: {} });
          }}
          data={roleGrantData.data}
        ></RoleGrant>
      ) : null}
      {deptFormData.visible ? (
        <DeptForm
          visible={deptFormData.visible}
          treeData={deptTreeList}
          isUpdate={deptFormData.isUpdate}
          onCancel={() => {
            setDeptFormData({ visible: false, data: {}, isUpdate: false });
          }}
          onVisibleChange={(visible) => {
            setDeptFormData({ visible: visible, data: {}, isUpdate: false });
            refreshDepts();
          }}
          data={deptFormData.data}
        />
      ) : null}
      {deptGrantData.visible ? (
        <DeptGrant
          visible={deptGrantData.visible}
          onCancel={() => {
            setDeptGrantData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setDeptGrantData({ visible: visible, data: {} });
          }}
          data={deptGrantData.data}
        />
      ) : null}
      {userFormData.visible ? (
        <UserForm
          visible={userFormData.visible}
          onCancel={() => {
            setUserFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setUserFormData({ visible: visible, data: {} });
            actionRef.current?.reload();
          }}
          data={userFormData.data}
        ></UserForm>
      ) : null}
    </Row>
  );
};

export default User;
