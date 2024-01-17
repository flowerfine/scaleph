import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {STEP_ATTR_TYPE, WeChatParams} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import {InfoCircleOutlined} from "@ant-design/icons";

const SinkWeChatStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatSchema(values);
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
            colProps={{span: 24}}
          />
          <ProFormText
            name={WeChatParams.url}
            label={intl.formatMessage({id: 'pages.project.di.step.wechat.url'})}
            rules={[{required: true}]}
          />
          <ProFormList
            name={WeChatParams.mentionedArray}
            label={intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedList'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedList.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.wechat.userId'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText name={WeChatParams.userId} colProps={{span: 20, offset: 1}}/>
            </ProFormGroup>
          </ProFormList>

          <ProFormList
            name={WeChatParams.mentionedMobileArray}
            label={intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedMobileList'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedMobileList.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.wechat.mobile'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText name={WeChatParams.mobile} colProps={{span: 20, offset: 1}}/>
            </ProFormGroup>
          </ProFormList>
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkWeChatStepForm;
