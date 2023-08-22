import {ModalFormProps} from '@/app.d';
import {NsGraph} from '@antv/xflow';
import {getIntl, getLocale} from 'umi';
import {WsDiJob} from '@/services/project/typings';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {StarRocksParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import {InfoCircleOutlined} from '@ant-design/icons';
import {StepSchemaService} from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper';

const SinkStarRocksStepForm: React.FC<
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
              StepSchemaService.formatStarRocksConfig(values);
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
          name={StarRocksParams.baseUrl}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.base-url'})}
          rules={[{required: true}]}
        />
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
        <ProFormText
          name={StarRocksParams.labelPrefix}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.labelPrefix'})}
        />
        <ProFormDigit
          name={StarRocksParams.batchMaxRows}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchMaxRows'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.batchMaxBytes}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchMaxBytes'})}
          colProps={{span: 8}}
          initialValue={5 * 1024 * 1024}
          fieldProps={{
            step: 1024 * 1024,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.batchIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchIntervalMs'})}
          colProps={{span: 8}}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetries'})}
          colProps={{span: 8}}
          initialValue={1}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.retryBackoffMultiplierMs}
          label={intl.formatMessage({
            id: 'pages.project.di.step.starrocks.retryBackoffMultiplierMs',
          })}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.maxRetryBackoffMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetryBackoffMs'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormSwitch
          name={StarRocksParams.enableUpsertDelete}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.enableUpsertDelete'})}
        />
        <ProFormTextArea
          name={StarRocksParams.saveModeCreateTemplate}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.saveModeCreateTemplate'})}
        />

        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfig'})}
          tooltip={{
            title: intl.formatMessage({
              id: 'pages.project.di.step.starrocks.starrocksConfig.tooltip',
            }),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={StarRocksParams.starrocksConfigMap}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.starrocks.starrocksConfig.list',
              }),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={StarRocksParams.starrocksConfigKey}
                label={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigKey'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigKey.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={StarRocksParams.starrocksConfigValue}
                label={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigValue'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigValue.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
};

export default SinkStarRocksStepForm;
