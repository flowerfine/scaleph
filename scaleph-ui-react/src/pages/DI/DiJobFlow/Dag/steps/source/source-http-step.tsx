import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {BaseHttpParams, HttpParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
import {JobService} from "@/services/project/job.service";
import {Form, message, Modal} from "antd";
import {DiJob} from "@/services/project/typings";
import {getIntl, getLocale} from "umi";
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {useEffect} from "react";

const SourceHttpFileStepForm: React.FC<ModalFormProps<{
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
      />
      <ProFormText
        name={BaseHttpParams.url}
        label={intl.formatMessage({id: 'pages.project.di.step.baseHttp.url'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name={HttpParams.method}
        label={intl.formatMessage({id: 'pages.project.di.step.http.method'})}
        rules={[{required: true}]}
        allowClear={false}
        valueEnum={{
          GET: "GET",
          POST: "POST"
        }}
      />
      <ProFormTextArea
        name={BaseHttpParams.headers}
        label={intl.formatMessage({id: 'pages.project.di.step.baseHttp.headers'})}
        colProps={{span: 24}}
      />
      <ProFormTextArea
        name={BaseHttpParams.params}
        label={intl.formatMessage({id: 'pages.project.di.step.baseHttp.params'})}
        colProps={{span: 24}}
      />
      <ProFormTextArea
        name={HttpParams.body}
        label={intl.formatMessage({id: 'pages.project.di.step.http.body'})}
        colProps={{span: 24}}
      />
      <ProFormSelect
        name={"format"}
        label={intl.formatMessage({id: 'pages.project.di.step.http.format'})}
        rules={[{required: true}]}
        allowClear={false}
        valueEnum={{
          json: "json",
          text: "text"
        }}
      />
      <ProFormDependency name={["format"]}>
        {({format}) => {
          if (format == "json") {
            return (
              <ProFormGroup>
                <ProFormTextArea
                  name={HttpParams.schema}
                  label={intl.formatMessage({id: 'pages.project.di.step.http.schema'})}
                  rules={[{required: true}]}
                />
              </ProFormGroup>
            );
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
      <ProFormDigit
        name={HttpParams.pollIntervalMs}
        label={intl.formatMessage({id: 'pages.project.di.step.http.pollIntervalMs'})}
        colProps={{span: 24}}
      />
      <ProFormDigit
        name={BaseHttpParams.retry}
        label={intl.formatMessage({id: 'pages.project.di.step.baseHttp.retry'})}
        colProps={{span: 6}}
      />
      <ProFormDigit
        name={BaseHttpParams.retryBackoffMultiplierMs}
        label={intl.formatMessage({id: 'pages.project.di.step.baseHttp.retryBackoffMultiplierMs'})}
        colProps={{span: 9}}
      />
      <ProFormDigit
        name={BaseHttpParams.retryBackoffMaxMs}
        label={intl.formatMessage({id: 'pages.project.di.step.baseHttp.retryBackoffMaxMs'})}
        colProps={{span: 9}}
      />
    </ProForm>
  </Modal>);
}

export default SourceHttpFileStepForm;
