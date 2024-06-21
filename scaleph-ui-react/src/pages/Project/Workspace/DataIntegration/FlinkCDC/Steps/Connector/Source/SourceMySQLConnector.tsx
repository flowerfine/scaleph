import React from 'react';
import {useIntl} from "@umijs/max";
import {ProFormGroup, ProFormSwitch, ProFormText} from "@ant-design/pro-components";

const SourceMySQLConnectorForm: React.FC = () => {
    const intl = useIntl();

    return (
        <ProFormGroup>
            <ProFormText
                name={"tables"}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.tables'})}
            />
            <ProFormText
                name={"tables.exclude"}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.tablesExclude'})}
            />
            <ProFormSwitch
                name={"schema-change.enabled"}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.schemaChangeEnabled'})}
                colProps={{span: 22, offset: 1}}
            />
        </ProFormGroup>
    );
};

export default SourceMySQLConnectorForm;
