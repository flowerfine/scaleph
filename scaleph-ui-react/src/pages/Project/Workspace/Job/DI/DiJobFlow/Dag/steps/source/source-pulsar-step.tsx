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
  ProFormText,
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {PulsarParams, SchemaParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from '@ant-design/icons';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";

const SourcePulsarStepForm: React.FC<ModalFormProps<{
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
          StepSchemaService.formatSchema(values);
          values[PulsarParams.cursorStartupMode] = values.startMode;
          values[PulsarParams.cursorStopMode] = values.stopMode;
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
        <DataSourceItem dataSource={"Pulsar"}/>
        <ProFormText
          name={PulsarParams.subscriptionName}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.subscriptionName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PulsarParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.topic'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.pulsar.topic.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormText
          name={PulsarParams.topicPattern}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.topicPattern'})}
        />
        <ProFormDigit
          name={PulsarParams.topicDiscoveryInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.topicDiscoveryInterval'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.pulsar.topicDiscoveryInterval.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
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
        <ProFormDigit
          name={PulsarParams.pollTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pollTimeout'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={PulsarParams.pollInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pollInterval'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={PulsarParams.pollBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pollBatchSize'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 100,
            min: 0,
          }}
        />
        <ProFormSelect
          name={'startMode'}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.cursorStartupMode'})}
          allowClear={false}
          initialValue={'LATEST'}
          options={['LATEST', 'EARLIEST', 'SUBSCRIPTION', 'TIMESTAMP']}
        />
        <ProFormDependency name={['startMode']}>
          {({startMode}) => {
            if (startMode == 'TIMESTAMP') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={PulsarParams.cursorStartupTimestamp}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.pulsar.cursorStartupTimestamp',
                    })}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            } else if (startMode == 'SUBSCRIPTION') {
              return (
                <ProFormGroup>
                  <ProFormSelect
                    name={PulsarParams.cursorResetMode}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.pulsar.cursorResetMode',
                    })}
                    allowClear={false}
                    initialValue={'LATEST'}
                    options={['LATEST', 'EARLIEST']}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSelect
          name={'stopMode'}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.cursorStopMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.pulsar.cursorStopMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          allowClear={false}
          initialValue={'NEVER'}
          options={['NEVER', 'LATEST', 'TIMESTAMP']}
        />
        <ProFormDependency name={['stopMode']}>
          {({stopMode}) => {
            if (stopMode == 'TIMESTAMP') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={PulsarParams.cursorStopTimestamp}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.pulsar.cursorStopTimestamp',
                    })}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </ProForm>
    </Modal>
  );
};

export default SourcePulsarStepForm;
