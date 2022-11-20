import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {BaseFileParams, STEP_ATTR_TYPE} from "../../constant";
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
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {DsInfoParam} from "@/services/datasource/typings";
import {DsInfoService} from "@/services/datasource/info.service";

const SinkOSSFileStepForm: React.FC<ModalFormProps<{
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
        map.set(STEP_ATTR_TYPE.stepAttrs, values);
        JobService.saveStepAttr(map).then((resp) => {
          if (resp.success) {
            message.success(intl.formatMessage({id: 'app.common.operate.success'}));
            onCancel();
            onOK ? onOK(values) : null;
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
      <ProFormSelect
        name={"dataSourceType"}
        label={intl.formatMessage({id: 'pages.project.di.step.dataSourceType'})}
        colProps={{span: 6}}
        initialValue={"OSS"}
        fieldProps={{
          disabled: true
        }}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.datasourceType)}
      />
      <ProFormSelect
        name={STEP_ATTR_TYPE.dataSource}
        label={intl.formatMessage({id: 'pages.project.di.step.dataSource'})}
        rules={[{required: true}]}
        colProps={{span: 18}}
        dependencies={["dataSourceType"]}
        request={((params, props) => {
          const param: DsInfoParam = {
            name: params.keyWords,
            dsType: params.dataSourceType
          };
          return DsInfoService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item};
            });
          });
        })}
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
      />
      <ProFormSwitch
        name={BaseFileParams.isPartitionFieldWriteInFile}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isPartitionFieldWriteInFile'})}
      />
      <ProFormText
        name={BaseFileParams.sinkColumns}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sinkColumns'})}
      />
      <ProFormSwitch
        name={BaseFileParams.isEnableTransaction}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isEnableTransaction'})}
        initialValue={true}
        fieldProps={{
          disabled: true
        }}
      />
    </ProForm>
  </Modal>);
}

export default SinkOSSFileStepForm;
