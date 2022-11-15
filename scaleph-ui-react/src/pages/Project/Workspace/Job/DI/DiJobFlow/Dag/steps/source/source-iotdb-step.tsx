import { NsGraph } from '@antv/xflow';
import { ModalFormProps } from '@/app.d';
import { IoTDBParams, SchemaParams, STEP_ATTR_TYPE } from '../../constant';
import { JobService } from '@/services/project/job.service';
import { Form, message, Modal } from 'antd';
import { DiJob } from '@/services/project/typings';
import { getIntl, getLocale } from 'umi';
import { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { InfoCircleOutlined } from '@ant-design/icons';
import { StepSchemaService } from '../schema';

const SourceIoTDBStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({ data, visible, onCancel, onOK }) => {
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
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let map: Map<string, any> = new Map();
          map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
          map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
          map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
          StepSchemaService.formatFields(values);
          map.set(STEP_ATTR_TYPE.stepAttrs, values);
          JobService.saveStepAttr(map).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
              onCancel();
              onOK ? onOK() : null;
            }
          });
        });
      }}
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
          rules={[{ required: true }, { max: 120 }]}
        />
        <ProFormText
          name={IoTDBParams.nodeUrls}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.nodeUrls' })}
          rules={[{ required: true }]}
        />
        <ProFormText
          name={IoTDBParams.username}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.username' })}
          rules={[{ required: true }]}
        />
        <ProFormText
          name={IoTDBParams.password}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.password' })}
          rules={[{ required: true }]}
        />
        <ProFormTextArea
          name={IoTDBParams.sql}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.sql' })}
          rules={[{ required: true }]}
        />
        <ProFormGroup
          label={intl.formatMessage({ id: 'pages.project.di.step.schema' })}
          tooltip={{
            title: intl.formatMessage({ id: 'pages.project.di.step.schema.tooltip' }),
            icon: <InfoCircleOutlined />,
          }}
        >
          <ProFormList
            name={IoTDBParams.fieldArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({ id: 'pages.project.di.step.schema.fields' }),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={SchemaParams.field}
                label={intl.formatMessage({ id: 'pages.project.di.step.schema.fields.field' })}
                colProps={{ span: 10, offset: 1 }}
              />
              <ProFormText
                name={SchemaParams.type}
                label={intl.formatMessage({ id: 'pages.project.di.step.schema.fields.type' })}
                colProps={{ span: 10, offset: 1 }}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
        <ProFormDigit
          name={IoTDBParams.fetchSize}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.fetchSize' })}
          min={1}
        />
        <ProFormDigit
          name={IoTDBParams.thriftDefaultBufferSize}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.thriftDefaultBufferSize' })}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.thriftMaxFrameSize}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.thriftMaxFrameSize' })}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormSwitch
          name={IoTDBParams.enableCacheLeader}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.enableCacheLeader' })}
        />
        <ProFormSelect
          name={IoTDBParams.version}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.version' })}
          valueEnum={{ V_0_12: 'V_0_12', V_0_13: 'V_0_13' }}
        />
        <ProFormDigit
          name={IoTDBParams.numPartitions}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.numPartitions' })}
          colProps={{ span: 8 }}
          fieldProps={{
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.lowerBound}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.lowerBound' })}
          colProps={{ span: 8 }}
          fieldProps={{
            step: 100000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.upperBound}
          label={intl.formatMessage({ id: 'pages.project.di.step.iotdb.upperBound' })}
          colProps={{ span: 8 }}
          fieldProps={{
            step: 100000,
            min: 1,
          }}
        />
      </ProForm>
    </Modal>
  );
};

export default SourceIoTDBStepForm;
