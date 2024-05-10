import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {IcebergParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import SaveModeItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/saveMode";
import {InfoCircleOutlined} from "@ant-design/icons";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SinkIcebergStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatIcebergCatalogConfig(values);
            StepSchemaService.formatCommonConfig(values, IcebergParams.hadoopConfig, IcebergParams.hadoopConfig);
            StepSchemaService.formatCommonConfig(values, IcebergParams.icebergTableWriteProps, IcebergParams.icebergTableWriteProps);
            StepSchemaService.formatCommonConfig(values, IcebergParams.icebergTableAutoCreateProps, IcebergParams.icebergTableAutoCreateProps);
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
        <ProFormText
          name={IcebergParams.catalogName}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogName'})}
          rules={[{required: true}]}
          colProps={{span: 6}}
        />
        <ProFormText
          name={IcebergParams.namespace}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.namespace'})}
          rules={[{required: true}]}
          colProps={{span: 6}}
        />
        <ProFormText
          name={IcebergParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.table'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormSelect
          name={IcebergParams.catalogConfigType}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogConfigType'})}
          rules={[{required: true}]}
          valueEnum={{
            hive: 'hive',
            hadoop: 'hadoop',
          }}
        />
        <ProFormDependency name={['type']}>
          {({type}) => {
            if (type == 'hive') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={IcebergParams.catalogConfigUri}
                    label={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogConfigUri'})}
                    placeholder={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogConfigUri.placeholder'})}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormText
          name={IcebergParams.catalogConfigWarehouse}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogConfigWarehouse'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.iceberg.catalogConfigWarehouse.placeholder'})}
          rules={[{required: true}]}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.iceberg.hadoopConfig'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.iceberg.hadoopConfig.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={IcebergParams.hadoopConfig}/>
        </ProFormGroup>
        <ProFormText
          name={IcebergParams.icebergHadoopConfPath}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergHadoopConfPath'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergHadoopConfPath.placeholder'})}
        />
        <ProFormSwitch
          name={IcebergParams.caseSensitive}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.caseSensitive'})}
          colProps={{span: 8}}
        />
        <ProFormSwitch
          name={IcebergParams.icebergTableSchemaEvolutionEnabled}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTableSchemaEvolutionEnabled'})}
          colProps={{span: 8}}
        />
        <ProFormSwitch
          name={IcebergParams.icebergTableUpsertModeEnabled}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTableUpsertModeEnabled'})}
          colProps={{span: 8}}
        />
        <ProFormText
          name={IcebergParams.icebergTablePrimaryKeys}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTablePrimaryKeys'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTablePrimaryKeys.placeholder'})}
          colProps={{span: 8}}
        />
        <ProFormText
          name={IcebergParams.icebergTablePartitionKeys}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTablePartitionKeys'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTablePartitionKeys.placeholder'})}
          colProps={{span: 8}}
        />
        <ProFormText
          name={IcebergParams.icebergTableCommitBranch}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTableCommitBranch'})}
          colProps={{span: 8}}
        />

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTableWriteProps'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTableWriteProps.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={IcebergParams.icebergTableWriteProps}/>
        </ProFormGroup>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTableAutoCreateProps'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.iceberg.icebergTableAutoCreateProps.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={IcebergParams.icebergTableAutoCreateProps}/>
        </ProFormGroup>

        <SaveModeItem/>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkIcebergStepForm;
