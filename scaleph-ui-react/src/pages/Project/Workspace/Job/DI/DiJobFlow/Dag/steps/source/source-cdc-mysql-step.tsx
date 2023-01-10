import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {CDCMySQLParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJob.service';
import {Form, message, Modal} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import {useEffect} from 'react';
import {InfoCircleOutlined} from "@ant-design/icons";
import {StepSchemaService} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/helper";

const SourceCDCMySQLStepForm: React.FC<ModalFormProps<{
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
          StepSchemaService.formatDebeziumProperties(values)
          map.set(STEP_ATTR_TYPE.stepAttrs, values);
          WsDiJobService.saveStepAttr(map).then((resp) => {
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
        <ProFormText
          name={CDCMySQLParams.hostname}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.hostname'})}
          rules={[{required: true}]}
          colProps={{span: 16}}
        />
        <ProFormDigit
          name={CDCMySQLParams.port}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.port'})}
          rules={[{required: true}]}
          colProps={{span: 8}}
          initialValue={3306}
          fieldProps={{
            min: 0,
            max: 65535
          }}
        />
        <ProFormText
          name={CDCMySQLParams.username}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.username'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormText
          name={CDCMySQLParams.password}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.password'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormText
          name={CDCMySQLParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.database'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormText
          name={CDCMySQLParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.table'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormSelect
          name={"startupMode"}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.startupMode'})}
          allowClear={false}
          initialValue={'initial'}
          options={['initial', 'earliest', 'latest', 'specific', 'timestamp']}
        />
        <ProFormDependency name={["startupMode"]}>
          {({startupMode}) => {
            if (startupMode == 'timestamp') {
              return (
                <ProFormGroup>
                  <ProFormDigit
                    name={CDCMySQLParams.startupTimestamp}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.startupTimestamp'})}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            } else if (startupMode == 'specific') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={CDCMySQLParams.startupSpecificOffsetFile}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.startupSpecificOffsetFile'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={CDCMySQLParams.startupSpecificOffsetPos}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.startupSpecificOffsetPos'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormSelect
          name={"stopMode"}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.stopMode'})}
          allowClear={false}
          initialValue={'never'}
          options={['never', 'latest', 'specific', 'timestamp']}
        />
        <ProFormDependency name={["stopMode"]}>
          {({stopMode}) => {
            if (stopMode == 'timestamp') {
              return (
                <ProFormGroup>
                  <ProFormDigit
                    name={CDCMySQLParams.stopTimestamp}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.stopTimestamp'})}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            } else if (stopMode == 'specific') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={CDCMySQLParams.stopSpecificOffsetFile}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.stopSpecificOffsetFile'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={CDCMySQLParams.stopSpecificOffsetPos}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.stopSpecificOffsetPos'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormDigit
          name={CDCMySQLParams.snapshotSplitSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.snapshotSplitSize'})}
          colProps={{span: 8}}
          initialValue={8096}
          fieldProps={{
            step: 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={CDCMySQLParams.snapshotFetchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.snapshotFetchSize'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={CDCMySQLParams.incrementalParallelism}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.incrementalParallelism'})}
          colProps={{span: 8}}
          initialValue={1}
          fieldProps={{
            min: 0
          }}
        />
        <ProFormText
          name={CDCMySQLParams.serverId}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.serverId'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={CDCMySQLParams.serverTimeZone}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.serverTimeZone'})}
          colProps={{span: 12}}
          initialValue={"UTC"}
        />
        <ProFormDigit
          name={CDCMySQLParams.connectionPoolSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.connectionPoolSize'})}
          colProps={{span: 8}}
          initialValue={20}
          fieldProps={{
            min: 1
          }}
        />
        <ProFormText
          name={CDCMySQLParams.connectTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.connectTimeout'})}
          colProps={{span: 8}}
          initialValue={"30s"}
        />
        <ProFormDigit
          name={CDCMySQLParams.connectMaxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.connectMaxRetries'})}
          colProps={{span: 8}}
          initialValue={3}
          fieldProps={{
            min: 0
          }}
        />

        <ProFormDigit
          name={CDCMySQLParams.chunkKeyEvenDistributionFactorLowerBound}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.chunkKeyEvenDistributionFactorLowerBound'})}
          colProps={{span: 12}}
          initialValue={0.05}
          fieldProps={{
            min: 0
          }}
        />
        <ProFormDigit
          name={CDCMySQLParams.chunkKeyEvenDistributionFactorUpperBound}
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.chunkKeyEvenDistributionFactorUpperBound'})}
          colProps={{span: 12}}
          initialValue={1000}
          fieldProps={{
            min: 0
          }}
        />


        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.debeziums'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.cdcmysql.debeziums.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={CDCMySQLParams.debeziumProperties}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.cdcmysql.debeziums.list'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={CDCMySQLParams.debeziumProperty}
                label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.debeziums.property'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.debeziums.property.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={CDCMySQLParams.debeziumValue}
                label={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.debeziums.value'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.cdcmysql.debeziums.value.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Modal>
  );
};

export default SourceCDCMySQLStepForm;
