import React from "react";
import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const DataIntegrationFlinkCDCStepConfigRoute: React.FC = () => {
    const intl = useIntl();

    return (
        <ProCard
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.route'})}
            bordered
        >
            <ProFormList
                name={"route"}
                copyIconProps={false}
                creatorButtonProps={{
                    creatorButtonText: intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.route.list'}),
                    type: 'text'
                }}
            >
                <ProFormGroup>
                    <ProFormText
                        name={"sourceTable"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.route.source'})}
                        colProps={{span: 9, offset: 1}}
                        rules={[{required: true}]}
                    />
                    <ProFormText
                        name={"sinkTable"}
                        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.route.sink'})}
                        colProps={{span: 9, offset: 1}}
                        rules={[{required: true}]}
                    />
                    <ProFormText
                        name={"description"}
                        label={intl.formatMessage({id: 'app.common.data.remark'})}
                        colProps={{span: 3, offset: 1}}
                    />
                </ProFormGroup>
            </ProFormList>
        </ProCard>);
}

export default DataIntegrationFlinkCDCStepConfigRoute;
