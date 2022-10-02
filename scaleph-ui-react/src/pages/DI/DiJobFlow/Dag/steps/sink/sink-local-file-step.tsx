import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {BaseFileParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
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
  ProFormText
} from "@ant-design/pro-components";
import {useEffect} from "react";

const SinkLocalFileStepForm: React.FC<ModalFormProps<{
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
        name={BaseFileParams.path}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
        rules={[{required: true}]}
        colProps={{span: 24}}
      />
      <ProFormSelect
        name={"file_format"}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFormat'})}
        colProps={{span: 24}}
        valueEnum={{
          json: "json",
          parquet: "parquet",
          orc: "orc",
          text: "text",
          csv: "csv"
        }}
      />
      <ProFormDependency name={["file_format"]}>
        {({file_format}) => {
          if (file_format == "text" || file_format == "csv") {
            return (
              <ProFormGroup>
                <ProFormText
                  name={BaseFileParams.fieldDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fieldDelimiter'})}
                  rules={[{required: true}]}
                  colProps={{span: 12}}
                />
                <ProFormText
                  name={BaseFileParams.rowDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.baseFile.rowDelimiter'})}
                  rules={[{required: true}]}
                  colProps={{span: 12}}
                />
              </ProFormGroup>
            );
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
      <ProFormText
        name={BaseFileParams.fileNameExpression}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileNameExpression'})}
        colProps={{span: 12}}
      />
      <ProFormText
        name={BaseFileParams.filenameTimeFormat}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.filenameTimeFormat'})}
        colProps={{span: 12}}
      />
      <ProFormText
        name={BaseFileParams.partitionBy}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionBy'})}
        colProps={{span: 12}}
      />
      <ProFormText
        name={BaseFileParams.partitionDirExpression}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionDirExpression'})}
        colProps={{span: 12}}
      />
      <ProFormSwitch
        name={BaseFileParams.isPartitionFieldWriteInFile}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isPartitionFieldWriteInFile'})}
        colProps={{span: 24}}
      />
      <ProFormText
        name={BaseFileParams.sinkColumns}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sinkColumns'})}
        colProps={{span: 24}}
      />
      <ProFormSwitch
        name={BaseFileParams.isEnableTransaction}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isEnableTransaction'})}
        colProps={{span: 24}}
        fieldProps={{
          defaultChecked: true,
          disabled: true
        }}
      />
      <ProFormSelect
        name={BaseFileParams.saveMode}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.saveMode'})}
        colProps={{span: 24}}
        allowClear={false}
        fieldProps={{
          defaultValue: "overwrite"
        }}
        valueEnum={{
          overwrite: "overwrite"
        }}
      />
    </ProForm>
  </Modal>);
}

export default SinkLocalFileStepForm;
