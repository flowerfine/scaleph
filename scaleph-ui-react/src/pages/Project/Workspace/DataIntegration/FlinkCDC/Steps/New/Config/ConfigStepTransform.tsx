import React, {useRef} from "react";
import {Button, Space, Tooltip} from "antd";
import {EditOutlined} from "@ant-design/icons";
import {
    ActionType,
    EditableFormInstance,
    EditableProTable,
    ProCard,
    ProColumns,
    ProFormInstance
} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

type TransformConfig = {
    id: React.Key;
    sourceTable?: string;
    projection?: string;
    filter?: string;
    primaryKeys?: string;
    partitionKeys?: string;
    tableOptions?: string;
    description?: string;
};

const DataIntegrationFlinkCDCStepConfigTransform: React.FC = () => {
    const intl = useIntl();
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();
    const editorFormRef = useRef<EditableFormInstance<TransformConfig>>();

    const columns: ProColumns<TransformConfig>[] = [
        {
            title: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.sourceTable'}),
            dataIndex: 'sourceTable',
            formItemProps: {rules: [{required: true}]}
        },
        {
            title: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.projection'}),
            dataIndex: 'projection',
        },
        {
            title: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.filter'}),
            dataIndex: 'filter',
        },
        {
            title: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.primaryKeys'}),
            dataIndex: 'primaryKeys',
        },
        {
            title: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.partitionKeys'}),
            dataIndex: 'partitionKeys',
        },
        {
            title: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.tableOptions'}),
            dataIndex: 'tableOptions',
        },
        {
            title: intl.formatMessage({id: 'app.common.data.remark'}),
            dataIndex: 'description',
        },
        {
            title: intl.formatMessage({id: 'app.common.operate.label'}),
            dataIndex: 'actions',
            align: 'center',
            width: 120,
            fixed: 'right',
            valueType: 'option',
            render: (text, record, _, action) => [
                <>
                    <Space>
                        <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                            <Button
                                shape="default"
                                type="link"
                                icon={<EditOutlined/>}
                                onClick={() => {
                                    action?.startEditable?.(record.id);
                                }}
                            />
                        </Tooltip>
                    </Space>
                </>
            ]
        },
    ]

    return (
        <ProCard
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform'})}
            bordered
        >
            <EditableProTable<TransformConfig>
                rowKey={"id"}
                actionRef={actionRef}
                formRef={formRef}
                editableFormRef={editorFormRef}
                columns={columns}
                recordCreatorProps={{
                    newRecordType: 'dataSource',
                    record: () => ({
                        id: Date.now(),
                    }),
                }}
                bordered={true}
                controlled={true}
                editable={{
                    type: 'multiple',
                }}
            />
        </ProCard>);
}

export default DataIntegrationFlinkCDCStepConfigTransform;
