import React from "react";
import {ProCard, ProFormGroup, ProFormSelect} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {SwapRightOutlined} from "@ant-design/icons";
import {Divider} from "antd";

const DataIntegrationFlinkCDCStepConfigDataSource: React.FC = () => {
  const intl = useIntl();

  return(
    <ProCard
      title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource'})}
      bordered
    >
      <ProFormGroup>
        <ProFormSelect
          name={"fromDsId"}
          label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource.from'})}
          colProps={{span: 11, offset: 1}}
        />
        <SwapRightOutlined />
        <ProFormSelect
          name={"fromDsId"}
          label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource.to'})}
          colProps={{span: 11, offset: 0}}
        />
        <Divider/>
      </ProFormGroup>
    </ProCard>);
}

export default DataIntegrationFlinkCDCStepConfigDataSource;
