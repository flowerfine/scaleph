import React, {useState} from "react";
import {Divider, TreeSelectProps} from "antd";
import {SwapRightOutlined} from "@ant-design/icons";
import {ProCard, ProFormDependency, ProFormGroup, ProFormTreeSelect} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {DsInfoService} from "@/services/datasource/info.service";
import ConnectorForm from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/ConnectorForm";

const DataIntegrationFlinkCDCStepConfigDataSource: React.FC = () => {
  const intl = useIntl();
  const [fromTreeData, setFromTreeData] = useState<any[]>([
    {id: -1, pId: 0, title: 'MySQL', value: -1, selectable: false, isLeaf: false}
  ]);
  const [toTreeData, setToTreeData] = useState<any[]>([
    {id: -1000, pId: 0, title: 'Doris', value: -1000, selectable: false, isLeaf: false},
    {id: -1001, pId: 0, title: 'StarRocks', value: -1001, selectable: false, isLeaf: false},
    {id: -1002, pId: 0, title: 'Kafka', value: -1002, selectable: false, isLeaf: false}
  ]);

  const loadFromDataSource: TreeSelectProps['loadData'] = ({id, title}) => {
    return DsInfoService.list({dsType: title}).then((response) => {
      if (response.data) {
        const childDs = response.data.map((item) => {
          return {id: item.id, pId: id, title: item.name, value: item.id, item: item, isLeaf: true};
        })
        setFromTreeData(fromTreeData.concat(childDs))
      }
    });
  }

  const loadToDataSource: TreeSelectProps['loadData'] = ({id, title}) => {
    return DsInfoService.list({dsType: title}).then((response) => {
      if (response.data) {
        const childDs = response.data?.map((item) => {
          return {id: item.id, pId: id, title: item.name, value: item.id, item: item, isLeaf: true};
        })
        setToTreeData(toTreeData.concat(childDs))
      }
    });
  }

  return (
    <ProCard
      title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource'})}
      bordered
    >
      <ProFormGroup>
        <ProFormTreeSelect
          name={"fromDsId"}
          label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource.from'})}
          colProps={{span: 11, offset: 1}}
          rules={[{required: true}]}
          fieldProps={{
            treeDataSimpleMode: true,
            treeData: fromTreeData,
            loadData: loadFromDataSource
          }}
        />
        <SwapRightOutlined/>
        <ProFormTreeSelect
          name={"toDsId"}
          label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource.to'})}
          colProps={{span: 11, offset: 0}}
          rules={[{required: true}]}
          fieldProps={{
            treeDataSimpleMode: true,
            treeData: toTreeData,
            loadData: loadToDataSource
          }}
        />

        <ProFormDependency name={["fromDsId"]}>
          {({fromDsId}) => {
            return <ProFormGroup colProps={{span: 11, offset: 1}}>
              <Divider
                plain>{intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource.fromDsConfig'})}</Divider>
              <ConnectorForm prefix={"fromDsConfig"} type={'source'} dsId={fromDsId}/>
            </ProFormGroup>
          }}
        </ProFormDependency>
        <Divider type={"vertical"}/>
        <ProFormDependency name={["toDsId"]}>
          {({toDsId}) => {
            return <ProFormGroup colProps={{span: 11}}>
              <Divider
                plain>{intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config.dataSource.toDsConfig'})}</Divider>
              <ConnectorForm prefix={"toDsConfig"} type={'sink'} dsId={toDsId}/>
            </ProFormGroup>
          }}
        </ProFormDependency>
      </ProFormGroup>
    </ProCard>);
}

export default DataIntegrationFlinkCDCStepConfigDataSource;
