import {ModalFormProps} from '@/app.d';
import {NsGraph} from '@antv/xflow';
import {getIntl, getLocale} from 'umi';
import {WsDiJob} from '@/services/project/typings';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {DorisParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import {InfoCircleOutlined} from '@ant-design/icons';
import {StepSchemaService} from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper';

const SinkDorisStepForm: React.FC<
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
              StepSchemaService.formatDorisConfig(values);
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
        <DataSourceItem dataSource={'Doris'}/>
        <ProFormText
          name={DorisParams.tableIdentifier}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.tableIdentifier'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={DorisParams.sinkLabelPrefix}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkLabelPrefix'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={DorisParams.sinkEnable2PC}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkEnable2PC'})}
          initialValue={true}
        />
        <ProFormSwitch
          name={DorisParams.sinkEnableDelete}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkEnableDelete'})}
          initialValue={false}
        />
        <ProFormDigit
          name={DorisParams.sinkCheckInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkCheckInterval'})}
          colProps={{span: 12}}
          initialValue={10000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkMaxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkMaxRetries'})}
          colProps={{span: 12}}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkBufferSize'})}
          colProps={{span: 12}}
          initialValue={256 * 1024}
          fieldProps={{
            step: 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkBufferCount}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkBufferCount'})}
          colProps={{span: 12}}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig'})}
        >
          <ProFormList
            name={DorisParams.dorisConfigArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.doris.dorisConfig.list',
              }),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={DorisParams.dorisConfigProperty}
                label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig.key'})}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.doris.dorisConfig.key.placeholder',
                })}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={DorisParams.dorisConfigValue}
                label={intl.formatMessage({
                  id: 'pages.project.di.step.doris.dorisConfig.value',
                })}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.doris.dorisConfig.value.placeholder',
                })}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
};

export default SinkDorisStepForm;
