import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {STEP_ATTR_TYPE, WeChatParams} from '../constant';
import {StepSchemaService} from "../helper";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";

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
            StepSchemaService.formatCommonList(values, WeChatParams.mentionedList, WeChatParams.mentionedList);
            StepSchemaService.formatCommonList(values, WeChatParams.mentionedMobileList, WeChatParams.mentionedMobileList);
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
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedList'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedList.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={WeChatParams.mentionedList}/>
        </ProFormGroup>

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedMobileList'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedMobileList.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={WeChatParams.mentionedMobileList}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkWeChatStepForm;
