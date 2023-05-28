import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {EmailParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {
  ProForm,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {useEffect} from 'react';

const SinkEmailStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as WsDiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Drawer
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll'}}
      destroyOnClose={true}
      onClose={onCancel}
      extra={
        <Button
          type="primary"
          onClick={() => {
            form.validateFields().then((values) => {
              let map: Map<string, any> = new Map();
              map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
              map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
              map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              WsDiJobService.saveStepAttr(map).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.success'}));
                  onOK ? onOK(values) : null;
                }
              });
            });
          }}
        >
          {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
        </Button>
      }
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
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
  );
};

export default SinkEmailStepForm;
