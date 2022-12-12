import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {DiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {InfluxDBParams, SchemaParams, STEP_ATTR_TYPE} from '../../constant';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";
import {InfoCircleOutlined} from "@ant-design/icons";
import {StepSchemaService} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/helper";

const SourceInfluxDBStepForm: React.FC<ModalFormProps<{
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

  return (
    <Modal
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
          StepSchemaService.formatFields(values)
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
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.schema'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.schema.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={InfluxDBParams.fieldArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.schema.fields'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={SchemaParams.field}
                label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.field'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={SchemaParams.type}
                label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.type'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
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
    </Modal>
  );
};

export default SourceInfluxDBStepForm;
