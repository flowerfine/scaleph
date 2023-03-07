import { NsGraph } from '@antv/xflow';
import { ModalFormProps } from '@/app.d';
import { WsDiJobService } from '@/services/project/WsDiJob.service';
import { Button, Drawer, Form, message } from 'antd';
import { WsDiJob } from '@/services/project/typings';
import { getIntl, getLocale } from 'umi';
import { InfoCircleOutlined } from '@ant-design/icons';
import { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormText,
} from '@ant-design/pro-components';
import { FakeParams, SchemaParams, STEP_ATTR_TYPE } from '../../constant';
import { StepSchemaService } from '../helper';

const SourceFakeStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({ data, visible, onCancel, onOK }) => {
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
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
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
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              WsDiJobService.saveStepAttr(map).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                  onOK ? onOK(values) : null;
                }
              });
            });
          }}
        >
          {intl.formatMessage({ id: 'app.common.operate.confirm.label' })}
        </Button>
      }
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
          rules={[{ required: true }, { max: 120 }]}
        />
        <ProFormDigit
          name={FakeParams.rowNum}
          label={intl.formatMessage({ id: 'pages.project.di.step.fake.rowNum' })}
          initialValue={10}
          fieldProps={{
            step: 100,
            min: 1,
          }}
        />
        <ProFormDigit
          name={FakeParams.splitNum}
          label={intl.formatMessage({ id: 'pages.project.di.step.fake.splitNum' })}
          tooltip={{
            title: intl.formatMessage({ id: 'pages.project.di.step.fake.splitNum.tooltip' }),
            icon: <InfoCircleOutlined />,
          }}
          initialValue={1}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={FakeParams.splitReadInterval}
          label={intl.formatMessage({ id: 'pages.project.di.step.fake.splitReadInterval' })}
          tooltip={{
            title: intl.formatMessage({
              id: 'pages.project.di.step.fake.splitReadInterval.tooltip',
            }),
            icon: <InfoCircleOutlined />,
          }}
          initialValue={1}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={FakeParams.mapSize}
          label={intl.formatMessage({ id: 'pages.project.di.step.fake.mapSize' })}
          initialValue={5}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={FakeParams.arraySize}
          label={intl.formatMessage({ id: 'pages.project.di.step.fake.arraySize' })}
          initialValue={5}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={FakeParams.bytesLength}
          label={intl.formatMessage({ id: 'pages.project.di.step.fake.bytesLength' })}
          initialValue={5}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={FakeParams.stringLength}
          label={intl.formatMessage({ id: 'pages.project.di.step.fake.stringLength' })}
          initialValue={5}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormGroup
          label={intl.formatMessage({ id: 'pages.project.di.step.schema' })}
          tooltip={{
            title: intl.formatMessage({ id: 'pages.project.di.step.schema.tooltip' }),
            icon: <InfoCircleOutlined />,
          }}
        >
          <ProFormList
            name={SchemaParams.fields}
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
      </ProForm>
    </Drawer>
  );
};

export default SourceFakeStepForm;
