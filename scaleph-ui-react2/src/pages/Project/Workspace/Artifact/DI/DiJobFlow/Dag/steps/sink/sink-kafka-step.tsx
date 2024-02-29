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
  ProFormText,
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {KafkaParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from '@ant-design/icons';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import {StepSchemaService} from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper';

const SinkKafkaStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({data, visible, onCancel, onOK}) => {
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
      bodyStyle={{overflowY: 'scroll'}}
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
              StepSchemaService.formatSchema(values);
              StepSchemaService.formatKafkaConf(values);
              StepSchemaService.formatPartitionKeyFields(values);
              StepSchemaService.formatAssginPartitions(values);
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
        <DataSourceItem dataSource={'Kafka'}/>
        <ProFormText
          name={KafkaParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.topic'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={'semantic'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.semantic'})}
          allowClear={false}
          initialValue={'AT_LEAST_ONCE'}
          options={['EXACTLY_ONCE', 'AT_LEAST_ONCE', 'NON']}
        />
        <ProFormDependency name={['semantic']}>
          {({semantic}) => {
            if (semantic == 'EXACTLY_ONCE') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={KafkaParams.transactionPrefix}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.kafka.transactionPrefix',
                    })}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.partitionKeyFields'})}
        >
          <ProFormList
            name={KafkaParams.partitionKeyArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.kafka.partitionKey',
              }),
              type: 'text',
            }}
          >
            <ProFormText name={KafkaParams.partitionKey}/>
          </ProFormList>
        </ProFormGroup>
        <ProFormDigit
          name={KafkaParams.partition}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.partition'})}
          min={0}
        />
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.assignPartitions'})}
        >
          <ProFormList
            name={KafkaParams.assignPartitionArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.kafka.assignPartition',
              }),
              type: 'text',
            }}
          >
            <ProFormText name={KafkaParams.assignPartition}/>
          </ProFormList>
        </ProFormGroup>
        <ProFormSelect
          name={'format'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.format'})}
          rules={[{required: true}]}
          initialValue={'json'}
          options={['json', 'text', "canal_json", "debezium_json", "compatible_debezium_json", "compatible_kafka_connect_json"]}
        />
        <ProFormDependency name={['format']}>
          {({format}) => {
            if (format == 'text') {
              return (
                <ProFormText
                  name={KafkaParams.fieldDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.kafka.fieldDelimiter'})}
                  initialValue={','}
                />
              );
            }
            return <ProFormGroup/>;
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
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.kafka.conf.list',
              }),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={KafkaParams.key}
                label={intl.formatMessage({id: 'pages.project.di.step.kafka.conf.key'})}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.kafka.conf.key.placeholder',
                })}
                colProps={{span: 10, offset: 1}}
                addonBefore={'kafka.'}
              />
              <ProFormText
                name={KafkaParams.value}
                label={intl.formatMessage({id: 'pages.project.di.step.kafka.conf.value'})}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.kafka.conf.value.placeholder',
                })}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
};

export default SinkKafkaStepForm;
