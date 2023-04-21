import {DICT_TYPE, PRIVILEGE_CODE, WORKSPACE_CONF} from '@/constant';
import {DictDataService} from '@/services/admin/dictData.service';
import {WsDiJobService} from '@/services/project/WsDiJob.service';
import {WsDiJob} from '@/services/project/typings';
import {DeleteOutlined, DownOutlined, EditOutlined, NodeIndexOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, Col, Dropdown, Menu, message, Modal, Row, Space, Tooltip} from 'antd';
import React, {useRef, useState} from 'react';
import {history, useAccess, useIntl} from 'umi';
import DiJobForm from './components/DiJobForm';

const DiJobView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const [jobFormData, setJobFormData] = useState<{ visible: boolean; data: WsDiJob }>({
    visible: false,
    data: {},
  });

  const onJobFlow = (record: WsDiJob) => {
    history.push("/workspace/job/seatunnel/dag", {
      data: record,
      meta: {flowId: 'flow_' + record.jobCode, origin: record}
    })
  }

  const tableColumns: ProColumns<WsDiJob>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.di.jobName'}),
      dataIndex: 'jobName',
      width: 200,
    },
    {
      title: intl.formatMessage({id: 'pages.project.di.jobEngine'}),
      dataIndex: 'jobEngine',
      align: 'center',
      width: 100,
      render: (_, record) => {
        return record.jobEngine?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelEngineType)
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.di.jobType'}),
      dataIndex: 'jobType',
      align: 'center',
      width: 100,
      render: (_, record) => {
        return record.jobType?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.flinkRuntimeExecutionMode)
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 150,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.updateTime'}),
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
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
              <Tooltip title={intl.formatMessage({id: 'pages.project.di.define'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<NodeIndexOutlined/>}
                  onClick={() => onJobFlow(record)}
                />
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
                      content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        WsDiJobService.deleteJobRow(record).then((d) => {
                          if (d.success) {
                            message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
                            actionRef.current?.reload();
                          }
                        });
                      },
                    });
                  }}
                />
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
          <ProTable<WsDiJob>
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
              return WsDiJobService.listJobByProject({
                ...params,
                projectId: projectId + '',
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
                            key: 'streaming',
                            label: (
                              <Button
                                type="text"
                                onClick={() => {
                                  setJobFormData({
                                    visible: true,
                                    data: {projectId: projectId + '', jobType: {value: 'STREAMING'}},
                                  });
                                }}
                              >
                                {intl.formatMessage({id: 'pages.project.di.job.streaming'})}
                              </Button>
                            ),
                          },
                          {
                            key: 'batch',
                            label: (
                              <Button
                                type="text"
                                onClick={() => {
                                  setJobFormData({
                                    visible: true,
                                    data: {projectId: projectId + '', jobType: {value: 'BATCH'}},
                                  });
                                }}
                              >
                                {intl.formatMessage({id: 'pages.project.di.job.batch'})}
                              </Button>
                            ),
                          },
                          {
                            key: 'automatic',
                            label: (
                              <Button
                                type="text"
                                onClick={() => {
                                  setJobFormData({
                                    visible: true,
                                    data: {projectId: projectId + '', jobType: {value: 'AUTOMATIC'}},
                                  });
                                }}
                              >
                                {intl.formatMessage({id: 'pages.project.di.job.automatic'})}
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
                onJobFlow(data)
              }
              actionRef.current?.reload();
            }}
            data={jobFormData.data}
          ></DiJobForm>
        )}
      </Row>
    </>
  );
};

export default DiJobView;
