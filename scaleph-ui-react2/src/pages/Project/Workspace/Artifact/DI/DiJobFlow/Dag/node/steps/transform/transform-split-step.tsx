import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {SplitParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const TransformSplitStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatSplitOutputFields(values);
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default TransformSplitStepForm;
