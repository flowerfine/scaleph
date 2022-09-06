import {DICT_TYPE, PRIVILEGE_CODE} from '@/constant';
import {FlinkRelease} from '@/services/resource/typings';
import {DeleteOutlined, DownloadOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Select, Space, Tooltip} from 'antd';
import {useEffect, useRef, useState} from 'react';
import {useAccess, useIntl} from 'umi';
import FlinkReleaseForm from './components/FlinkReleaseForm';
import {deleteBatch, deleteOne, download, list} from "@/services/resource/flinkRelease.service";
import {Dict} from "@/app.d";
import {listDictDataByType} from "@/services/admin/dictData.service";

const FlinkReleaseResource: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkRelease[]>([]);
  const [flinkVersionList, setFlinkVersionList] = useState<Dict[]>([]);
  const [flinkReleaseFormData, setFlinkReleaseData] = useState<{
    visiable: boolean;
    data: FlinkRelease;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<FlinkRelease>[] = [
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      dataIndex: 'version',
      render: (text, record, index) => {
        return record.version?.label;
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
            {flinkVersionList.map((item) => {
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
      title: intl.formatMessage({id: 'pages.resource.fileName'}),
      dataIndex: 'fileName',
      width: 280,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.path'}),
      dataIndex: 'path',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.updateTime'}),
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
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.download.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined></DownloadOutlined>}
                  onClick={() => {
                    download(record)
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
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
                        deleteOne(record).then((d) => {
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

  useEffect(() => {
    listDictDataByType(DICT_TYPE.flinkVersion).then((d) => {
      setFlinkVersionList(d);
    });
  }, []);

  return (
    <div>
      <ProTable<FlinkRelease>
        search={{
          labelWidth: 'auto',
          span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return list(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkReleaseData({visiable: true, data: {}});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.upload.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
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
                      deleteBatch(selectedRows).then((d) => {
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
              >
                {intl.formatMessage({id: 'app.common.operate.delete.label'})}
              </Button>
            ),
          ],
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
      {flinkReleaseFormData.visiable && (
        <FlinkReleaseForm
          visible={flinkReleaseFormData.visiable}
          onCancel={() => {
            setFlinkReleaseData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setFlinkReleaseData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={flinkReleaseFormData.data}
        />
      )}
    </div>
  );
};

export default FlinkReleaseResource;
