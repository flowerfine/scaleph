import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {InfluxDBParams, STEP_ATTR_TYPE} from '../../constant';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import {StepSchemaService} from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper';

const SinkInfluxDBStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({data, visible, onCancel, onOK}) => {
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
      bodyStyle={{overflowY: 'scroll'}}
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
              StepSchemaService.formatKeyTags(values);
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
        <DataSourceItem dataSource={'InfluxDB'}/>
        <ProFormText
          name={InfluxDBParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={InfluxDBParams.measurement}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.measurement'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={InfluxDBParams.keyKime}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.keyKime'})}
        />
        <ProFormSelect
          name={InfluxDBParams.keyTagArray}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.keyTags'})}
          fieldProps={{
            mode: 'tags',
          }}
        />
        <ProFormDigit
          name={InfluxDBParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.batchSize'})}
          colProps={{span: 12}}
          initialValue={1024}
          fieldProps={{
            min: 1,
            step: 1000,
          }}
        />
        <ProFormDigit
          name={InfluxDBParams.batchIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.batchIntervalMs'})}
          colProps={{span: 12}}
          initialValue={1000}
          fieldProps={{
            min: 1,
            step: 1000,
          }}
        />
        <ProFormDigit
          name={InfluxDBParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.maxRetries'})}
          colProps={{span: 12}}
          min={1}
        />
        <ProFormDigit
          name={InfluxDBParams.retryBackoffMultiplierMs}
          label={intl.formatMessage({
            id: 'pages.project.di.step.influxdb.retryBackoffMultiplierMs',
          })}
          colProps={{span: 12}}
          fieldProps={{
            min: 1,
            step: 1000,
          }}
        />
        <ProFormDigit
          name={InfluxDBParams.connectTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.influxdb.connectTimeoutMs'})}
          fieldProps={{
            min: 1,
            step: 1000,
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SinkInfluxDBStepForm;
