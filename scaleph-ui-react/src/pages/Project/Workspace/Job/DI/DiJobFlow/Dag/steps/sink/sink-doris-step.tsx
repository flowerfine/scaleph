import {ModalFormProps} from '@/app.d';
import {NsGraph} from '@antv/xflow';
import {getIntl, getLocale} from 'umi';
import {WsDiJob} from '@/services/project/typings';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {DorisParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJob.service';
import {ProForm, ProFormDigit, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";
import {InfoCircleOutlined} from "@ant-design/icons";
import {StepSchemaService} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/helper";

const SinkDorisStepForm: React.FC<ModalFormProps<{
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
          StepSchemaService.formatDorisSinkProperties(values)
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
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
          colProps={{span: 24}}
        />
        <DataSourceItem dataSource={"Doris"}/>
        <ProFormText
          name={DorisParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={DorisParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.table'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={DorisParams.labelPrefix}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.labelPrefix'})}
        />
        <ProFormDigit
          name={DorisParams.batchMaxRows}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.batchMaxRows'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.batchMaxBytes}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.batchMaxBytes'})}
          colProps={{span: 8}}
          initialValue={5 * 1024 * 1024}
          fieldProps={{
            step: 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.batchIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.batchIntervalMs'})}
          colProps={{span: 8}}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.maxRetries'})}
          colProps={{span: 8}}
          initialValue={1}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.retryBackoffMultiplierMs}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.retryBackoffMultiplierMs'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.maxRetryBackoffMs}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.maxRetryBackoffMs'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />

        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkProperties'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.doris.sinkProperties.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={DorisParams.sinkPropertyArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.doris.sinkProperties.list'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={DorisParams.sinkProperty}
                label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkProperties.key'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.doris.sinkProperties.key.placeholder'})}
                colProps={{span: 10, offset: 1}}
                addonBefore={"sink.properties."}
              />
              <ProFormText
                name={DorisParams.sinkPropertyValue}
                label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkProperties.value'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.doris.sinkProperties.value.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Modal>
  );
};

export default SinkDorisStepForm;
