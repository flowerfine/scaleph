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
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {KafkaParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkKafkaStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatSchema(values);
                StepSchemaService.formatKafkaConf(values);
                StepSchemaService.formatPartitionKeyFields(values);
                StepSchemaService.formatAssginPartitions(values);
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
    </XFlow>
  );
};

export default SinkKafkaStepForm;
