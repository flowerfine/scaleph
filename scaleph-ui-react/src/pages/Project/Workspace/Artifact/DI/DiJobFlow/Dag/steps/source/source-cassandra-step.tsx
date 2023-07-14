import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {CassandraParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {ProForm, ProFormSelect, ProFormText, ProFormTextArea} from '@ant-design/pro-components';
import {useEffect} from 'react';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';

const SourceCassandraStepForm: React.FC<
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
        <ProFormTextArea
          name={CassandraParams.cql}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.cql'})}
          rules={[{required: true}]}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceCassandraStepForm;
