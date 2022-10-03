import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {EmailParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
import {JobService} from "@/services/project/job.service";
import {Form, message, Modal} from "antd";
import {DiJob} from "@/services/project/typings";
import {getIntl, getLocale} from "umi";
import {
  ProForm,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {useEffect} from "react";

const SinkEmailStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as DiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
  }, []);

  return (<Modal
    open={visible}
    title={nodeInfo.data.displayName}
    width={780}
    bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
    destroyOnClose={true}
    onCancel={onCancel}
    onOk={() => {
      form.validateFields().then((values) => {
        let map: Map<string, string> = new Map();
        map.set(STEP_ATTR_TYPE.jobId, jobInfo.id + '');
        map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
        map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
        map.set(STEP_ATTR_TYPE.stepAttrs, form.getFieldsValue());
        JobService.saveStepAttr(map).then((resp) => {
          if (resp.success) {
            message.success(intl.formatMessage({id: 'app.common.operate.success'}));
            onCancel();
            onOK ? onOK() : null;
          }
        });
      });
    }}
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
        name={"email_transport_protocol"}
        label={intl.formatMessage({id: 'pages.project.di.step.email.emailTransportProtocol'})}
        rules={[{required: true}]}
        allowClear={false}
        valueEnum={{
          smtp: "smtp"
        }}
      />
      <ProFormDependency name={["email_transport_protocol"]}>
        {({email_transport_protocol}) => {
          if (email_transport_protocol == "smtp") {
            return (
              <ProFormGroup>
                <ProFormSwitch
                  name={"email_smtp_auth"}
                  label={intl.formatMessage({id: 'pages.project.di.step.email.emailSmtpAuth'})}
                  rules={[{required: true}]}
                />
                <ProFormDependency name={["email_smtp_auth"]}>
                  {({email_smtp_auth}) => {
                    if (email_smtp_auth) {
                      return (
                        <ProFormGroup>
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
      />
      <ProFormTextArea
        name={EmailParams.emailMessageContent}
        label={intl.formatMessage({id: 'pages.project.di.step.email.emailMessageContent'})}
      />
    </ProForm>
  </Modal>);
}

export default SinkEmailStepForm;
