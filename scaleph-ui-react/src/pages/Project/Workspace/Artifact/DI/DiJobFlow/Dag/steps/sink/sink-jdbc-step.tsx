import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from '@/constants/dictType';
import {DictDataService} from '@/services/admin/dictData.service';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {JdbcParams, STEP_ATTR_TYPE} from '../../constant';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {InfoCircleOutlined} from "@ant-design/icons";
import {DsInfoParam} from "@/services/datasource/typings";
import {DsInfoService} from "@/services/datasource/info.service";
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper";

const SinkJdbcStepForm: React.FC<ModalFormProps<{
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
              StepSchemaService.formatPrimaryKeys(values);
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
        <ProFormSelect
          name={"dataSourceType"}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSourceType'})}
          colProps={{span: 6}}
          initialValue={"MySQL"}
          allowClear={false}
          request={(() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.datasourceType);
          })}
        />
        <ProFormSelect
          name={STEP_ATTR_TYPE.dataSource}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSource'})}
          rules={[{required: true}]}
          colProps={{span: 18}}
          dependencies={["dataSourceType"]}
          request={((params, props) => {
            const param: DsInfoParam = {
              name: params.keyWords,
              dsType: params.dataSourceType
            };
            return DsInfoService.list(param).then((response) => {
              return response.data.map((item) => {
                return {label: item.name, value: item.id, item: item};
              });
            });
          })}
        />
        <ProFormDigit
          name={JdbcParams.connectionCheckTimeoutSec}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.connectionCheckTimeoutSec'})}
          fieldProps={{
            min: 0
          }}
        />
        <ProFormText
          name={JdbcParams.compatibleMode}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormText
          name={JdbcParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.database'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.table.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 12}}
        />
        <ProFormText
          name={JdbcParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.table'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.table.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 12}}
        />
        <ProFormSwitch
          name={"support_upsert_by_query_primary_key_exist"}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.supportUpsert'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.supportUpsert.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormDependency name={["support_upsert_by_query_primary_key_exist"]}>
          {({support_upsert_by_query_primary_key_exist}) => {
            if (support_upsert_by_query_primary_key_exist) {
              return (
                <ProFormGroup
                  label={intl.formatMessage({id: 'pages.project.di.step.jdbc.primaryKeys'})}
                  tooltip={{
                    title: intl.formatMessage({id: 'pages.project.di.step.jdbc.primaryKeys.tooltip'}),
                    icon: <InfoCircleOutlined/>,
                  }}
                >
                  <ProFormList
                    name={JdbcParams.primaryKeyArray}
                    copyIconProps={false}
                    creatorButtonProps={{
                      creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.jdbc.primaryKeys.list'}),
                      type: 'text',
                    }}
                  >
                    <ProFormText
                      name={JdbcParams.primaryKey}
                      colProps={{span: 16, offset: 4}}
                    />
                  </ProFormList>
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormSwitch
          name={"generate_sink_sql"}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.generateSinkSql'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.generateSinkSql.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={true}
        />
        <ProFormDependency name={["generate_sink_sql"]}>
          {({generate_sink_sql}) => {
            if (!generate_sink_sql) {
              return (
                <ProFormTextArea
                  name={JdbcParams.query}
                  label={intl.formatMessage({id: 'pages.project.di.step.jdbc.query'})}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormDigit
          name={JdbcParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.batchSize'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.batch.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 8}}
          initialValue={300}
          fieldProps={{
            min: 0,
            step: 100
          }}
        />
        <ProFormDigit
          name={JdbcParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.maxRetries'})}
          colProps={{span: 8}}
          initialValue={3}
          fieldProps={{
            min: 0
          }}
        />

        <ProFormSwitch
          name={"is_exactly_once"}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.isExactlyOnce'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.isExactlyOnce.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={false}
        />
        <ProFormDependency name={["is_exactly_once"]}>
          {({is_exactly_once}) => {
            if (is_exactly_once) {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={JdbcParams.xaDataSourceClassName}
                    label={intl.formatMessage({id: 'pages.project.di.step.jdbc.xaDataSourceClassName'})}
                    rules={[{required: true}]}
                  />
                  <ProFormSwitch
                    name={JdbcParams.autoCommit}
                    label={intl.formatMessage({id: 'pages.project.di.step.jdbc.autoCommit'})}
                  />
                  <ProFormDigit
                    name={JdbcParams.maxCommitAttempts}
                    label={intl.formatMessage({id: 'pages.project.di.step.jdbc.maxCommitAttempts'})}
                    colProps={{span: 12}}
                    initialValue={3}
                    fieldProps={{
                      min: 0
                    }}
                  />
                  <ProFormDigit
                    name={JdbcParams.transactionTimeoutSec}
                    label={intl.formatMessage({id: 'pages.project.di.step.jdbc.transactionTimeoutSec'})}
                    tooltip={{
                      title: intl.formatMessage({id: 'pages.project.di.step.jdbc.transactionTimeoutSec.tooltip'}),
                      icon: <InfoCircleOutlined/>,
                    }}
                    colProps={{span: 12}}
                    initialValue={-1}
                    fieldProps={{
                      min: -1
                    }}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </ProForm>
    </Drawer>
  );
};

export default SinkJdbcStepForm;
