import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {BaseFileParams, FtpFileParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
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

const SourceFtpFileStepForm: React.FC<ModalFormProps<{
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
        map.set(STEP_ATTR_TYPE.stepTitle, values[STEP_ATTR_TYPE.stepTitle]);
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
        name={FtpFileParams.host}
        label={intl.formatMessage({id: 'pages.project.di.step.ftpFile.host'})}
        rules={[{required: true}]}
      />
      <ProFormDigit
        name={FtpFileParams.port}
        label={intl.formatMessage({id: 'pages.project.di.step.ftpFile.port'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={FtpFileParams.username}
        label={intl.formatMessage({id: 'pages.project.di.step.ftpFile.username'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={FtpFileParams.password}
        label={intl.formatMessage({id: 'pages.project.di.step.ftpFile.password'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={BaseFileParams.path}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name={"type"}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.type'})}
        rules={[{required: true}]}
        valueEnum={{
          json: "json",
          parquet: "parquet",
          orc: "orc",
          text: "text",
          csv: "csv"
        }}
      />
      <ProFormDependency name={["type"]}>
        {({type}) => {
          if (type == "json") {
            return (
              <ProFormGroup>
                <ProFormTextArea
                  name={BaseFileParams.schema}
                  label={intl.formatMessage({id: 'pages.project.di.step.baseFile.schema'})}
                  rules={[{required: true}]}
                />
              </ProFormGroup>
            );
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
    </ProForm>
  </Modal>);
}

export default SourceFtpFileStepForm;
