import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {BaseHttpParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
import {JobService} from "@/services/project/job.service";
import {Form, message, Modal} from "antd";
import {DiJob} from "@/services/project/typings";
import {getIntl, getLocale} from "umi";
import {ProForm, ProFormDigit, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useEffect} from "react";

const SinkHttpFileStepForm: React.FC<ModalFormProps<{
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
        name={BaseHttpParams.url}
        label={intl.formatMessage({id: 'pages.project.di.step.baseHttp.url'})}
        rules={[{required: true}]}
        colProps={{span: 24}}
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

export default SinkHttpFileStepForm;
