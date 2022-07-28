import { useIntl } from 'umi';
import { Button, Col, message, Modal, Row, Space, Tooltip } from "antd";
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { SysDictData, SysDictType } from '@/services/admin/typings';
import { DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { deleteDictTypeBatch, deleteDictTypeRow, listDictTypeByPage } from '@/services/admin/dictType.service';
import { useRef, useState } from 'react';
import DictTypeForm from './components/DictTypeForm';
import { deleteDictDataBatch, deleteDictDataRow, listDictDataByPage } from '@/services/admin/dictData.service';
import DictDataForm from './components/DictDataForm';

const Dict: React.FC = () => {
    const intl = useIntl();
    const dictTypeActionRef = useRef<ActionType>();
    const dictDataActionRef = useRef<ActionType>();
    const dictTypeFormRef = useRef<ProFormInstance>();
    const dictDataFormRef = useRef<ProFormInstance>();
    const [selectedDictType, setSelectedDictType] = useState<SysDictType[]>([]);
    const [selectedDictData, setSelectedDictData] = useState<SysDictData[]>([]);
    const [dictTypeFormData, setDictTypeFormData] = useState<{ visiable: boolean, data: SysDictType }>({ visiable: false, data: {} });
    const [dictDataFormData, setDictDataFormData] = useState<{ visiable: boolean, data: SysDictData }>({ visiable: false, data: {} });

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
            hideInSearch: true
        },
        {
            title: intl.formatMessage({ id: 'pages.admin.dict.remark' }),
            dataIndex: 'remark',
            width: 260,
            hideInSearch: true
        },
        {
            title: intl.formatMessage({ id: 'app.common.operate.label' }),
            dataIndex: 'actions',
            width: 120,
            fixed: 'right',
            valueType: "option",
            render: (_, record) => (
                <>
                    <Space>
                        <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                            <Button
                                shape="default"
                                type="link"
                                icon={<EditOutlined />}
                                onClick={() => {
                                    setDictTypeFormData({ visiable: true, data: record });
                                    dictTypeActionRef.current?.reload();
                                }}>
                            </Button>
                        </Tooltip>
                        <Tooltip title={intl.formatMessage({ id: 'app.common.operate.delete.label' })}>
                            <Button
                                shape="default"
                                type="link"
                                icon={<DeleteOutlined />}
                                onClick={() => {
                                    Modal.confirm({
                                        title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                                        content: intl.formatMessage({ id: 'app.common.operate.delete.confirm.content' }),
                                        okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                                        okButtonProps: { danger: true },
                                        cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                                        onOk() {
                                            deleteDictTypeRow(record).then(d => {
                                                if (d.success) {
                                                    message.success(intl.formatMessage({ id: 'app.common.operate.delete.success' }));
                                                    dictTypeActionRef.current?.reload();
                                                }
                                            });
                                        },
                                    });
                                }}
                            >
                            </Button>
                        </Tooltip>
                    </Space>
                </>
            )
        }
    ];

    const dictDataTableColumns: ProColumns<SysDictData>[] = [
        {
            title: intl.formatMessage({ id: 'pages.admin.dict.dictType' }),
            dataIndex: 'dictTypeCode',
            width: 240,
            fixed: 'left',
            render: (_, record) => {
                return <span>{record.dictType?.dictTypeCode + '-' + record.dictType?.dictTypeName}</span>
            }
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
            hideInSearch: true
        },
        {
            title: intl.formatMessage({ id: 'app.common.operate.label' }),
            dataIndex: 'actions',
            width: 120,
            fixed: 'right',
            valueType: "option",
            render: (_, record) => (
                <>
                    <Space>
                        <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                            <Button
                                shape="default"
                                type="link"
                                icon={<EditOutlined />}
                                onClick={() => {
                                    setDictDataFormData({ visiable: true, data: record });
                                    dictDataActionRef.current?.reload();
                                }}>
                            </Button>
                        </Tooltip>
                        <Tooltip title={intl.formatMessage({ id: 'app.common.operate.delete.label' })}>
                            <Button
                                shape="default"
                                type="link"
                                icon={<DeleteOutlined />}
                                onClick={() => {
                                    Modal.confirm({
                                        title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                                        content: intl.formatMessage({ id: 'app.common.operate.delete.confirm.content' }),
                                        okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                                        okButtonProps: { danger: true },
                                        cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                                        onOk() {
                                            deleteDictDataRow(record).then(d => {
                                                if (d.success) {
                                                    message.success(intl.formatMessage({ id: 'app.common.operate.delete.success' }));
                                                    dictDataActionRef.current?.reload();
                                                }
                                            });
                                        },
                                    });
                                }}
                            >
                            </Button>
                        </Tooltip>
                    </Space>
                </>
            )
        }
    ];

    return (<div>
        <Row gutter={[12, 12]} >
            <Col span={12}>
                <ProTable<SysDictType>
                    headerTitle={intl.formatMessage({ id: 'pages.admin.dict.dictType' })}
                    search={{ filterType: "light" }}
                    scroll={{ x: 800 }}
                    size="small"
                    rowKey="dictTypeCode"
                    actionRef={dictTypeActionRef}
                    formRef={dictTypeFormRef}
                    options={false}
                    toolbar={{
                        actions: [
                            <Button key="new" type="primary" onClick={() => {
                                setDictTypeFormData({ visiable: true, data: {} });
                            }}>
                                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
                            </Button>,
                            <Button
                                key="del"
                                type="default"
                                disabled={selectedDictType.length < 1}
                                onClick={() => {
                                    Modal.confirm({
                                        title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                                        content: intl.formatMessage({ id: 'app.common.operate.delete.confirm.content' }),
                                        okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                                        okButtonProps: { danger: true },
                                        cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                                        onOk() {
                                            deleteDictTypeBatch(selectedDictType).then(d => {
                                                if (d.success) {
                                                    message.success(intl.formatMessage({ id: 'app.common.operate.delete.success' }));
                                                    dictTypeActionRef.current?.reload();
                                                }
                                            });
                                        },
                                    });
                                }}
                            >
                                {intl.formatMessage({ id: 'app.common.operate.delete.label' })}
                            </Button>,
                        ],
                    }}
                    columns={dictTypeTableColumns}
                    request={(params, sorter, filter) => {
                        dictDataFormRef.current?.setFieldsValue({
                            dictTypeCode: params.dictTypeCode
                        });
                        dictDataFormRef.current?.submit();
                        return listDictTypeByPage(params);
                    }}
                    pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
                    onRow={record => {
                        return {
                            onClick: event => {
                                dictDataFormRef.current?.setFieldsValue({
                                    dictTypeCode: record.dictTypeCode
                                });
                                dictDataFormRef.current?.submit();
                            }
                        }
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
                    search={{ filterType: "light" }}
                    scroll={{ x: 800 }}
                    size="small"
                    rowKey="id"
                    actionRef={dictDataActionRef}
                    formRef={dictDataFormRef}
                    options={false}
                    toolbar={{
                        actions: [
                            <Button key="new" type="primary" onClick={() => {
                                setDictDataFormData({ visiable: true, data: {} });
                            }}>
                                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
                            </Button>,
                            <Button
                                key="del"
                                type="default"
                                disabled={selectedDictData.length < 1}
                                onClick={() => {
                                    Modal.confirm({
                                        title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                                        content: intl.formatMessage({ id: 'app.common.operate.delete.confirm.content' }),
                                        okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                                        okButtonProps: { danger: true },
                                        cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                                        onOk() {
                                            deleteDictDataBatch(selectedDictData).then(d => {
                                                if (d.success) {
                                                    message.success(intl.formatMessage({ id: 'app.common.operate.delete.success' }));
                                                    dictDataActionRef.current?.reload();
                                                }
                                            });
                                        },
                                    });
                                }}
                            >
                                {intl.formatMessage({ id: 'app.common.operate.delete.label' })}
                            </Button>,
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
        {
            dictTypeFormData.visiable ? (
                <DictTypeForm
                    visible={dictTypeFormData.visiable}
                    onCancel={() => {
                        setDictTypeFormData({ visiable: false, data: {} })
                    }}
                    onVisibleChange={(visiable) => {
                        setDictTypeFormData({ visiable: visiable, data: {} });
                        dictTypeActionRef.current?.reload();
                    }}
                    data={dictTypeFormData.data} />
            ) : null
        }
        {
            dictDataFormData.visiable ? (
                <DictDataForm
                    visible={dictDataFormData.visiable}
                    onCancel={() => {
                        setDictDataFormData({ visiable: false, data: {} })
                    }}
                    onVisibleChange={(visiable) => {
                        setDictDataFormData({ visiable: visiable, data: {} });
                        dictDataActionRef.current?.reload();
                    }}
                    data={dictDataFormData.data} />
            ) : null
        }
    </div >
    );
}

export default Dict;
