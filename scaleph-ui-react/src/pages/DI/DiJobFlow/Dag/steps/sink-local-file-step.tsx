import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {LocalFileParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
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
    JobService.listStepAttr(jobInfo.id + '', nodeInfo.id).then((resp) => {
      resp.map((step) => {
        form.setFieldValue(step.stepAttrKey, step.stepAttrValue);
      });
    });
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
        map.set(LocalFileParams.path, values[LocalFileParams.path]);
        map.set(LocalFileParams.fileNameExpression, values[LocalFileParams.fileNameExpression]);
        map.set(LocalFileParams.fileFormat, values[LocalFileParams.fileFormat]);
        map.set(LocalFileParams.filenameTimeFormat, values[LocalFileParams.filenameTimeFormat]);
        map.set(LocalFileParams.fieldDelimiter, values[LocalFileParams.fieldDelimiter]);
        map.set(LocalFileParams.rowDelimiter, values[LocalFileParams.rowDelimiter]);
        map.set(LocalFileParams.partitionBy, values[LocalFileParams.partitionBy]);
        map.set(LocalFileParams.partitionDirExpression, values[LocalFileParams.partitionDirExpression]);
        map.set(LocalFileParams.isPartitionFieldWriteInFile, values[LocalFileParams.isPartitionFieldWriteInFile]);
        map.set(LocalFileParams.sinkColumns, values[LocalFileParams.sinkColumns]);
        map.set(LocalFileParams.isEnableTransaction, values[LocalFileParams.isEnableTransaction]);
        map.set(LocalFileParams.saveMode, values[LocalFileParams.saveMode]);
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
    <ProForm form={form} grid={true} submitter={false}>
      <ProFormText
        name={STEP_ATTR_TYPE.stepTitle}
        label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
        rules={[{required: true}, {max: 120}]}
        colProps={{span: 24}}
      />
      <ProFormText
        name={LocalFileParams.path}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.path'})}
        rules={[{required: true}]}
        colProps={{span: 24}}
      />
      <ProFormSelect
        name={"file_format"}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.fileFormat'})}
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
                  name={LocalFileParams.fieldDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.localFile.fieldDelimiter'})}
                  rules={[{required: true}]}
                  colProps={{span: 12}}
                />
                <ProFormText
                  name={LocalFileParams.rowDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.localFile.rowDelimiter'})}
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
        name={LocalFileParams.fileNameExpression}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.fileNameExpression'})}
        colProps={{span: 12}}
      />
      <ProFormText
        name={LocalFileParams.filenameTimeFormat}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.filenameTimeFormat'})}
        colProps={{span: 12}}
      />
      <ProFormText
        name={LocalFileParams.partitionBy}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.partitionBy'})}
        colProps={{span: 12}}
      />
      <ProFormText
        name={LocalFileParams.partitionDirExpression}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.partitionDirExpression'})}
        colProps={{span: 12}}
      />
      <ProFormSwitch
        name={LocalFileParams.isPartitionFieldWriteInFile}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.isPartitionFieldWriteInFile'})}
        colProps={{span: 24}}
      />
      <ProFormText
        name={LocalFileParams.sinkColumns}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.sinkColumns'})}
        colProps={{span: 24}}
      />
      <ProFormSwitch
        name={LocalFileParams.isEnableTransaction}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.isEnableTransaction'})}
        colProps={{span: 24}}
        fieldProps={{
          defaultChecked: true,
          disabled: true
        }}
      />
      <ProFormSelect
        name={LocalFileParams.saveMode}
        label={intl.formatMessage({id: 'pages.project.di.step.localFile.saveMode'})}
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
