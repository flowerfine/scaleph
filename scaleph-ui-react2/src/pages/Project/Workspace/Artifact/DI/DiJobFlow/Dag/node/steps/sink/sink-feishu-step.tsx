import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {FeishuParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const SinkFeishuStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            StepSchemaService.formatHeader(values);
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
          name={FeishuParams.url}
          label={intl.formatMessage({id: 'pages.project.di.step.feishu.url'})}
          rules={[{required: true}]}
        />
        <ProFormList
          name={FeishuParams.headerArray}
          label={intl.formatMessage({id: 'pages.project.di.step.feishu.headers'})}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.feishu.header'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText
              name={FeishuParams.header}
              label={intl.formatMessage({id: 'pages.project.di.step.feishu.header'})}
              colProps={{span: 10, offset: 1}}
            />
            <ProFormText
              name={FeishuParams.headerValue}
              label={intl.formatMessage({id: 'pages.project.di.step.feishu.value'})}
              colProps={{span: 10, offset: 1}}
            />
          </ProFormGroup>
        </ProFormList>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkFeishuStepForm;
