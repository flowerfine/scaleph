import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {STEP_ATTR_TYPE, WeChatParams} from '../constant';
import {StepSchemaService} from "../helper";

const SinkWeChatStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatUserIds(values);
            StepSchemaService.formatMobiles(values);
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
      </DrawerForm>
    </XFlow>
  );
};

export default SinkWeChatStepForm;
