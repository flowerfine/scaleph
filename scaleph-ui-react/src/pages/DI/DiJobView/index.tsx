import { Dict, TreeNode } from '@/app.d';
import { DICT_TYPE, PRIVILEGE_CODE, WORKSPACE_CONF } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { DirectoryService } from '@/services/project/directory.service';
import { JobService } from '@/services/project/job.service';
import { DiDirectory, DiDirectoryTreeNode, DiJob } from '@/services/project/typings';
import {
  DeleteOutlined,
  DownOutlined,
  EditOutlined,
  NodeIndexOutlined,
  PlayCircleOutlined,
  PlusOutlined,
  SettingOutlined,
  StopOutlined,
} from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import {
  Button,
  Card,
  Col,
  Dropdown,
  Input,
  Menu,
  message,
  Modal,
  Row,
  Select,
  Space,
  Tooltip,
  Tree,
  Typography,
} from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import DiJobFlow from '../DiJobFlow';
import CrontabSetting from './components/CrontabSetting';
import DiJobForm from './components/DiJobForm';
import DirectoryForm from './components/DirectoryForm';
import styles from './index.less';

const DiJobView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId) + '';
  const [dirList, setDirList] = useState<TreeNode[]>([]);
  const [searchValue, setSearchValue] = useState<string>();
  const [expandKeys, setExpandKeys] = useState<React.Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<DiJob[]>([]);
  const [jobTypeList, setJobTypeList] = useState<Dict[]>([]);
  const [runtimeStateList, setRuntimeStateList] = useState<Dict[]>([]);
  const [selectDir, setSelectDir] = useState<React.Key>('');
  const [dirFormData, setDirFormData] = useState<{
    visible: boolean;
    data: DiDirectory;
    isUpdate: boolean;
  }>({
    visible: false,
    data: {},
    isUpdate: false,
  });
  const [jobFlowData, setJobFlowData] = useState<{ visible: boolean; data: DiJob }>({
    visible: false,
    data: {},
  });
  const [jobFormData, setJobFormData] = useState<{ visible: boolean; data: DiJob }>({
    visible: false,
    data: {},
  });
  const [crontabFormData, setCrontabFormData] = useState<{ visible: boolean; data: DiJob }>({
    visible: false,
    data: {},
  });
  const tableColumns: ProColumns<DiJob>[] = [
    {
      title: intl.formatMessage({ id: 'pages.project.di.jobCode' }),
      dataIndex: 'jobCode',
      width: 200,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.jobName' }),
      dataIndex: 'jobName',
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.jobType' }),
      dataIndex: 'jobType',
      align: 'center',
      width: 100,
      render: (_, record) => {
        return record.jobType?.label;
      },
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
            {jobTypeList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.jobStatus' }),
      dataIndex: 'jobStatus',
      align: 'center',
      hideInSearch: true,
      width: 100,
      render: (_, record) => {
        return record.jobStatus?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.runtimeState' }),
      dataIndex: 'runtimeState',
      align: 'center',
      width: 100,
      render: (_, record) => {
        return record.runtimeState?.label;
      },
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
            {runtimeStateList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.jobCrontab' }),
      dataIndex: 'jobCrontab',
      align: 'center',
      hideInSearch: true,
      width: 120,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.directory' }),
      dataIndex: 'directory',
      hideInSearch: true,
      width: 200,
      render: (_, record) => {
        return record.directory?.fullPath;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.jobVersion' }),
      dataIndex: 'jobVersion',
      width: 80,
      hideInSearch: true,
      align: 'center',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.updateTime' }),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.di.remark' }),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 150,
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
            {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'pages.project.di.run' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<PlayCircleOutlined />}
                  onClick={() => {
                    alert('run');
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'pages.project.di.stop' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<StopOutlined />}
                  onClick={() => {
                    alert('stop');
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'pages.project.di.define' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<NodeIndexOutlined />}
                  onClick={() => {
                    setJobFlowData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
          </Space>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    setJobFormData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'pages.project.di.setting' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<SettingOutlined />}
                  onClick={() => {
                    setCrontabFormData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.delete.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined />}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                      okButtonProps: { danger: true },
                      cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                      onOk() {
                        JobService.deleteJobRow(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.delete.success' }),
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
          </Space>
        </>
      ),
    },
  ];
  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.jobType).then((d) => {
      setJobTypeList(d);
    });
    DictDataService.listDictDataByType(DICT_TYPE.runtimeState).then((d) => {
      setRuntimeStateList(d);
    });
    refreshDirList();
  }, []);

  const refreshDirList = () => {
    DirectoryService.listProjectDir(projectId).then((d) => {
      setDirList(buildTree(d));
    });
  };

  const buildTree = (data: DiDirectoryTreeNode[]): TreeNode[] => {
    let tree: TreeNode[] = [];
    data.forEach((dir) => {
      const node: TreeNode = {
        key: '',
        title: '',
        origin: {
          id: dir.id,
          directoryName: dir.directoryName,
          pid: dir.pid,
        },
      };
      if (dir.children) {
        node.key = dir.id;
        node.title = dir.directoryName;
        node.children = buildTree(dir.children);
        node.showOpIcon = false;
      } else {
        node.key = dir.id;
        node.title = dir.directoryName;
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

  const searchDirTree = (value: string) => {
    keys = [];
    setExpandKeys(buildExpandKeys(dirList, value));
    setSearchValue(value);
    setAutoExpandParent(true);
  };

  const onExpand = (newExpandedKeys: React.Key[]) => {
    setExpandKeys(newExpandedKeys);
    setAutoExpandParent(false);
  };

  return (
    <>
      <Row gutter={[12, 12]}>
        <Col span={5}>
          <Card
            className={styles.leftCard}
            title={
              <Typography.Title level={5}>
                {intl.formatMessage({ id: 'pages.project.dir' })}
              </Typography.Title>
            }
          >
            <Input.Search
              style={{ marginBottom: 8 }}
              allowClear={true}
              onSearch={searchDirTree}
              placeholder={intl.formatMessage({ id: 'app.common.operate.search.label' })}
            ></Input.Search>
            <Tree
              treeData={dirList}
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
                  setSelectDir(selectedKeys[0]);
                } else {
                  setSelectDir('');
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
                        setDirList([...dirList]);
                      }}
                      onMouseLeave={() => {
                        node.showOpIcon = false;
                        setDirList([...dirList]);
                      }}
                    >
                      <Typography.Text style={{ paddingRight: 12 }}>{node.title}</Typography.Text>
                      {node.showOpIcon && (
                        <Space size={2}>
                          {access.canAccess(PRIVILEGE_CODE.datadevDirAdd) && (
                            <Tooltip
                              title={intl.formatMessage({ id: 'app.common.operate.new.label' })}
                            >
                              <Button
                                shape="default"
                                type="text"
                                size="small"
                                icon={<PlusOutlined />}
                                onClick={() => {
                                  setDirFormData({
                                    visible: true,
                                    data: {
                                      pid: node.origin.id,
                                      projectId: projectId,
                                    },
                                    isUpdate: false,
                                  });
                                }}
                              ></Button>
                            </Tooltip>
                          )}
                          {access.canAccess(PRIVILEGE_CODE.datadevDirEdit) && (
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
                                  setDirFormData({
                                    visible: true,
                                    data: {
                                      id: node.origin.id,
                                      directoryName: node.origin.directoryName,
                                      pid: node.origin.pid == '0' ? undefined : node.origin.pid,
                                      projectId: projectId,
                                    },
                                    isUpdate: true,
                                  });
                                }}
                              ></Button>
                            </Tooltip>
                          )}
                          {access.canAccess(PRIVILEGE_CODE.datadevDirDelete) && (
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
                                      DirectoryService.deleteDir(node.origin).then((d) => {
                                        if (d.success) {
                                          message.success(
                                            intl.formatMessage({
                                              id: 'app.common.operate.delete.success',
                                            }),
                                          );
                                          refreshDirList();
                                        }
                                      });
                                    },
                                  });
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
          </Card>
        </Col>
        <Col span={19}>
          <ProTable<DiJob>
            headerTitle={intl.formatMessage({ id: 'pages.project.di.job' })}
            search={{
              labelWidth: 'auto',
              span: { xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4 },
            }}
            scroll={{ x: 1200, y: 480 }}
            rowKey="id"
            actionRef={actionRef}
            formRef={formRef}
            options={false}
            columns={tableColumns}
            request={(params, sorter, filter) => {
              return JobService.listJobByProject({
                ...params,
                projectId: projectId,
                directoryId: selectDir as string,
              });
            }}
            toolbar={{
              actions: [
                access.canAccess(PRIVILEGE_CODE.datadevJobAdd) && (
                  <Dropdown
                    arrow={true}
                    placement="bottom"
                    overlay={
                      <Menu
                        items={[
                          {
                            key: 'r',
                            label: (
                              <Button
                                type="text"
                                onClick={() => {
                                  setJobFormData({
                                    visible: true,
                                    data: { projectId: projectId, jobType: { value: 'r' } },
                                  });
                                }}
                              >
                                {intl.formatMessage({ id: 'pages.project.di.job.realtime' })}
                              </Button>
                            ),
                          },
                          {
                            key: 'b',
                            label: (
                              <Button
                                type="text"
                                onClick={() => {
                                  setJobFormData({
                                    visible: true,
                                    data: { projectId: projectId, jobType: { value: 'b' } },
                                  });
                                }}
                              >
                                {intl.formatMessage({ id: 'pages.project.di.job.batch' })}
                              </Button>
                            ),
                          },
                        ]}
                      ></Menu>
                    }
                  >
                    <Button key="new" type="primary">
                      <Space>
                        {intl.formatMessage({ id: 'app.common.operate.new.label' })}
                        <DownOutlined />
                      </Space>
                    </Button>
                  </Dropdown>
                ),
                access.canAccess(PRIVILEGE_CODE.datadevJobDelete) && (
                  <Button
                    key="del"
                    type="default"
                    disabled={selectedRows.length < 1}
                    onClick={() => {
                      Modal.confirm({
                        title: intl.formatMessage({
                          id: 'app.common.operate.delete.confirm.title',
                        }),
                        content: intl.formatMessage({
                          id: 'app.common.operate.delete.confirm.content',
                        }),
                        okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                        okButtonProps: { danger: true },
                        cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                        onOk() {
                          JobService.deleteJobBatch(selectedRows).then((d) => {
                            if (d.success) {
                              message.success(
                                intl.formatMessage({ id: 'app.common.operate.delete.success' }),
                              );
                              actionRef.current?.reload();
                            }
                          });
                        },
                      });
                    }}
                  >
                    {intl.formatMessage({ id: 'app.common.operate.delete.label' })}
                  </Button>
                ),
              ],
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

        {dirFormData.visible && (
          <DirectoryForm
            visible={dirFormData.visible}
            isUpdate={dirFormData.isUpdate}
            onCancel={() => {
              setDirFormData({ visible: false, data: {}, isUpdate: false });
            }}
            onVisibleChange={(visible) => {
              setDirFormData({ visible: false, data: {}, isUpdate: false });
              refreshDirList();
            }}
            data={dirFormData.data}
          ></DirectoryForm>
        )}
        {jobFormData.visible && (
          <DiJobForm
            visible={jobFormData.visible}
            treeData={dirList}
            onCancel={() => {
              setJobFormData({ visible: false, data: {} });
            }}
            onVisibleChange={(visible, data) => {
              setJobFormData({ visible: visible, data: {} });
              if (data?.id) {
                setJobFlowData({ visible: true, data: data });
              }
              actionRef.current?.reload();
            }}
            data={jobFormData.data}
          ></DiJobForm>
        )}
        {jobFlowData.visible && (
          <DiJobFlow
            visible={jobFlowData.visible}
            onCancel={() => {
              setJobFlowData({ visible: false, data: {} });
            }}
            onVisibleChange={(visible) => {
              setJobFlowData({ visible: false, data: {} });
              actionRef.current?.reload();
            }}
            data={jobFlowData.data}
            meta={{ flowId: 'flow_' + jobFlowData.data.jobCode, origin: jobFlowData.data }}
          ></DiJobFlow>
        )}
        {crontabFormData.visible && (
          <CrontabSetting
            visible={crontabFormData.visible}
            onCancel={() => {
              setCrontabFormData({ visible: false, data: {} });
            }}
            onVisibleChange={(visible) => {
              setCrontabFormData({ visible: false, data: {} });
              actionRef.current?.reload();
            }}
            data={crontabFormData.data}
          ></CrontabSetting>
        )}
      </Row>
    </>
  );
};

export default DiJobView;
