import {ModalFormProps} from '@/app.d';
import {NsGraph} from '@antv/xflow';
import {getIntl, getLocale} from 'umi';
import {WsDiJob} from '@/services/project/typings';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {StarRocksParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {ProForm, ProFormDigit, ProFormText,} from '@ant-design/pro-components';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import {StepSchemaService} from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper';
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";

const SourceStarRocksStepForm: React.FC<
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
          colProps={{span: 24}}
        />
        <DataSourceItem dataSource={'StarRocks'}/>
        <ProFormText
          name={StarRocksParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={StarRocksParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.table'})}
          rules={[{required: true}]}
        />
        <FieldItem/>
        <ProFormText
          name={StarRocksParams.scanFilter}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanFilter'})}
        />
        <ProFormDigit
          name={StarRocksParams.scanConnectTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanConnectTimeoutMs'})}
          colProps={{span: 8}}
          initialValue={30000}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanQueryTimeoutSec}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanQueryTimeoutSec'})}
          colProps={{span: 8}}
          initialValue={3600}
          fieldProps={{
            step: 60,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanKeepAliveMin}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanKeepAliveMin'})}
          colProps={{span: 8}}
          initialValue={10}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanBatchRows}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanBatchRows'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanMemLimit}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanMemLimit'})}
          colProps={{span: 8}}
          initialValue={1024 * 1024 * 1024 * 2}
          fieldProps={{
            step: 1024 * 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={StarRocksParams.requestTabletSize}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.requestTabletSize'})}
          colProps={{span: 8}}
          initialValue={10}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetries'})}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceStarRocksStepForm;
