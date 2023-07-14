import {ModalFormProps} from '@/app.d';
import {NsGraph} from '@antv/xflow';
import {getIntl, getLocale} from 'umi';
import {WsDiJob} from '@/services/project/typings';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {HbaseParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper";
import {InfoCircleOutlined} from "@ant-design/icons";

const SinkHbaseStepForm: React.FC<
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
              StepSchemaService.formatRowKeyColumn(values)
              StepSchemaService.formatHbaseExtraConfig(values)
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
        <ProFormText
          name={HbaseParams.zookeeperQuorum}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.zookeeperQuorum'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={HbaseParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.table'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={HbaseParams.familyName}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.familyName'})}
          rules={[{required: true}]}
        />
        <ProFormList
          name={HbaseParams.rowkeyColumnArray}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.rowkeyColumnArray'})}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.hbase.rowkeyColumn'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText
              name={HbaseParams.rowkeyColumnValue}
              colProps={{span: 18, offset: 4}}
            />
          </ProFormGroup>
        </ProFormList>
        <ProFormText
          name={HbaseParams.rowkeyDelimiter}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.rowkeyDelimiter'})}
        />
        <ProFormText
          name={HbaseParams.versionColumn}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.versionColumn'})}
        />
        <ProFormSelect
          name={HbaseParams.nullMode}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.nullMode'})}
          initialValue={"skip"}
          options={["skip", "empty"]}
        />
        <ProFormSwitch
          name={HbaseParams.walWrite}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.walWrite'})}
        />
        <ProFormDigit
          name={HbaseParams.writeBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.writeBufferSize'})}
          initialValue={1024 * 1024 * 8}
          fieldProps={{
            min: 1,
            step: 1024 * 1024
          }}
        />
        <ProFormSelect
          name={HbaseParams.encoding}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.encoding'})}
          initialValue={"utf8"}
          options={["utf8", "gbk"]}
        />
        <ProFormList
          name={HbaseParams.hbaseExtraConfigMap}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.hbaseExtraConfig'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.hbase.hbaseExtraConfig.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.hbase.hbaseExtraConfigMap'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText
              name={HbaseParams.hbaseExtraConfigKey}
              label={intl.formatMessage({id: 'pages.project.di.step.hbase.hbaseExtraConfigKey'})}
              colProps={{span: 10, offset: 1}}
            />
            <ProFormText
              name={HbaseParams.hbaseExtraConfigValue}
              label={intl.formatMessage({id: 'pages.project.di.step.hbase.hbaseExtraConfigValue'})}
              colProps={{span: 10, offset: 1}}
            />
          </ProFormGroup>
        </ProFormList>
      </ProForm>
    </Drawer>
  );
};

export default SinkHbaseStepForm;
