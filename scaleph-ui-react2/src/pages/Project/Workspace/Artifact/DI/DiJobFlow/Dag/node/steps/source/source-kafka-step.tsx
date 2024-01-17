import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {KafkaParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/fields";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SourceKafkaStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <Drawer
        open={visible}
        title={data.data.label}
        width={780}
        bodyStyle={{overflowY: 'scroll'}}
        destroyOnClose={true}
        onClose={onCancel}
        extra={
          <Button
            type="primary"
            onClick={() => {
              form.validateFields().then((values) => {
                StepSchemaService.formatSchema(values)
                StepSchemaService.formatKafkaConf(values)
                if (onOK) {
                  onOK(values);
                }
              });
            }}
          >
            {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
          </Button>
        }
      >
        <ProForm form={form} initialValues={data.data.attrs} grid={true} submitter={false}>
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
    </XFlow>
  );
};

export default SourceKafkaStepForm;
