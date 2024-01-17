import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {
  ProForm,
  ProFormDependency,
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
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const SinkEmailStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                              <ProFormTextArea
                                name={EmailParams.emailAuthorizationCode}
                                label={intl.formatMessage({
                                  id: 'pages.project.di.step.email.emailAuthorizationCode',
                                })}
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkEmailStepForm;
