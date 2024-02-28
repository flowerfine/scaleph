import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {FieldMapperParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";

const TransformFieldMapperStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatFieldMapper(values);
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
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.fieldMapper'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.fieldMapper.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={FieldMapperParams.fieldMapperGroup}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.fieldMapper.fieldMapper'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={FieldMapperParams.srcField}
                label={intl.formatMessage({id: 'pages.project.di.step.fieldMapper.fieldMapper.source'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={FieldMapperParams.destField}
                label={intl.formatMessage({id: 'pages.project.di.step.fieldMapper.fieldMapper.target'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default TransformFieldMapperStepForm;
