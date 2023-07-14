import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {CassandraParams, STEP_ATTR_TYPE} from '../../constant';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import {StepSchemaService} from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper';

const SinkCassandraStepForm: React.FC<
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
              StepSchemaService.formatCassandraFields(values);
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
        <DataSourceItem dataSource={'Cassandra'}/>
        <ProFormSelect
          name={CassandraParams.consistencyLevel}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.consistencyLevel'})}
          allowClear={false}
          initialValue={'LOCAL_ONE'}
          options={[
            'ANY',
            'ONE',
            'TWO',
            'THREE',
            'QUORUM',
            'ALL',
            'LOCAL_ONE',
            'LOCAL_QUORUM',
            'EACH_QUORUM',
            'SERIAL',
            'LOCAL_SERIAL',
          ]}
        />
        <ProFormText
          name={CassandraParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.table'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={CassandraParams.batchType}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.batchType'})}
          allowClear={false}
          initialValue={'UNLOGGED'}
          options={['LOGGED', 'UNLOGGED', 'COUNTER']}
        />
        <ProFormDigit
          name={CassandraParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.batchSize'})}
          initialValue={5000}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormSwitch
          name={CassandraParams.asyncWrite}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.asyncWrite'})}
          initialValue={true}
        />

        <ProFormGroup label={intl.formatMessage({id: 'pages.project.di.step.cassandra.fields'})}>
          <ProFormList
            name={CassandraParams.fieldArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.cassandra.fields.field',
              }),
              type: 'text',
            }}
          >
            <ProFormText name={CassandraParams.field}/>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
};

export default SinkCassandraStepForm;
