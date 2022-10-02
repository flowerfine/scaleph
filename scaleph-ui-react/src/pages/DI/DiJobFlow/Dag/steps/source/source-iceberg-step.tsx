import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {IcebergParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
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
  ProFormSwitch,
  ProFormText
} from "@ant-design/pro-components";
import {useEffect} from "react";

const SourceIcebergStepForm: React.FC<ModalFormProps<{
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
      <ProFormSelect
        name={IcebergParams.catalogType}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogType'})}
        rules={[{required: true}]}
        colProps={{span: 24}}
        valueEnum={{
          hive: "Hive",
          hadoop: "Hadoop"
        }}
      />
      <ProFormDependency name={["catalog_type"]}>
        {({catalog_type}) => {
          if (catalog_type == 'hive') {
            return (
              <ProFormGroup>
                <ProFormText
                  name={IcebergParams.uri}
                  label={intl.formatMessage({id: 'pages.project.di.step.iceberg.uri'})}
                  rules={[{required: true}]}
                  colProps={{span: 24}}
                />
              </ProFormGroup>
            );
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
      <ProFormText
        name={IcebergParams.catalogName}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogName'})}
        rules={[{required: true}]}
        colProps={{span: 6}}
      />
      <ProFormText
        name={IcebergParams.namespace}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.namespace'})}
        rules={[{required: true}]}
        colProps={{span: 6}}
      />
      <ProFormText
        name={IcebergParams.table}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.table'})}
        rules={[{required: true}]}
        colProps={{span: 12}}
      />
      <ProFormText
        name={IcebergParams.warehouse}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.warehouse'})}
        rules={[{required: true}]}
        colProps={{span: 24}}
      />
      <ProFormSwitch
        name={IcebergParams.caseSensitive}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.caseSensitive'})}
        colProps={{span: 6}}
      />
      <ProFormSelect
        name={IcebergParams.streamScanStrategy}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.streamScanStrategy'})}
        colProps={{span: 18}}
        allowClear={false}
        fieldProps={{
          defaultValue: "FROM_LATEST_SNAPSHOT"
        }}
        valueEnum={{
          FROM_LATEST_SNAPSHOT: "FROM_LATEST_SNAPSHOT",
          FROM_EARLIEST_SNAPSHOT: "FROM_EARLIEST_SNAPSHOT",
          FROM_SNAPSHOT_ID: "FROM_SNAPSHOT_ID",
          FROM_SNAPSHOT_TIMESTAMP: "FROM_SNAPSHOT_TIMESTAMP",
          TABLE_SCAN_THEN_INCREMENTAL: "TABLE_SCAN_THEN_INCREMENTAL"
        }}
      />
      <ProFormText
        name={IcebergParams.fields}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.fields'})}
        colProps={{span: 24}}
      />
      <ProFormSwitch
        name={"use_snapshot_id"}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.useSnapshotId'})}
        colProps={{span: 24}}
      />
      <ProFormDependency name={["use_snapshot_id"]}>
        {({use_snapshot_id}) => {
          if (use_snapshot_id) {
            return (
              <ProFormGroup>
                <ProFormDigit
                  name={IcebergParams.startSnapshotId}
                  label={intl.formatMessage({id: 'pages.project.di.step.iceberg.startSnapshotId'})}
                  rules={[{required: true}]}
                  colProps={{span: 12}}
                />
                <ProFormDigit
                  name={IcebergParams.endSnapshotId}
                  label={intl.formatMessage({id: 'pages.project.di.step.iceberg.endSnapshotId'})}
                  rules={[{required: true}]}
                  colProps={{span: 12}}
                />
              </ProFormGroup>
            );
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
      <ProFormDigit
        name={IcebergParams.startSnapshotTimestamp}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.startSnapshotTimestamp'})}
        colProps={{span: 12}}
      />
      <ProFormDigit
        name={IcebergParams.useSnapshotTimestamp}
        label={intl.formatMessage({id: 'pages.project.di.step.iceberg.useSnapshotTimestamp'})}
        colProps={{span: 12}}
      />
    </ProForm>
  </Modal>);
}

export default SourceIcebergStepForm;
