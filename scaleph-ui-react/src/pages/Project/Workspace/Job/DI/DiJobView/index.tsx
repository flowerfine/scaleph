import {Dict} from '@/app.d';
import {DICT_TYPE, PRIVILEGE_CODE, WORKSPACE_CONF} from '@/constant';
import {DictDataService} from '@/services/admin/dictData.service';
import {JobService} from '@/services/project/job.service';
import {DiJob} from '@/services/project/typings';
import {DeleteOutlined, DownOutlined, EditOutlined, NodeIndexOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, Col, Dropdown, Menu, message, Modal, Row, Select, Space, Tooltip} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {useAccess, useIntl} from 'umi';
import DiJobFlow from '../DiJobFlow';
import DiJobForm from './components/DiJobForm';

const DiJobView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [jobTypeList, setJobTypeList] = useState<Dict[]>([]);

  const [jobFlowData, setJobFlowData] = useState<{ visible: boolean; data: DiJob }>({
    visible: false,
    data: {},
  });
  const [jobFormData, setJobFormData] = useState<{ visible: boolean; data: DiJob }>({
    visible: false,
    data: {},
  });

  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.jobType).then((d) => {
      setJobTypeList(d);
    });
  }, []);

  const tableColumns: ProColumns<DiJob>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.di.jobName'}),
      dataIndex: 'jobName',
      width: 200,
    },
    {
      title: intl.formatMessage({id: 'pages.project.di.jobType'}),
      dataIndex: 'jobType',
      align: 'center',
      width: 100,
      render: (_, record) => {
        return record.jobType?.label;
      },
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
      title: intl.formatMessage({id: 'pages.project.di.jobStatus'}),
      dataIndex: 'jobStatus',
      align: 'center',
      hideInSearch: true,
      width: 100,
      render: (_, record) => {
        return record.jobStatus?.label;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.di.jobVersion'}),
      dataIndex: 'jobVersion',
      width: 80,
      hideInSearch: true,
      align: 'center',
    },
    {
      title: intl.formatMessage({id: 'pages.project.di.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 150,
    },
    {
      title: intl.formatMessage({id: 'pages.project.di.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.project.di.updateTime'}),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => {
                    setJobFormData({visible: true, data: record});
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
              <Tooltip title={intl.formatMessage({id: 'pages.project.di.define'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<NodeIndexOutlined/>}
                  onClick={() => {
                    setJobFlowData({visible: true, data: record});
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        JobService.deleteJobRow(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({id: 'app.common.operate.delete.success'}),
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

  return (
    <>
      <Row gutter={[12, 12]}>
        <Col span={24}>
          <ProTable<DiJob>
            headerTitle={intl.formatMessage({id: 'pages.project.di.job'})}
            search={{
              labelWidth: 'auto',
              span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
            }}
            scroll={{x: 1200, y: 480}}
            rowKey="id"
            actionRef={actionRef}
            formRef={formRef}
            options={false}
            columns={tableColumns}
            request={(params, sorter, filter) => {
              return JobService.listJobByProject({
                ...params,
                projectId: projectId,
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
                                    data: {projectId: projectId, jobType: {value: 'r'}},
                                  });
                                }}
                              >
                                {intl.formatMessage({id: 'pages.project.di.job.realtime'})}
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
                                    data: {projectId: projectId, jobType: {value: 'b'}},
                                  });
                                }}
                              >
                                {intl.formatMessage({id: 'pages.project.di.job.batch'})}
                              </Button>
                            ),
                          },
                        ]}
                      ></Menu>
                    }
                  >
                    <Button key="new" type="primary">
                      <Space>
                        {intl.formatMessage({id: 'app.common.operate.new.label'})}
                        <DownOutlined/>
                      </Space>
                    </Button>
                  </Dropdown>
                ),
              ],
            }}
            pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
            tableAlertRender={false}
            tableAlertOptionRender={false}
          ></ProTable>
        </Col>
        {jobFormData.visible && (
          <DiJobForm
            visible={jobFormData.visible}
            onCancel={() => {
              setJobFormData({visible: false, data: {}});
            }}
            onVisibleChange={(visible, data) => {
              setJobFormData({visible: visible, data: {}});
              if (data?.id) {
                setJobFlowData({visible: true, data: data});
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
              setJobFlowData({visible: false, data: {}});
            }}
            onVisibleChange={(visible) => {
              setJobFlowData({visible: false, data: {}});
              actionRef.current?.reload();
            }}
            data={jobFlowData.data}
            meta={{flowId: 'flow_' + jobFlowData.data.jobCode, origin: jobFlowData.data}}
          ></DiJobFlow>
        )}
      </Row>
    </>
  );
};

export default DiJobView;
