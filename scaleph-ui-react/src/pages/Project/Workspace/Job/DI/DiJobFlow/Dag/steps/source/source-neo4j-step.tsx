import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJob.service';
import {WsDiJob} from '@/services/project/typings';
import {ProForm, ProFormDigit, ProFormText, ProFormTextArea} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {Neo4jParams, STEP_ATTR_TYPE} from '../../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/fields";

const SourceNeo4jStepForm: React.FC<ModalFormProps<{
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
          StepSchemaService.formatSchema(values)
          map.set(STEP_ATTR_TYPE.stepAttrs, values);
          WsDiJobService.saveStepAttr(map).then((resp) => {
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
        <DataSourceItem dataSource={"Neo4j"}/>
        <ProFormText
          name={Neo4jParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.database'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={Neo4jParams.query}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.query'})}
          rules={[{required: true}]}
        />
        <FieldItem/>
        <ProFormDigit
          name={Neo4jParams.maxTransactionRetryTime}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.maxTransactionRetryTime'})}
          initialValue={30}
          fieldProps={{
            step: 5,
            min: 0
          }}
        />
        <ProFormDigit
          name={Neo4jParams.maxConnectionTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.maxConnectionTimeout'})}
          initialValue={30}
          fieldProps={{
            step: 5,
            min: 0
          }}
        />
      </ProForm>
    </Modal>
  );
};

export default SourceNeo4jStepForm;
