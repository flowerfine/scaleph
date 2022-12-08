import { PRIVILEGE_CODE, WORKSPACE_CONF } from '@/constant';
import { FlinkArtifactService } from '@/services/project/flinkArtifact.service';
import { FlinkArtifact } from '@/services/project/typings';
import { DeleteOutlined, EditOutlined, UploadOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Space, Tooltip } from 'antd';
import { useRef, useState } from 'react';
import { history, useAccess, useIntl } from 'umi';
import FlinkArtifactForm from './components/FlinkArtifactForm';

const JobArtifactView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [flinkArtifactFormData, setFlinkArtifactData] = useState<{
    visiable: boolean;
    data: FlinkArtifact;
  }>({ visiable: false, data: {} });

  const tableColumns: ProColumns<FlinkArtifact>[] = [
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.name' }),
      dataIndex: 'name',
      width: 240,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.remark' }),
      dataIndex: 'remark',
      width: 240,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.updateTime' }),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
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
            {access.canAccess(PRIVILEGE_CODE.datadevJobShow) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.upload.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<UploadOutlined />}
                  onClick={() => {
                    history.push('/workspace/job/artifact/jar', record);
                  }}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    console.log(record);
                    setFlinkArtifactData({ visiable: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
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
                        FlinkArtifactService.deleteOne(record).then((d) => {
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

  return (
    <div>
      <ProTable<FlinkArtifact>
        headerTitle={intl.formatMessage({ id: 'menu.project.job.artifact' })}
        search={{
          labelWidth: 'auto',
          span: { xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4 },
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) =>
          FlinkArtifactService.list({ ...params, projectId: projectId + '' })
        }
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkArtifactData({ visiable: true, data: {} });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
              </Button>
            ),
          ],
        }}
        pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      ></ProTable>
      {flinkArtifactFormData.visiable && (
        <FlinkArtifactForm
          visible={flinkArtifactFormData.visiable}
          onCancel={() => {
            setFlinkArtifactData({ visiable: false, data: {} });
          }}
          onVisibleChange={(visiable) => {
            setFlinkArtifactData({ visiable: visiable, data: {} });
            actionRef.current?.reload();
          }}
          data={flinkArtifactFormData.data}
        />
      )}
    </div>
  );
};
export default JobArtifactView;
