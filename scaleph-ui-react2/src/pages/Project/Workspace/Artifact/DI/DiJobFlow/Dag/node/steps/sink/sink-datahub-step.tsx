import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProForm, ProFormDigit, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {DatahubParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkDatahubStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <DataSourceItem dataSource={'DataHub'}/>
          <ProFormText
            name={DatahubParams.project}
            label={intl.formatMessage({id: 'pages.project.di.step.datahub.project'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={DatahubParams.topic}
            label={intl.formatMessage({id: 'pages.project.di.step.datahub.topic'})}
            rules={[{required: true}]}
          />
          <ProFormDigit
            name={DatahubParams.timeout}
            label={intl.formatMessage({id: 'pages.project.di.step.datahub.timeout'})}
            initialValue={1000}
            fieldProps={{
              step: 1000,
              min: 0,
            }}
          />
          <ProFormDigit
            name={DatahubParams.retryTimes}
            label={intl.formatMessage({id: 'pages.project.di.step.datahub.retryTimes'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.datahub.retryTimes.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            initialValue={0}
            fieldProps={{
              min: 0,
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkDatahubStepForm;
