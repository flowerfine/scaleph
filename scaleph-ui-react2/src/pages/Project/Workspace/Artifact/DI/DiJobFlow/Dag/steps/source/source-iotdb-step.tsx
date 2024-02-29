import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {IoTDBParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {useEffect} from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";

const SourceIoTDBStepForm: React.FC<ModalFormProps<{
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
              StepSchemaService.formatSchema(values);
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
        <DataSourceItem dataSource={'IoTDB'}/>
        <ProFormTextArea
          name={IoTDBParams.sql}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.sql'})}
          rules={[{required: true}]}
        />
        <FieldItem/>
        <ProFormDigit
          name={IoTDBParams.fetchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.fetchSize'})}
          min={1}
        />
        <ProFormDigit
          name={IoTDBParams.thriftDefaultBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.thriftDefaultBufferSize'})}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormSwitch
          name={IoTDBParams.enableCacheLeader}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.enableCacheLeader'})}
        />
        <ProFormSelect
          name={IoTDBParams.version}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.version'})}
          valueEnum={{V_0_12: 'V_0_12', V_0_13: 'V_0_13'}}
        />
        <ProFormDigit
          name={IoTDBParams.numPartitions}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.numPartitions'})}
          colProps={{span: 8}}
          fieldProps={{
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.lowerBound}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.lowerBound'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 100000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.upperBound}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.upperBound'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 100000,
            min: 1,
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceIoTDBStepForm;
