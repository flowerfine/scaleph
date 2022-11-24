import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {DiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormText
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {KafkaParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";
import {StepSchemaService} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/helper";

const SinkKafkaStepForm: React.FC<ModalFormProps<{
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
          StepSchemaService.formatAssginPartitions(values)
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
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"semantic"}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.semantic'})}
          allowClear={false}
          initialValue={"AT_LEAST_ONCE"}
          options={["EXACTLY_ONCE", "AT_LEAST_ONCE", "NON"]}
        />
        <ProFormDependency name={['semantic']}>
          {({semantic}) => {
            if (semantic == 'EXACTLY_ONCE') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={KafkaParams.transactionPrefix}
                    label={intl.formatMessage({id: 'pages.project.di.step.kafka.transactionPrefix'})}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormText
          name={KafkaParams.partitionKey}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.partitionKey'})}
        />
        <ProFormDigit
          name={KafkaParams.partition}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.partition'})}
          min={0}
        />
        <ProFormGroup label={intl.formatMessage({id: 'pages.project.di.step.kafka.assignPartitions'})}>
          <ProFormList
            name={KafkaParams.assignPartitionArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.kafka.assignPartition'}),
              type: 'text',
            }}
          >
            <ProFormText name={KafkaParams.assignPartition}/>
          </ProFormList>
        </ProFormGroup>
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

export default SinkKafkaStepForm;
