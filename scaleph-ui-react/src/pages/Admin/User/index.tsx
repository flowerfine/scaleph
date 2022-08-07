import { Dict } from '@/app';
import { DICT_TYPE } from '@/constant';
import { listDictDataByType } from '@/services/admin/dictData.service';
import { deleteRole, listAllRole } from '@/services/admin/role.service';
import { SecDept, SecRole, SecUser } from '@/services/admin/typings';
import {
  deleteUserBatch,
  deleteUserRow,
  listUserByPage,
  updateUser,
} from '@/services/admin/user.service';
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
  List,
  message,
  Modal,
  Row,
  Select,
  Space,
  Tabs,
  Tooltip,
  Typography,
} from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useIntl } from 'umi';
import RoleForm from './components/RoleForm';
import UserForm from './components/UserForm';
import styles from './index.less';

const User: React.FC = () => {
  const intl = useIntl();
  const roleTab: string = 'role';
  const deptTab: string = 'dept';
  const [tabId, setTabId] = useState<string>(roleTab);
  const [roleList, setRoleList] = useState<SecRole[]>([]);
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
  const [deptFormData, setDeptFormData] = useState<{ visiable: boolean; data: SecDept }>({
    visiable: false,
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
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
              <Button
                shape="default"
                type="link"
                icon={<EditOutlined />}
                onClick={() => {
                  setUserFormData({ visiable: true, data: record });
                  actionRef.current?.reload();
                }}
              ></Button>
            </Tooltip>
            {record.userStatus?.value?.substring(0, 1) != '9' && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.forbid.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<StopOutlined />}
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
                        deleteUserRow(record).then((d) => {
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
            {record.userStatus?.value?.substring(0, 1) == '9' && (
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
                    updateUser(user).then((resp) => {
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
    if (type == 'role') {
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
              setRoleFormData({ visiable: true, data: {} });
            }}
          ></Button>
        </Tooltip>
      );
    } else if (type == 'dept') {
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
              console.log('创建部门');
            }}
          ></Button>
        </Tooltip>
      );
    } else {
      return <></>;
    }
  };

  const refreshRoles = () => {
    listAllRole().then((d) => {
      setRoleList(d);
    });
  };

  //init data
  useEffect(() => {
    refreshRoles();
    listDictDataByType(DICT_TYPE.userStatus).then((d) => {
      setUserStatusList(d);
    });
  }, []);

  return (
    <Row gutter={[12, 12]}>
      <Col span={6}>
        <Card>
          <Tabs
            tabBarExtraContent={tabBarButtonOperations(tabId)}
            type="card"
            onChange={(activeKey) => {
              setTabId(activeKey);
            }}
            className={styles.leftCard}
          >
            <Tabs.TabPane tab={intl.formatMessage({ id: 'pages.admin.user.role' })} key={roleTab}>
              <List
                size="small"
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
                    <Typography.Text style={{ paddingRight: 12 }}>{item.roleName}</Typography.Text>
                    {item.showOpIcon && (
                      <Space size={2}>
                        <Button
                          shape="default"
                          type="text"
                          size="small"
                          icon={<EditOutlined />}
                          onClick={() => {
                            setRoleFormData({ visiable: true, data: item });
                          }}
                        ></Button>
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
                        <Button
                          shape="default"
                          type="text"
                          size="small"
                          icon={<UserSwitchOutlined />}
                          onClick={() => {}}
                        ></Button>
                      </Space>
                    )}
                  </List.Item>
                )}
              />
            </Tabs.TabPane>
            <Tabs.TabPane tab={intl.formatMessage({ id: 'pages.admin.user.dept' })} key={deptTab}>
              部门树
            </Tabs.TabPane>
          </Tabs>
        </Card>
      </Col>
      <Col span={18}>
        <ProTable<SecUser>
          headerTitle={intl.formatMessage({ id: 'pages.admin.user' })}
          search={{ filterType: 'query' }}
          scroll={{ x: 800 }}
          size="small"
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
                  setUserFormData({ visiable: true, data: {} });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
              </Button>,
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
                      deleteUserBatch(selectedRows).then((d) => {
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
              </Button>,
            ],
          }}
          columns={tableColumns}
          request={(params, sorter, filter) => {
            return listUserByPage(params);
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
      {roleFormData.visiable ? (
        <RoleForm
          visible={roleFormData.visiable}
          onCancel={() => {
            setRoleFormData({ visiable: false, data: {} });
          }}
          onVisibleChange={(visiable) => {
            setRoleFormData({ visiable: visiable, data: {} });
            refreshRoles();
          }}
          data={roleFormData.data}
        />
      ) : null}
      {userFormData.visiable ? (
        <UserForm
          visible={userFormData.visiable}
          onCancel={() => {
            setUserFormData({ visiable: false, data: {} });
          }}
          onVisibleChange={(visiable) => {
            setUserFormData({ visiable: visiable, data: {} });
            actionRef.current?.reload();
          }}
          data={userFormData.data}
        ></UserForm>
      ) : null}
    </Row>
  );
};

export default User;
