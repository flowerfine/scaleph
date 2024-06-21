import React from "react";
import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const DataIntegrationFlinkCDCStepConfigTransform: React.FC = () => {
    const intl = useIntl();

    return (
        <ProCard
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform'})}
            bordered
        >
            <ProFormList
                name={"transform"}
                copyIconProps={false}
                creatorButtonProps={{
                    creatorButtonText: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.list'}),
                    type: 'text'
                }}
            >
                <ProFormGroup>
                    <ProFormText
                        name={"sourceTable"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.sourceTable'})}
                        colProps={{span: 4, offset: 1}}
                        rules={[{required: true}]}
                    />
                    <ProFormText
                        name={"projection"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.projection'})}
                        colProps={{span: 4}}
                    />
                    <ProFormText
                        name={"filter"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.filter'})}
                        colProps={{span: 3}}
                    />
                    <ProFormText
                        name={"primaryKeys"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.primaryKeys'})}
                        colProps={{span: 3}}
                    />
                    <ProFormText
                        name={"partitionKeys"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.partitionKeys'})}
                        colProps={{span: 3}}
                    />
                    <ProFormText
                        name={"tableOptions"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.transform.tableOptions'})}
                        colProps={{span: 3}}
                    />
                    <ProFormText
                        name={"description"}
                        label={intl.formatMessage({id: 'app.common.data.remark'})}
                        colProps={{span: 3}}
                    />
                </ProFormGroup>
            </ProFormList>
        </ProCard>);
}

export default DataIntegrationFlinkCDCStepConfigTransform;
