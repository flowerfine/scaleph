import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tag, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import {
    ActionType,
    PageContainer,
    ProColumns,
    ProFormInstance,
    ProFormSelect,
    ProTable
} from "@ant-design/pro-components";
import {history, useAccess, useIntl} from "@umijs/max";
import {WORKSPACE_CONF} from "@/constants/constant";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {DICT_TYPE} from "@/constants/dictType";
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import {DictDataService} from "@/services/admin/dictData.service";

const FlinkKubernetesDeploymentWeb: React.FC = () => {
    const intl = useIntl();
    const access = useAccess();
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();
    const [selectedRows, setSelectedRows] = useState<WsFlinkKubernetesDeployment[]>([]);
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

    const tableColumns: ProColumns<WsFlinkKubernetesDeployment>[] = [
        {
            title: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.name'}),
            dataIndex: 'name',
            width: '10%',
        },
        {
            title: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.flinkVersion'}),
            dataIndex: 'flinkVersion',
            hideInSearch: true,
            render: (dom, entity) => {
                return (<Tag>{entity.kubernetesOptions?.flinkVersion}</Tag>)
            },
            renderFormItem: (item, {defaultRender, ...rest}, form) => {
                return (
                    <ProFormSelect
                        showSearch={false}
                        allowClear={true}
                        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)}
                    />
                );
            }
        },
        {
            title: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.image'}),
            dataIndex: 'image',
            hideInSearch: true,
            render: (dom, entity) => {
                return entity.kubernetesOptions?.image
            },
        },
        {
            title: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.namespace'}),
            dataIndex: 'namespace',
            hideInSearch: true,
        },
        {
            title: intl.formatMessage({id: 'app.common.data.remark'}),
            dataIndex: 'remark',
            hideInSearch: true,
        },
        {
            title: intl.formatMessage({id: 'app.common.data.createTime'}),
            dataIndex: 'createTime',
            hideInSearch: true,
            width: '8%',
        },
        {
            title: intl.formatMessage({id: 'app.common.data.updateTime'}),
            dataIndex: 'updateTime',
            hideInSearch: true,
            width: '8%',
        },
        {
            title: intl.formatMessage({id: 'app.common.operate.label'}),
            dataIndex: 'actions',
            align: 'center',
            width: 120,
            fixed: 'right',
            valueType: 'option',
            render: (_, record) => (
                <Space>
                    {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
                        <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                            <Button
                                shape="default"
                                type="link"
                                icon={<EditOutlined/>}
                                onClick={() => {
                                    history.push("/workspace/engine/compute/flink/deployment/steps/update", record)
                                }}
                            />
                        </Tooltip>
                    )}
                    {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
                        <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                            <Button
                                shape="default"
                                type="link"
                                danger
                                icon={<DeleteOutlined/>}
                                onClick={() => {
                                    Modal.confirm({
                                        title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                                        content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                                        okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                                        okButtonProps: {danger: true},
                                        cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                                        onOk() {
                                            WsFlinkKubernetesDeploymentService.delete(record).then((d) => {
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
            ),
        },
    ];

    return (<PageContainer title={false}>
        <ProTable<WsFlinkKubernetesDeployment>
            search={{
                labelWidth: 'auto',
                span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
            }}
            rowKey="id"
            actionRef={actionRef}
            formRef={formRef}
            options={false}
            columns={tableColumns}
            request={(params, sorter, filter) =>
                WsFlinkKubernetesDeploymentService.list({...params, projectId: projectId})
            }
            toolbar={{
                actions: [
                    access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
                        <Button
                            key="new"
                            type="primary"
                            onClick={() => {
                                history.push("/workspace/engine/compute/flink/deployment/steps/new")
                            }}
                        >
                            {intl.formatMessage({id: 'app.common.operate.new.label'})}
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
                                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                                    okButtonProps: {danger: true},
                                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                                    onOk() {
                                        WsFlinkKubernetesDeploymentService.deleteBatch(selectedRows).then((d) => {
                                            if (d.success) {
                                                message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
                                                actionRef.current?.reload();
                                            }
                                        });
                                    },
                                });
                            }}
                        >
                            {intl.formatMessage({id: 'app.common.operate.delete.label'})}
                        </Button>
                    )
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
        />
    </PageContainer>);
}

export default FlinkKubernetesDeploymentWeb;
