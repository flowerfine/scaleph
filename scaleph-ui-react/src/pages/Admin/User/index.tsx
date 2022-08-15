import {Dict, TreeNode} from '@/app.d';
import {DICT_TYPE} from '@/constant';
import {deleteDept, listAllDept} from '@/services/admin/dept.service';
import {listDictDataByType} from '@/services/admin/dictData.service';
import {deleteRole, listAllRole} from '@/services/admin/role.service';
import {SecDept, SecDeptTreeNode, SecRole, SecUser} from '@/services/admin/typings';
import {deleteUserBatch, deleteUserRow, listUserByPage, updateUser,} from '@/services/admin/user.service';
import {
  DeleteOutlined,
  EditOutlined,
  PlusOutlined,
  RedoOutlined,
  StopOutlined,
  UserSwitchOutlined,
} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
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
import React, {useEffect, useRef, useState} from 'react';
import {useIntl} from 'umi';
import DeptForm from './components/DeptForm';
import DeptGrant from './components/DeptGrant';
import RoleForm from './components/RoleForm';
import RoleGrant from './components/RoleGrant';
import UserForm from './components/UserForm';
import styles from "./index.less";

const User: React.FC = () => {
  const intl = useIntl();
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
  const [userFormData, setUserFormData] = useState<{ visiable: boolean; data: SecUser }>({
    visiable: false,
    data: {},
  });
  const [roleFormData, setRoleFormData] = useState<{ visiable: boolean; data: SecRole }>({
    visiable: false,
    data: {},
  });
  const [roleGrantData, setRoleGrantData] = useState<{ visiable: boolean; data: SecRole }>({
    visiable: false,
    data: {},
  });
  const [deptFormData, setDeptFormData] = useState<{
    visiable: boolean;
    data: SecDept;
    isUpdate: boolean;
  }>({
    visiable: false,
    data: {},
    isUpdate: false,
  });
  const [deptGrantData, setDeptGrantData] = useState<{ visiable: boolean; data: SecDept }>({
    visiable: false,
    data: {},
  });

  const tableColumns: ProColumns<SecUser>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.user.userName'}),
      dataIndex: 'userName',
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.nickName'}),
      dataIndex: 'nickName',
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.email'}),
      dataIndex: 'email',
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.realName'}),
      dataIndex: 'realName',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.mobilePhone'}),
      dataIndex: 'mobilePhone',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.gender'}),
      dataIndex: 'gender',
      hideInSearch: true,
      render: (text, record, index) => {
        return record.gender?.label;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.userStatus'}),
      dataIndex: 'userStatus',
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
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
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      dataIndex: 'actions',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
              <Button
                shape="default"
                type="link"
                icon={<EditOutlined/>}
                onClick={() => {
                  setUserFormData({visiable: true, data: record});
                  actionRef.current?.reload();
                }}
              ></Button>
            </Tooltip>
            {record.userStatus?.value?.substring(0, 1) != '9' && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.forbid.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<StopOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.forbid.confirm.title'}),
                      content: intl.formatMessage({
                        id: 'app.common.operate.forbid.confirm.content',
                      }),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        deleteUserRow(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({id: 'app.common.operate.forbid.success'}),
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
            {record.userStatus?.value?.substring(0, 1) == '9' && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.enable.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<RedoOutlined/>}
                  onClick={() => {
                    let user: SecUser = {
                      userName: record.userName,
                      id: record.id,
                      userStatus: {value: '10', label: ''},
                      email: record.email,
                    };
                    updateUser(user).then((resp) => {
                      if (resp.success) {
                        message.success(intl.formatMessage({id: 'app.common.operate.success'}));
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
    if (type == 'role') {
      return (
        <Tooltip
          title={
            intl.formatMessage({id: 'app.common.operate.new.label'}) +
            intl.formatMessage({id: 'pages.admin.user.role'})
          }
        >
          <Button
            shape="default"
            type="link"
            icon={<PlusOutlined/>}
            onClick={() => {
              setRoleFormData({visiable: true, data: {}});
            }}
          ></Button>
        </Tooltip>
      );
    } else if (type == 'dept') {
      return (
        <Tooltip
          title={
            intl.formatMessage({id: 'app.common.operate.new.label'}) +
            intl.formatMessage({id: 'pages.admin.user.dept'})
          }
        >
          <Button
            shape="default"
            type="link"
            icon={<PlusOutlined/>}
            onClick={() => {
              setDeptFormData({visiable: true, data: {}, isUpdate: false});
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
    listDictDataByType(DICT_TYPE.userStatus).then((d) => {
      setUserStatusList(d);
    });
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
            }}
          >
            <Tabs.TabPane tab={intl.formatMessage({id: 'pages.admin.user.role'})} key={roleTab}>
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
                  >
                    <Typography.Text style={{paddingRight: 12}}>{item.roleName}</Typography.Text>
                    {item.showOpIcon && (
                      <Space size={2}>
                        <Tooltip
                          title={intl.formatMessage({id: 'app.common.operate.edit.label'})}
                        >
                          <Button
                            shape="default"
                            type="text"
                            icon={<EditOutlined/>}
                            onClick={() => {
                              setRoleFormData({visiable: true, data: item});
                            }}
                          ></Button>
                        </Tooltip>
                        <Tooltip
                          title={intl.formatMessage({id: 'app.common.operate.delete.label'})}
                        >
                          <Button
                            shape="default"
                            type="text"
                            size="small"
                            icon={<DeleteOutlined/>}
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
                                okButtonProps: {danger: true},
                                cancelText: intl.formatMessage({
                                  id: 'app.common.operate.cancel.label',
                                }),
                                onOk() {
                                  deleteRole(item).then((d) => {
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
                        <Tooltip
                          title={intl.formatMessage({id: 'app.common.operate.grant.label'})}
                        >
                          <Button
                            shape="default"
                            type="text"
                            size="small"
                            icon={<UserSwitchOutlined/>}
                            onClick={() => {
                              setRoleGrantData({visiable: true, data: item});
                            }}
                          ></Button>
                        </Tooltip>
                      </Space>
                    )}
                  </List.Item>
                )}
              />
            </Tabs.TabPane>
            <Tabs.TabPane tab={intl.formatMessage({id: 'pages.admin.user.dept'})} key={deptTab}>
              <Input.Search
                style={{marginBottom: 8}}
                allowClear={true}
                onSearch={searchDeptTree}
                placeholder={intl.formatMessage({id: 'app.common.operate.search.label'})}
              ></Input.Search>
              <Tree
                treeData={deptTreeList}
                showLine={{showLeafIcon: false}}
                blockNode={true}
                showIcon={false}
                height={680}
                defaultExpandAll={true}
                expandedKeys={expandKeys}
                autoExpandParent={autoExpandParent}
                onExpand={onExpand}
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
                        <Typography.Text style={{paddingRight: 12}}>{node.title}</Typography.Text>
                        {node.showOpIcon && (
                          <Space size={2}>
                            <Tooltip
                              title={intl.formatMessage({id: 'app.common.operate.new.label'})}
                            >
                              <Button
                                shape="default"
                                type="text"
                                size="small"
                                icon={<PlusOutlined/>}
                                onClick={() => {
                                  setDeptFormData({
                                    visiable: true,
                                    data: {
                                      pid: node.origin.id,
                                    },
                                    isUpdate: false,
                                  });
                                }}
                              ></Button>
                            </Tooltip>
                            <Tooltip
                              title={intl.formatMessage({id: 'app.common.operate.edit.label'})}
                            >
                              <Button
                                shape="default"
                                type="text"
                                size="small"
                                icon={<EditOutlined/>}
                                onClick={() => {
                                  setDeptFormData({
                                    visiable: true,
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
                            <Tooltip
                              title={intl.formatMessage({id: 'app.common.operate.delete.label'})}
                            >
                              <Button
                                shape="default"
                                type="text"
                                size="small"
                                icon={<DeleteOutlined/>}
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
                                    okButtonProps: {danger: true},
                                    cancelText: intl.formatMessage({
                                      id: 'app.common.operate.cancel.label',
                                    }),
                                    onOk() {
                                      deleteDept(node.origin).then((d) => {
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
                            <Tooltip
                              title={intl.formatMessage({id: 'app.common.operate.grant.label'})}
                            >
                              <Button
                                shape="default"
                                type="text"
                                size="small"
                                icon={<UserSwitchOutlined/>}
                                onClick={() => {
                                  setDeptGrantData({visiable: true, data: node.origin});
                                }}
                              ></Button>
                            </Tooltip>
                          </Space>
                        )}
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
        <ProTable<SecUser>
          headerTitle={intl.formatMessage({id: 'pages.admin.user'})}
          search={{
            labelWidth: 'auto',
            span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
          }}
          sticky
          scroll={{x: 800}}
          rowKey="id"
          actionRef={actionRef}
          formRef={formRef}
          options={false}
          toolbar={{
            actions: [
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setUserFormData({visiable: true, data: {}});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>,
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({id: 'app.common.operate.forbid.confirm.title'}),
                    content: intl.formatMessage({
                      id: 'app.common.operate.forbid.confirm.content',
                    }),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      deleteUserBatch(selectedRows).then((d) => {
                        if (d.success) {
                          message.success(
                            intl.formatMessage({id: 'app.common.operate.forbid.success'}),
                          );
                          actionRef.current?.reload();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.forbid.label'})}
              </Button>,
            ],
          }}
          columns={tableColumns}
          request={(params, sorter, filter) => {
            return listUserByPage(params);
          }}
          pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
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
      {roleFormData.visiable ? (
        <RoleForm
          visible={roleFormData.visiable}
          onCancel={() => {
            setRoleFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setRoleFormData({visiable: visiable, data: {}});
            refreshRoles();
          }}
          data={roleFormData.data}
        />
      ) : null}
      {roleGrantData.visiable ? (
        <RoleGrant
          visible={roleGrantData.visiable}
          onCancel={() => {
            setRoleGrantData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setRoleGrantData({visiable: visiable, data: {}});
          }}
          data={roleGrantData.data}
        ></RoleGrant>
      ) : null}
      {deptFormData.visiable ? (
        <DeptForm
          visible={deptFormData.visiable}
          treeData={deptTreeList}
          isUpdate={deptFormData.isUpdate}
          onCancel={() => {
            setDeptFormData({visiable: false, data: {}, isUpdate: false});
          }}
          onVisibleChange={(visiable) => {
            setDeptFormData({visiable: visiable, data: {}, isUpdate: false});
            refreshDepts();
          }}
          data={deptFormData.data}
        />
      ) : null}
      {deptGrantData.visiable ? (
        <DeptGrant
          visible={deptGrantData.visiable}
          onCancel={() => {
            setDeptGrantData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setDeptGrantData({visiable: visiable, data: {}});
          }}
          data={deptGrantData.data}
        />
      ) : null}
      {userFormData.visiable ? (
        <UserForm
          visible={userFormData.visiable}
          onCancel={() => {
            setUserFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setUserFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={userFormData.data}
        ></UserForm>
      ) : null}
    </Row>
  );
};

export default User;
