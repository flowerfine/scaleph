import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {SplitParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";

const TransformSplitStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatSplitOutputFields(values);
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
        />
        <ProFormText
          name={SplitParams.separator}
          label={intl.formatMessage({id: 'pages.project.di.step.split.separator'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={SplitParams.splitField}
          label={intl.formatMessage({id: 'pages.project.di.step.split.splitField'})}
          rules={[{required: true}]}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.split.outputFields'})}
        >
          <ProFormList
            name={SplitParams.outputFieldArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.split.outputField'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={SplitParams.outputField}
                colProps={{span: 16, offset: 4}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default TransformSplitStepForm;
