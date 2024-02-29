import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormText, ProFormTextArea} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {DingTalkParams, STEP_ATTR_TYPE} from '../constant';

const SinkDingTalkStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <DrawerForm
        title={data.data.label}
        form={form}
        initialValues={data.data.attrs}
        open={visible}
        onOpenChange={onVisibleChange}
        grid={true}
        width={780}
        drawerProps={{
          styles: {body: {overflowY: 'scroll'}},
          closeIcon: null,
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            onOK(values)
            return Promise.resolve(true)
          }
          return Promise.resolve(false)
        }}
      >
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
          colProps={{span: 24}}
        />
        <ProFormText
          name={DingTalkParams.url}
          label={intl.formatMessage({id: 'pages.project.di.step.dingtalk.url'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={DingTalkParams.secret}
          label={intl.formatMessage({id: 'pages.project.di.step.dingtalk.secret'})}
          rules={[{required: true}]}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkDingTalkStepForm;
