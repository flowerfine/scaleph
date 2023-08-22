import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {ClickHouseParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from '@ant-design/icons';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import {StepSchemaService} from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper';

const SinkClickHouseStepForm: React.FC<
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
              map.set(STEP_ATTR_TYPE.jobId, jobInfo.id + '');
              map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
              map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              StepSchemaService.formatClickHouseConf(values);
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
        <DataSourceItem dataSource={'ClickHouse'}/>
        <ProFormText
          name={STEP_ATTR_TYPE.table}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.table'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={STEP_ATTR_TYPE.bulkSize}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.bulkSize'})}
          initialValue={20000}
          fieldProps={{
            min: 1,
            step: 1000,
          }}
        />
        <ProFormSwitch
          name={'split_mode'}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.splitMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.clickhosue.splitMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormDependency name={['split_mode']}>
          {({split_mode}) => {
            if (split_mode) {
              return (
                <ProFormText
                  name={ClickHouseParams.shardingKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.shardingKey'})}
                  rules={[{required: true}]}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSwitch
          name={'support_upsert'}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.supportUpsert'})}
        />
        <ProFormDependency name={['support_upsert']}>
          {({support_upsert}) => {
            if (support_upsert) {
              return (
                <ProFormText
                  name={ClickHouseParams.primaryKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.primaryKey'})}
                  rules={[{required: true}]}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSwitch
          name={ClickHouseParams.allowExperimentalLightweightDelete}
          label={intl.formatMessage({
            id: 'pages.project.di.step.clickhosue.allowExperimentalLightweightDelete',
          })}
        />
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf'})}
          tooltip={{
            title: intl.formatMessage({
              id: 'pages.project.di.step.clickhosue.clickhouseConf.tooltip',
            }),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={ClickHouseParams.clickhouseConfArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.clickhosue.clickhouseConf.list',
              }),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={ClickHouseParams.key}
                label={intl.formatMessage({
                  id: 'pages.project.di.step.clickhosue.clickhouseConf.key',
                })}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.clickhosue.clickhouseConf.key.placeholder',
                })}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={ClickHouseParams.value}
                label={intl.formatMessage({
                  id: 'pages.project.di.step.clickhosue.clickhouseConf.value',
                })}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.clickhosue.clickhouseConf.value.placeholder',
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

export default SinkClickHouseStepForm;
