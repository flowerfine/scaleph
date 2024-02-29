import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {ProForm, ProFormDigit, ProFormText} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {AmazonDynamoDBParams, STEP_ATTR_TYPE} from '../../constant';

const SinkAmazonDynamodbStepForm: React.FC<
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
        <ProFormText
          name={AmazonDynamoDBParams.url}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.url'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.region}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.region'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.accessKeyId}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.accessKeyId'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.secretAccessKey}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.secretAccessKey'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.table'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={AmazonDynamoDBParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.batchSize'})}
          colProps={{span: 12}}
          initialValue={25}
          fieldProps={{
            step: 100,
            min: 0,
          }}
        />
        <ProFormDigit
          name={AmazonDynamoDBParams.batchIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.batchIntervalMs'})}
          colProps={{span: 12}}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SinkAmazonDynamodbStepForm;
