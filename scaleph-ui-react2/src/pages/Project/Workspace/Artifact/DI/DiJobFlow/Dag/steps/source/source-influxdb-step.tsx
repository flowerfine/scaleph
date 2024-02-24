import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {InfluxDBParams, STEP_ATTR_TYPE} from '../../constant';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";

const SourceInfluxDBStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
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
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
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
              StepSchemaService.formatSchema(values)
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
        />
        <DataSourceItem dataSource={"InfluxDB"}/>
        <ProFormText
          name={InfluxDBParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.database'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={InfluxDBParams.sql}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.sql'})}
          rules={[{required: true}]}
        />
        <FieldItem/>
        <ProFormText
          name={InfluxDBParams.splitColumn}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.splitColumn'})}
        />
        <ProFormDigit
          name={InfluxDBParams.lowerBound}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.lowerBound'})}
          colProps={{span: 8}}
          min={0}
        />
        <ProFormDigit
          name={InfluxDBParams.upperBound}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.upperBound'})}
          colProps={{span: 8}}
          min={0}
        />
        <ProFormDigit
          name={InfluxDBParams.partitionNum}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.partitionNum'})}
          colProps={{span: 8}}
          min={1}
        />
        <ProFormSelect
          name={InfluxDBParams.epoch}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.epoch'})}
          allowClear={false}
          initialValue={"n"}
          options={["H", "m", "s", "MS", "u", "n"]}
        />
        <ProFormDigit
          name={InfluxDBParams.queryTimeoutSec}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.queryTimeoutSec'})}
          fieldProps={{
            min: 1,
            step: 1
          }}
        />
        <ProFormDigit
          name={InfluxDBParams.connectTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.connectTimeoutMs'})}
          fieldProps={{
            min: 1,
            step: 1000
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceInfluxDBStepForm;
