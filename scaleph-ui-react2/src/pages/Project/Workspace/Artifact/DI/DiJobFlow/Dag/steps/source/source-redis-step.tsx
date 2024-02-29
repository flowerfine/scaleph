import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {RedisParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {useEffect} from 'react';
import {ProForm, ProFormDependency, ProFormGroup, ProFormSelect, ProFormText,} from '@ant-design/pro-components';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";

const SourceRedisStepForm: React.FC<ModalFormProps<{
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
        <DataSourceItem dataSource={"Redis"}/>
        <ProFormText
          name={RedisParams.keys}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.keys'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"data_type"}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.dataType'})}
          rules={[{required: true}]}
          valueEnum={{
            key: 'key',
            hash: 'hash',
            listDataSetType: 'list',
            set: 'set',
            zset: 'zset',
          }}
        />
        <ProFormDependency name={['data_type']}>
          {({data_type}) => {
            if (data_type == 'hash') {
              return (
                <ProFormSelect
                  name={RedisParams.hashKeyParseMode}
                  label={intl.formatMessage({id: 'pages.project.di.step.redis.hashKeyParseMode'})}
                  initialValue={"all"}
                  options={["all", "kv"]}
                  allowClear={false}
                />
              );
            }
            return <></>;
          }}
        </ProFormDependency>
        <ProFormSelect
          name={'format'}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.format'})}
          allowClear={false}
          initialValue={'json'}
          valueEnum={{
            json: 'json',
            text: 'text',
          }}
        />
        <ProFormDependency name={['format']}>
          {({format}) => {
            if (format == 'json') {
              return <FieldItem/>;
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </ProForm>
    </Drawer>
  );
};

export default SourceRedisStepForm;
