import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {HttpParams, STEP_ATTR_TYPE} from "../../constant";
import {JobService} from "@/services/project/job.service";
import {Form, message, Modal} from "antd";
import {DiJob} from "@/services/project/typings";
import {getIntl, getLocale} from "umi";
import {ProForm, ProFormDigit, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {useEffect} from "react";
import { StepSchemaService } from "../schema";

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
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
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
        let map: Map<string, any> = new Map();
        map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
        map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
        map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
        StepSchemaService.formatHeader(values)
        StepSchemaService.formatParam(values)
        map.set(STEP_ATTR_TYPE.stepAttrs, values);
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
        name={HttpParams.url}
        label={intl.formatMessage({id: 'pages.project.di.step.http.url'})}
        rules={[{required: true}]}
        colProps={{span: 24}}
      />
      <ProFormList
        name={HttpParams.headerArray}
        label={intl.formatMessage({id: 'pages.project.di.step.http.headers'})}
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.http.headers'}),
          type: 'text',
        }}>
        <ProFormGroup>
          <ProFormText
            name={HttpParams.header}
            label={intl.formatMessage({id: 'pages.project.di.step.http.header'})}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name={HttpParams.headerValue}
            label={intl.formatMessage({id: 'pages.project.di.step.http.value'})}
            colProps={{span: 10, offset: 1}}
          />
        </ProFormGroup>
      </ProFormList>
      <ProFormList
        name={HttpParams.paramArray}
        label={intl.formatMessage({id: 'pages.project.di.step.http.params'})}
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.http.params'}),
          type: 'text',
        }}>
        <ProFormGroup>
          <ProFormText
            name={HttpParams.param}
            label={intl.formatMessage({id: 'pages.project.di.step.http.param'})}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name={HttpParams.paramValue}
            label={intl.formatMessage({id: 'pages.project.di.step.http.value'})}
            colProps={{span: 10, offset: 1}}
          />
        </ProFormGroup>
      </ProFormList>
      <ProFormDigit
        name={HttpParams.retry}
        label={intl.formatMessage({id: 'pages.project.di.step.http.retry'})}
        colProps={{span: 6}}
      />
      <ProFormDigit
        name={HttpParams.retryBackoffMultiplierMs}
        label={intl.formatMessage({id: 'pages.project.di.step.http.retryBackoffMultiplierMs'})}
        colProps={{span: 9}}
      />
      <ProFormDigit
        name={HttpParams.retryBackoffMaxMs}
        label={intl.formatMessage({id: 'pages.project.di.step.http.retryBackoffMaxMs'})}
        colProps={{span: 9}}
      />
    </ProForm>
  </Modal>);
}

export default SinkHttpFileStepForm;
