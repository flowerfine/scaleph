import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {DiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDependency,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {KafkaParams, SchemaParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";
import {StepSchemaService} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/helper";

const SourceKafkaStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as DiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Modal
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let map: Map<string, any> = new Map();
          map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
          map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
          map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
          StepSchemaService.formatSchema(values)
          StepSchemaService.formatKafkaConf(values)
          map.set(STEP_ATTR_TYPE.stepAttrs, values);
          JobService.saveStepAttr(map).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.success'}));
              onCancel();
              onOK ? onOK(values) : null;
            }
          });
        });
      }}
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
          name={KafkaParams.pattern}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.pattern'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.pattern.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={false}
        />
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
          name={'format'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.format'})}
          rules={[{required: true}]}
          initialValue={"json"}
          valueEnum={{
            json: 'json',
            text: 'text'
          }}
        />
        <ProFormDependency name={['format']}>
          {({format}) => {
            if (format == 'json') {
              return (
                <ProFormGroup
                  label={intl.formatMessage({id: 'pages.project.di.step.schema'})}
                  tooltip={{
                    title: intl.formatMessage({id: 'pages.project.di.step.schema.tooltip'}),
                    icon: <InfoCircleOutlined/>,
                  }}
                >
                  <ProFormList
                    name={SchemaParams.fields}
                    copyIconProps={false}
                    creatorButtonProps={{
                      creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.schema.fields'}),
                      type: 'text',
                    }}
                  >
                    <ProFormGroup>
                      <ProFormText
                        name={SchemaParams.field}
                        label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.field'})}
                        colProps={{span: 10, offset: 1}}
                      />
                      <ProFormText
                        name={SchemaParams.type}
                        label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.type'})}
                        colProps={{span: 10, offset: 1}}
                      />
                    </ProFormGroup>
                  </ProFormList>
                </ProFormGroup>
              );
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
                addonBefore={"kafka."}
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
    </Modal>
  );
};

export default SourceKafkaStepForm;
