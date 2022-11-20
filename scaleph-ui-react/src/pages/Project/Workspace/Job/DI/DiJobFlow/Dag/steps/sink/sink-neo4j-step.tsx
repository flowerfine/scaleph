import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {DiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {Neo4jParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import { StepSchemaService } from '../schema';

const SinkNeo4jStepForm: React.FC<ModalFormProps<{
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
          StepSchemaService.formatPositionMapping(values)
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
        />
        <ProFormText
          name={Neo4jParams.uri}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.uri'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={Neo4jParams.username}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.username'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={Neo4jParams.password}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.password'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={Neo4jParams.bearerToken}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.bearerToken'})}
        />
        <ProFormText
          name={Neo4jParams.kerberosTicket}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.kerberosTicket'})}
        />
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
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.queryParamPosition'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.neo4j.queryParamPosition.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={Neo4jParams.queryParamPositionArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.neo4j.queryParamPosition.list'}),
              type: 'text',
            }}>
            <ProFormGroup>
              <ProFormText
                name={Neo4jParams.field}
                label={intl.formatMessage({id: 'pages.project.di.step.neo4j.field'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormDigit
                name={Neo4jParams.position}
                label={intl.formatMessage({id: 'pages.project.di.step.neo4j.position'})}
                colProps={{span: 10, offset: 1}}
                fieldProps={{
                  min: 0
                }}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
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

export default SinkNeo4jStepForm;
