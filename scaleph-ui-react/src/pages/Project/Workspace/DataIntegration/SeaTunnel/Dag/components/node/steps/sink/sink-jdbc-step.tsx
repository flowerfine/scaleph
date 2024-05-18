import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {DICT_TYPE} from "@/constants/dictType";
import {JdbcParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import {DsInfoParam} from "@/services/datasource/typings";
import {DsInfoService} from "@/services/datasource/info.service";
import {DictDataService} from "@/services/admin/dictData.service";
import SaveModeItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/saveMode";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SinkJdbcStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <DrawerForm
        title={data.data.label}
        form={form}
        initialValues={data.data.attrs}
        open={visible}
        onOpenChange={onVisibleChange}
        grid={true}
        width={780}
        drawerProps={{
          styles: {body: {overflowY: 'scroll'}},
          closeIcon: null,
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            StepSchemaService.formatCommonList(values, JdbcParams.primaryKeys, JdbcParams.primaryKeys);
            StepSchemaService.formatCommonConfig(values, JdbcParams.properties, JdbcParams.properties);
            onOK(values)
            return Promise.resolve(true)
          }
          return Promise.resolve(false)
        }}
      >
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
        <ProFormText
          name={JdbcParams.compatibleMode}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormDigit
          name={JdbcParams.connectionCheckTimeoutSec}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.connectionCheckTimeoutSec'})}
          fieldProps={{
            min: 0
          }}
        />
        <ProFormSwitch
          name={JdbcParams.useCopyStatement}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.useCopyStatement'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.useCopyStatement.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={false}
        />
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
            return <ProFormGroup>
              <ProFormText
                name={JdbcParams.database}
                label={intl.formatMessage({id: 'pages.project.di.step.jdbc.database'})}
                tooltip={{
                  title: intl.formatMessage({id: 'pages.project.di.step.jdbc.table.tooltip'}),
                  icon: <InfoCircleOutlined/>,
                }}
                rules={[{required: true}]}
                colProps={{span: 12}}
              />
              <ProFormText
                name={JdbcParams.table}
                label={intl.formatMessage({id: 'pages.project.di.step.jdbc.table'})}
                tooltip={{
                  title: intl.formatMessage({id: 'pages.project.di.step.jdbc.table.tooltip'}),
                  icon: <InfoCircleOutlined/>,
                }}
                rules={[{required: true}]}
                colProps={{span: 12}}
              />
            </ProFormGroup>;
          }}
        </ProFormDependency>

        <ProFormSwitch
          name={JdbcParams.enableUpsert}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.enableUpsert'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.enableUpsert.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 12}}
        />
        <ProFormSwitch
          name={"support_upsert_by_query_primary_key_exist"}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.supportUpsertByQueryPrimaryKeyExist'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.supportUpsertByQueryPrimaryKeyExist.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 12}}
        />
        <ProFormDependency name={["support_upsert_by_query_primary_key_exist"]}>
          {({support_upsert_by_query_primary_key_exist}) => {
            if (support_upsert_by_query_primary_key_exist) {
              return (
                <ProFormGroup
                  title={intl.formatMessage({id: 'pages.project.di.step.jdbc.primaryKeys'})}
                  tooltip={{
                    title: intl.formatMessage({id: 'pages.project.di.step.jdbc.primaryKeys.tooltip'}),
                    icon: <InfoCircleOutlined/>,
                  }}
                  collapsible={true}
                  defaultCollapsed={true}
                >
                  <CommonListItem data={JdbcParams.primaryKeys}/>
                </ProFormGroup>
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
          colProps={{span: 12}}
          initialValue={300}
          fieldProps={{
            min: 0,
            step: 100
          }}
        />
        <ProFormDigit
          name={JdbcParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.maxRetries'})}
          colProps={{span: 12}}
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
                    colProps={{span: 8}}
                  />
                  <ProFormDigit
                    name={JdbcParams.maxCommitAttempts}
                    label={intl.formatMessage({id: 'pages.project.di.step.jdbc.maxCommitAttempts'})}
                    colProps={{span: 8}}
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
                    colProps={{span: 8}}
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

        <ProFormSelect
          name={JdbcParams.fieldIde}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.fieldIde'})}
          options={["ORIGINAL", "UPPERCASE", "LOWERCASE"]}
        />

        <SaveModeItem/>
        <ProFormDependency name={['data_save_mode']}>
          {({data_save_mode}) => {
            if (data_save_mode == 'CUSTOM_PROCESSING') {
              return (
                <ProFormTextArea
                  name={JdbcParams.customSql}
                  label={intl.formatMessage({id: 'pages.project.di.step.jdbc.customSql'})}
                  rules={[{required: true}]}
                />
              )
            }
          }}
        </ProFormDependency>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.jdbc.properties'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.properties.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={JdbcParams.properties}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkJdbcStepForm;
