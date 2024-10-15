import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
  ProFormDependency, ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {EmailParams, STEP_ATTR_TYPE} from '../constant';

const SinkEmailStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          name={EmailParams.emailHost}
          label={intl.formatMessage({id: 'pages.project.di.step.email.emailHost'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={'email_transport_protocol'}
          label={intl.formatMessage({id: 'pages.project.di.step.email.emailTransportProtocol'})}
          rules={[{required: true}]}
          allowClear={false}
          initialValue={'smtp'}
          valueEnum={{
            smtp: 'smtp',
          }}
        />
        <ProFormDependency name={['email_transport_protocol']}>
          {({email_transport_protocol}) => {
            if (email_transport_protocol == 'smtp') {
              return (
                <ProFormGroup>
                  <ProFormSwitch
                    name={'email_smtp_auth'}
                    label={intl.formatMessage({id: 'pages.project.di.step.email.emailSmtpAuth'})}
                    rules={[{required: true}]}
                  />
                  <ProFormDependency name={['email_smtp_auth']}>
                    {({email_smtp_auth}) => {
                      if (email_smtp_auth) {
                        return (
                          <ProFormGroup>
                            <ProFormDigit
                              name={EmailParams.emailSmtpPort}
                              label={intl.formatMessage({id: 'pages.project.di.step.email.emailSmtpPort'})}
                              min={0}
                              initialValue={465}
                            />
                            <ProFormTextArea
                              name={EmailParams.emailAuthorizationCode}
                              label={intl.formatMessage({id: 'pages.project.di.step.email.emailAuthorizationCode'})}
                              rules={[{required: true}]}
                            />
                          </ProFormGroup>
                        );
                      }
                      return <ProFormGroup/>;
                    }}
                  </ProFormDependency>
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormText
          name={EmailParams.emailFromAddress}
          label={intl.formatMessage({id: 'pages.project.di.step.email.emailFromAddress'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={EmailParams.emailToAddress}
          label={intl.formatMessage({id: 'pages.project.di.step.email.emailToAddress'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={EmailParams.emailMessageHeadline}
          label={intl.formatMessage({id: 'pages.project.di.step.email.emailMessageHeadline'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={EmailParams.emailMessageContent}
          label={intl.formatMessage({id: 'pages.project.di.step.email.emailMessageContent'})}
          rules={[{required: true}]}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkEmailStepForm;
