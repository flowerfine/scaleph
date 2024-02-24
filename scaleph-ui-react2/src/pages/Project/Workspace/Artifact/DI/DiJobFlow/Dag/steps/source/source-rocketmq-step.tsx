import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup, ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {RocketMQParams, SchemaParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper";

const SourceRocketMQStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as WsDiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Drawer
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
      destroyOnClose={true}
      onClose={onCancel}
      extra={
        <Button
          type="primary"
          onClick={() => {
            form.validateFields().then((values) => {
              let map: Map<string, any> = new Map();
              map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
              map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
              map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
              values[RocketMQParams.aclEnabled] = values[RocketMQParams.aclEnabledField]
              values[RocketMQParams.startMode] = values[RocketMQParams.startModeField]
              StepSchemaService.formatSchema(values)
              StepSchemaService.formatRocketMQPartitionOffsets(values)
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              WsDiJobService.saveStepAttr(map).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.success'}));
                  onOK ? onOK(values) : null;
                }
              });
            });
          }}
        >
          {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
        </Button>
      }
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <ProFormText
          name={RocketMQParams.nameSrvAddr}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.nameSrvAddr'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={RocketMQParams.aclEnabledField}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.aclEnabled'})}
          initialValue={false}
        />
        <ProFormDependency name={[RocketMQParams.aclEnabledField]}>
          {({acl_enabled}) => {
            if (acl_enabled) {
              return <ProFormGroup>
                <ProFormText
                  name={RocketMQParams.accessKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.accessKey'})}
                  rules={[{required: true}]}
                  colProps={{span: 12}}
                />
                <ProFormText
                  name={RocketMQParams.secretKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.secretKey'})}
                  rules={[{required: true}]}
                  colProps={{span: 12}}
                />
              </ProFormGroup>;
            }

            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormText
          name={RocketMQParams.topics}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.topics'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.rocketmq.topics.placeholder'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={RocketMQParams.consumerGroup}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.consumerGroup'})}
          initialValue={'SeaTunnel-Consumer-Group'}
        />
        <ProFormDigit
          name={RocketMQParams.partitionDiscoveryIntervalMillis}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionDiscoveryIntervalMillis'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionDiscoveryIntervalMillis.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 8}}
          initialValue={-1}
          fieldProps={{
            step: 1000
          }}
        />
        <ProFormDigit
          name={RocketMQParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.batchSize'})}
          colProps={{span: 8}}
          initialValue={100}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={RocketMQParams.consumerPollTimeoutMillis}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.consumerPollTimeoutMillis'})}
          colProps={{span: 8}}
          initialValue={5000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />

        <ProFormSwitch
          name={RocketMQParams.commitOnCheckpoint}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.commitOnCheckpoint'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.rocketmq.commitOnCheckpoint.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={true}
        />

        <ProFormSelect
          name={RocketMQParams.format}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.format'})}
          options={["json", "text"]}
          initialValue={"json"}
        />
        <ProFormDependency name={[RocketMQParams.format]}>
          {({format}) => {
            if (format == 'text') {
              return <ProFormGroup>
                <ProFormText
                  name={RocketMQParams.fieldDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.fieldDelimiter'})}
                  initialValue={','}
                />
              </ProFormGroup>;
            }
            if (format == 'json') {
              return <FieldItem/>;
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormSelect
          name={RocketMQParams.startModeField}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.startMode'})}
          options={["CONSUME_FROM_LAST_OFFSET", "CONSUME_FROM_FIRST_OFFSET", "CONSUME_FROM_GROUP_OFFSETS", "CONSUME_FROM_TIMESTAMP", "CONSUME_FROM_SPECIFIC_OFFSETS"]}
          initialValue={"CONSUME_FROM_GROUP_OFFSETS"}
        />

        <ProFormDependency name={[RocketMQParams.startModeField]}>
          {({startModeField}) => {
            if (startModeField == 'CONSUME_FROM_TIMESTAMP') {
              return <ProFormGroup>
                <ProFormDigit
                  name={RocketMQParams.startModeTimestamp}
                  label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.startModeTimestamp'})}
                  rules={[{required: true}]}
                />
              </ProFormGroup>;
            }
            if (startModeField == 'CONSUME_FROM_SPECIFIC_OFFSETS') {
              return <ProFormGroup
                label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.startModeOffsets'})}
                tooltip={{
                  title: intl.formatMessage({id: 'pages.project.di.step.rocketmq.startModeOffsets.tooltip'}),
                  icon: <InfoCircleOutlined/>,
                }}
              >
                <ProFormList
                  name={RocketMQParams.startModeOffsetsList}
                  copyIconProps={false}
                  creatorButtonProps={{
                    creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.rocketmq.startModeOffsetsList'}),
                    type: 'text',
                  }}
                >
                  <ProFormGroup>
                    <ProFormText
                      name={RocketMQParams.specificPartition}
                      label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.specificPartition'})}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormDigit
                      name={RocketMQParams.specificPartitionOffset}
                      label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.specificPartitionOffset'})}
                      colProps={{span: 10, offset: 1}}
                    />
                  </ProFormGroup>
                </ProFormList>
              </ProFormGroup>;
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

      </ProForm>
    </Drawer>
  );
};

export default SourceRocketMQStepForm;
