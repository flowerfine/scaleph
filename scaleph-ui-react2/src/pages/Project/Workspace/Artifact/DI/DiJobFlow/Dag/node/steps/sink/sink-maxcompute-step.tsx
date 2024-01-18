import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {MaxComputeParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkMaxComputeStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <DataSourceItem dataSource={'MaxCompute'}/>
          <ProFormText
            name={MaxComputeParams.project}
            label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.project'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={MaxComputeParams.tableName}
            label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.tableName'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={MaxComputeParams.partitionSpec}
            label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.partitionSpec'})}
          />
          <ProFormSwitch
            name={MaxComputeParams.overwrite}
            label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.overwrite'})}
            initialValue={false}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkMaxComputeStepForm;