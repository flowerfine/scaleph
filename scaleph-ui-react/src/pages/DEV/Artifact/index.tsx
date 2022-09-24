import { Dict } from '@/app.d';
import { DICT_TYPE, PRIVILEGE_CODE } from '@/constant';
import FlinkArtifactForm from '@/pages/DEV/Artifact/components/FlinkArtifactForm';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkArtifactService } from '@/services/dev/flinkArtifact.service';
import { FlinkArtifact } from '@/services/dev/typings';
import { DeleteOutlined, EditOutlined, UploadOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Select, Space, Tooltip } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { history, useAccess, useIntl } from 'umi';

const FlinkArtifactWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkArtifact[]>([]);
  const [flinkArtifactTypeList, setFlinkArtifactTypeList] = useState<Dict[]>([]);
  const [flinkArtifactFormData, setFlinkArtifactData] = useState<{
    visiable: boolean;
    data: FlinkArtifact;
  }>({ visiable: false, data: {} });

  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.flinkArtifactType).then((d) => {
      setFlinkArtifactTypeList(d);
    });
  }, []);

  const tableColumns: ProColumns<FlinkArtifact>[] = [
    {
      title: intl.formatMessage({ id: 'pages.dev.artifact.name' }),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.artifact.type' }),
      dataIndex: 'type',
      render: (text, record, index) => {
        return record.type?.label;
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
            {flinkArtifactTypeList.map((item) => {
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
      title: intl.formatMessage({ id: 'pages.dev.remark' }),
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.updateTime' }),
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
                    history.push('/workspace/dev/artifact/jar', { id: record.id });
                  }}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.download.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
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
        search={{
          labelWidth: 'auto',
          span: { xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4 },
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => FlinkArtifactService.list(params)}
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
            access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
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
                      FlinkArtifactService.deleteBatch(selectedRows).then((d) => {
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

export default FlinkArtifactWeb;
