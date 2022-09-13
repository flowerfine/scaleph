import { PRIVILEGE_CODE } from '@/constant';
import {
  deleteDictDataBatch,
  deleteDictDataRow,
  listDictDataByPage,
} from '@/services/admin/dictData.service';
import {
  deleteDictTypeBatch,
  deleteDictTypeRow,
  listDictTypeByPage,
} from '@/services/admin/dictType.service';
import { SysDictData, SysDictType } from '@/services/admin/typings';
import { DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Button, Col, message, Modal, Row, Space, Tooltip } from 'antd';
import { useRef, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import DictDataForm from './components/DictDataForm';
import DictTypeForm from './components/DictTypeForm';

const Dict: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const dictTypeActionRef = useRef<ActionType>();
  const dictDataActionRef = useRef<ActionType>();
  const dictTypeFormRef = useRef<ProFormInstance>();
  const dictDataFormRef = useRef<ProFormInstance>();
  const [selectedDictType, setSelectedDictType] = useState<SysDictType[]>([]);
  const [selectedDictData, setSelectedDictData] = useState<SysDictData[]>([]);
  const [dictTypeFormData, setDictTypeFormData] = useState<{
    visible: boolean;
    data: SysDictType;
  }>({ visible: false, data: {} });
  const [dictDataFormData, setDictDataFormData] = useState<{
    visible: boolean;
    data: SysDictData;
  }>({ visible: false, data: {} });

  const dictTypeTableColumns: ProColumns<SysDictType>[] = [
    {
      title: intl.formatMessage({ id: 'pages.admin.dict.dictTypeCode' }),
      dataIndex: 'dictTypeCode',
      width: 180,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.dict.dictTypeName' }),
      dataIndex: 'dictTypeName',
      width: 180,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.dict.remark' }),
      dataIndex: 'remark',
      width: 260,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'app.common.operate.label' }),
      dataIndex: 'actions',
      width: 120,
      align: 'center',
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.dictTypeEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    setDictTypeFormData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.dictTypeDelete) && (
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
                        deleteDictTypeRow(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.delete.success' }),
                            );
                            dictTypeActionRef.current?.reload();
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

  const dictDataTableColumns: ProColumns<SysDictData>[] = [
    {
      title: intl.formatMessage({ id: 'pages.admin.dict.dictType' }),
      dataIndex: 'dictTypeCode',
      width: 240,
      fixed: 'left',
      render: (_, record) => {
        return <span>{record.dictType?.dictTypeCode + '-' + record.dictType?.dictTypeName}</span>;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.dict.dictCode' }),
      dataIndex: 'dictCode',
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.dict.dictValue' }),
      dataIndex: 'dictValue',
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.admin.dict.remark' }),
      dataIndex: 'remark',
      width: 260,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'app.common.operate.label' }),
      dataIndex: 'actions',
      width: 120,
      align: 'center',
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.dictDataEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    setDictDataFormData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.dictDataDelete) && (
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
                        deleteDictDataRow(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.delete.success' }),
                            );
                            dictDataActionRef.current?.reload();
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
      <Row gutter={[12, 12]}>
        <Col span={12}>
          <ProTable<SysDictType>
            headerTitle={intl.formatMessage({ id: 'pages.admin.dict.dictType' })}
            search={{ filterType: 'light' }}
            scroll={{ x: 800 }}
            rowKey="dictTypeCode"
            actionRef={dictTypeActionRef}
            formRef={dictTypeFormRef}
            options={false}
            toolbar={{
              actions: [
                access.canAccess(PRIVILEGE_CODE.dictTypeAdd) && (
                  <Button
                    key="new"
                    type="primary"
                    onClick={() => {
                      setDictTypeFormData({ visible: true, data: {} });
                    }}
                  >
                    {intl.formatMessage({ id: 'app.common.operate.new.label' })}
                  </Button>
                ),
                access.canAccess(PRIVILEGE_CODE.dictTypeDelete) && (
                  <Button
                    key="del"
                    type="default"
                    disabled={selectedDictType.length < 1}
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
                          deleteDictTypeBatch(selectedDictType).then((d) => {
                            if (d.success) {
                              message.success(
                                intl.formatMessage({ id: 'app.common.operate.delete.success' }),
                              );
                              dictTypeActionRef.current?.reload();
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
            columns={dictTypeTableColumns}
            request={(params, sorter, filter) => {
              dictDataFormRef.current?.setFieldsValue({
                dictTypeCode: params.dictTypeCode,
              });
              dictDataFormRef.current?.submit();
              return listDictTypeByPage(params);
            }}
            pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
            onRow={(record) => {
              return {
                onClick: (event) => {
                  dictDataFormRef.current?.setFieldsValue({
                    dictTypeCode: record.dictTypeCode,
                  });
                  dictDataFormRef.current?.submit();
                },
              };
            }}
            rowSelection={{
              fixed: true,
              onChange(selectedRowKeys, selectedRows, info) {
                setSelectedDictType(selectedRows);
              },
            }}
            tableAlertRender={false}
            tableAlertOptionRender={false}
          ></ProTable>
        </Col>
        <Col span={12}>
          <ProTable<SysDictData>
            headerTitle={intl.formatMessage({ id: 'pages.admin.dict.dictData' })}
            search={{ filterType: 'light' }}
            scroll={{ x: 800 }}
            rowKey="id"
            actionRef={dictDataActionRef}
            formRef={dictDataFormRef}
            options={false}
            toolbar={{
              actions: [
                access.canAccess(PRIVILEGE_CODE.dictDataAdd) && (
                  <Button
                    key="new"
                    type="primary"
                    onClick={() => {
                      setDictDataFormData({ visible: true, data: {} });
                    }}
                  >
                    {intl.formatMessage({ id: 'app.common.operate.new.label' })}
                  </Button>
                ),
                access.canAccess(PRIVILEGE_CODE.dictDataDelete) && (
                  <Button
                    key="del"
                    type="default"
                    disabled={selectedDictData.length < 1}
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
                          deleteDictDataBatch(selectedDictData).then((d) => {
                            if (d.success) {
                              message.success(
                                intl.formatMessage({ id: 'app.common.operate.delete.success' }),
                              );
                              dictDataActionRef.current?.reload();
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
            columns={dictDataTableColumns}
            request={(params, sorter, filter) => {
              return listDictDataByPage(params);
            }}
            pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
            rowSelection={{
              fixed: true,
              onChange(selectedRowKeys, selectedRows, info) {
                setSelectedDictData(selectedRows);
              },
            }}
            tableAlertRender={false}
            tableAlertOptionRender={false}
          ></ProTable>
        </Col>
      </Row>
      {dictTypeFormData.visible ? (
        <DictTypeForm
          visible={dictTypeFormData.visible}
          onCancel={() => {
            setDictTypeFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setDictTypeFormData({ visible: visible, data: {} });
            dictTypeActionRef.current?.reload();
          }}
          data={dictTypeFormData.data}
        />
      ) : null}
      {dictDataFormData.visible ? (
        <DictDataForm
          visible={dictDataFormData.visible}
          onCancel={() => {
            setDictDataFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setDictDataFormData({ visible: visible, data: {} });
            dictDataActionRef.current?.reload();
          }}
          data={dictDataFormData.data}
        />
      ) : null}
    </div>
  );
};

export default Dict;
