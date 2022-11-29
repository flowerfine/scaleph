import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {RedisParams, STEP_ATTR_TYPE} from '../../constant';
import {JobService} from '@/services/project/job.service';
import {Form, message, Modal} from 'antd';
import {DiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {useEffect} from 'react';
import {ProForm, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";

const SinkRedisStepForm: React.FC<ModalFormProps<{
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
        <DataSourceItem dataSource={"Redis"}/>
        <ProFormText
          name={RedisParams.key}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.key'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={RedisParams.dataType}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.dataType'})}
          rules={[{required: true}]}
          valueEnum={{
            key: 'key',
            hash: 'hash',
            list: 'list',
            set: 'set',
            zset: 'zset',
          }}
        />
        <ProFormSelect
          name={RedisParams.format}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.format'})}
          allowClear={false}
          initialValue={'json'}
          valueEnum={{
            json: 'json',
            text: 'text',
          }}
        />
      </ProForm>
    </Modal>
  );
};

export default SinkRedisStepForm;
