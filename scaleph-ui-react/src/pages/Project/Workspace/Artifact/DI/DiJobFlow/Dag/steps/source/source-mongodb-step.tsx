import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {MongoDBParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {useEffect} from 'react';
import {ProForm, ProFormDigit, ProFormSwitch, ProFormText, ProFormTextArea} from '@ant-design/pro-components';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";

const SourceMongoDBStepForm: React.FC<ModalFormProps<{
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
        <DataSourceItem dataSource={"MongoDB"}/>
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

        <ProFormText
          name={MongoDBParams.matchProjection}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.matchProjection'})}
        />
        <ProFormTextArea
          name={MongoDBParams.matchQuery}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.matchQuery'})}
        />
        <FieldItem/>
        <ProFormText
          name={MongoDBParams.partitionSplitKey}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.partitionSplitKey'})}
          colProps={{span: 12}}
          initialValue={"_id"}
        />
        <ProFormDigit
          name={MongoDBParams.partitionSplitSize}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.partitionSplitSize'})}
          colProps={{span: 12}}
          initialValue={1024 * 1024 * 64}
          fieldProps={{
            step: 1024 * 1024,
            min: 1
          }}
        />
        <ProFormSwitch
          name={MongoDBParams.cursorNoTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.cursorNoTimeout'})}
          colProps={{span: 8}}
          initialValue={true}
        />
        <ProFormDigit
          name={MongoDBParams.fetchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.fetchSize'})}
          colProps={{span: 8}}
          initialValue={1024 * 2}
          fieldProps={{
            step: 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={MongoDBParams.maxTimeMin}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.maxTimeMin'})}
          colProps={{span: 8}}
          initialValue={600}
          fieldProps={{
            step: 60,
            min: 1
          }}
        />
        <ProFormSwitch
          name={MongoDBParams.flatSyncString}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.flatSyncString'})}
          initialValue={true}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceMongoDBStepForm;
