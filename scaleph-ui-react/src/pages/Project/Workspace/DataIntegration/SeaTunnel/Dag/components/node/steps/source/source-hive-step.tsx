import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormGroup, ProFormText,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {HiveParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import ColumnItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/column";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";

const SourceHiveStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatColumns(values);
            StepSchemaService.formatCommonList(values, HiveParams.readPartitions, HiveParams.readPartitions);
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

        <DataSourceItem dataSource={'Hive'}/>

        <ProFormText
          name={HiveParams.tableName}
          label={intl.formatMessage({id: 'pages.project.di.step.hive.tableName'})}
          rules={[{required: true}]}
        />

        <ColumnItem/>

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.hive.readParitions'})}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={HiveParams.readPartitions}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SourceHiveStepForm;
