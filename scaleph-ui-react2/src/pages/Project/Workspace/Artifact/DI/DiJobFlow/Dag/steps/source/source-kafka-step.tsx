import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {KafkaParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";

const SourceKafkaStepForm: React.FC<ModalFormProps<{
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
              StepSchemaService.formatSchema(values)
              StepSchemaService.formatKafkaConf(values)
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
        <DataSourceItem dataSource={"Kafka"}/>
        <ProFormText
          name={KafkaParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.topic'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.kafka.topic.placeholder'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={'pattern'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.pattern'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.pattern.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={false}
        />
        <ProFormDependency name={['pattern']}>
          {({pattern}) => {
            if (pattern) {
              return (
                <ProFormDigit
                  name={KafkaParams.partitionDiscoveryIntervalMillis}
                  label={intl.formatMessage({id: 'pages.project.di.step.kafka.partitionDiscoveryIntervalMillis'})}
                  tooltip={{
                    title: intl.formatMessage({id: 'pages.project.di.step.kafka.partitionDiscoveryIntervalMillis.tooltip'}),
                    icon: <InfoCircleOutlined/>,
                  }}
                  initialValue={-1}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormText
          name={KafkaParams.consumerGroup}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.consumerGroup'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.consumerGroup.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={"SeaTunnel-Consumer-Group"}
        />
        <ProFormSwitch
          name={KafkaParams.commit_on_checkpoint}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.commit_on_checkpoint'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.commit_on_checkpoint.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={true}
        />

        <ProFormSelect
          name={KafkaParams.formatErrorHandleWay}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.formatErrorHandleWay'})}
          initialValue={"fail"}
          options={["fail", "skip"]}
        />
        <ProFormSelect
          name={'format'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.format'})}
          rules={[{required: true}]}
          initialValue={"json"}
          options={["json", "text", "canal_json", "debezium_json", "compatible_debezium_json", "compatible_kafka_connect_json"]}
        />
        <ProFormDependency name={['format']}>
          {({format}) => {
            if (format == 'json') {
              return <FieldItem/>
            } else if (format == 'text') {
              return (
                <ProFormText
                  name={KafkaParams.fieldDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.kafka.fieldDelimiter'})}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormSelect
          name={'start_mode'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.startMode'})}
          rules={[{required: true}]}
          initialValue={"group_offsets"}
          options={["earliest", "group_offsets", "latest", "specific_offsets", "timestamp"]}
        />
        <ProFormDependency name={['start_mode']}>
          {({start_mode}) => {
            if (start_mode == 'timestamp') {
              return (
                <ProFormDigit
                  name={KafkaParams.startModeTimestamp}
                  label={intl.formatMessage({id: 'pages.project.di.step.kafka.startModeTimestamp'})}
                  min={0}
                />
              );
            } else if (start_mode == 'specific_offsets') {
              return (
                <ProFormText
                  name={KafkaParams.startModeOffsets}
                  label={intl.formatMessage({id: 'pages.project.di.step.kafka.startModeOffsets'})}
                />
              );
            }
            return <></>;
          }}
        </ProFormDependency>

        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.conf'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.conf.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={KafkaParams.kafkaConf}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.kafka.conf.list'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={KafkaParams.key}
                label={intl.formatMessage({id: 'pages.project.di.step.kafka.conf.key'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.kafka.conf.key.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={KafkaParams.value}
                label={intl.formatMessage({id: 'pages.project.di.step.kafka.conf.value'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.kafka.conf.value.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
};

export default SourceKafkaStepForm;
