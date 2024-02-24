import {ModalFormProps} from '@/app.d';
import {NsGraph} from '@antv/xflow';
import {getIntl, getLocale} from 'umi';
import {WsDiJob} from '@/services/project/typings';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {RocketMQParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';

const SinkRocketMQStepForm: React.FC<
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
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              values[RocketMQParams.aclEnabled] = values[RocketMQParams.aclEnabledField]
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
          colProps={{span: 24}}
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
          name={RocketMQParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.topic'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={RocketMQParams.exactlyOnce}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.exactlyOnce'})}
          initialValue={false}
        />

        <ProFormText
          name={RocketMQParams.partitionKeyFields}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionKeyFields'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionKeyFields.placeholder'})}
        />
        <ProFormSwitch
          name={RocketMQParams.producerSendSync}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.producerSendSync'})}
          colProps={{span: 8}}
          initialValue={false}
        />
        <ProFormDigit
          name={RocketMQParams.maxMessageSize}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.maxMessageSize'})}
          colProps={{span: 8}}
          initialValue={1024 * 1024 * 4}
          fieldProps={{
            step: 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={RocketMQParams.sendMessageTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.sendMessageTimeout'})}
          colProps={{span: 8}}
          initialValue={3000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
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
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </ProForm>
    </Drawer>
  );
};

export default SinkRocketMQStepForm;
