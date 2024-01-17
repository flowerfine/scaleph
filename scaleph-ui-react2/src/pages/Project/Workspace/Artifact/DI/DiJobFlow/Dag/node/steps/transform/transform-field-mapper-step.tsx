import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {FieldMapperParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const TransformFieldMapperStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatFieldMapper(values);
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default TransformFieldMapperStepForm;
