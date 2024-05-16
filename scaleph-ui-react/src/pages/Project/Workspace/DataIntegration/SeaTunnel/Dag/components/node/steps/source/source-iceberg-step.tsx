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
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {IcebergParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";
import FieldItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourceIcebergStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatSchema(values);
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
          colProps={{span: 6}}
        />
        <ProFormSelect
          name={IcebergParams.streamScanStrategy}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.streamScanStrategy'})}
          colProps={{span: 18}}
          allowClear={false}
          initialValue={'FROM_LATEST_SNAPSHOT'}
          valueEnum={{
            FROM_LATEST_SNAPSHOT: 'FROM_LATEST_SNAPSHOT',
            FROM_EARLIEST_SNAPSHOT: 'FROM_EARLIEST_SNAPSHOT',
            FROM_SNAPSHOT_ID: 'FROM_SNAPSHOT_ID',
            FROM_SNAPSHOT_TIMESTAMP: 'FROM_SNAPSHOT_TIMESTAMP',
            TABLE_SCAN_THEN_INCREMENTAL: 'TABLE_SCAN_THEN_INCREMENTAL',
          }}
        />

        <FieldItem/>

        <ProFormSwitch
          name={'use_snapshot_id'}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.useSnapshotId'})}
        />
        <ProFormDependency name={['use_snapshot_id']}>
          {({use_snapshot_id}) => {
            if (use_snapshot_id) {
              return (
                <ProFormGroup>
                  <ProFormDigit
                    name={IcebergParams.startSnapshotId}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.iceberg.startSnapshotId',
                    })}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormDigit
                    name={IcebergParams.endSnapshotId}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.iceberg.endSnapshotId',
                    })}
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
          name={IcebergParams.startSnapshotTimestamp}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.startSnapshotTimestamp'})}
          colProps={{span: 12}}
        />
        <ProFormDigit
          name={IcebergParams.useSnapshotTimestamp}
          label={intl.formatMessage({id: 'pages.project.di.step.iceberg.useSnapshotTimestamp'})}
          colProps={{span: 12}}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceIcebergStepForm;
