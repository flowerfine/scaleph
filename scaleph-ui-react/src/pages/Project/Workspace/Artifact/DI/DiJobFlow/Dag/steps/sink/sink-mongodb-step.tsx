import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {MongoDBParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {useEffect} from 'react';
import {ProForm, ProFormDigit, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper";

const SinkMongoDBStepForm: React.FC<
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
        <DataSourceItem dataSource={'MongoDB'}/>
        <ProFormText
          name={MongoDBParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={MongoDBParams.collection}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.collection'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={MongoDBParams.bufferFlushMaxRows}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.bufferFlushMaxRows'})}
          initialValue={1000}
        />
        <ProFormDigit
          name={MongoDBParams.bufferFlushInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.bufferFlushInterval'})}
          initialValue={30000}
        />
        <ProFormDigit
          name={MongoDBParams.retryMax}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.retryMax'})}
          initialValue={3}
        />
        <ProFormDigit
          name={MongoDBParams.retryInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.retryInterval'})}
          initialValue={1000}
        />
        <ProFormSwitch
          name={MongoDBParams.upsertEnable}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.upsertEnable'})}
          initialValue={false}
        />
        <ProFormText
          name={MongoDBParams.primaryKey}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.primaryKey'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.mongodb.primaryKey.placeholder'})}
        />
        <ProFormSwitch
          name={MongoDBParams.transaction}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.transaction'})}
          initialValue={false}
        />
        <FieldItem/>
      </ProForm>
    </Drawer>
  );
};

export default SinkMongoDBStepForm;
