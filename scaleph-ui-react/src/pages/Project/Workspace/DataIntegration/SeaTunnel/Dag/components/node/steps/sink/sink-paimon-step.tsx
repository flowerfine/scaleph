import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormGroup, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {PaimonParams, STEP_ATTR_TYPE} from '../constant';
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";
import {StepSchemaService} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/helper";
import SaveModeItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/saveMode";

const SinkPaimonStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonConfig(values, PaimonParams.paimonHadoopConf, PaimonParams.paimonHadoopConf)
            StepSchemaService.formatCommonConfig(values, PaimonParams.paimonTableWriteProps, PaimonParams.paimonTableWriteProps)
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
          name={PaimonParams.warehouse}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.warehouse'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PaimonParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PaimonParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.table'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PaimonParams.hdfsSitePath}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.hdfsSitePath'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.paimon.hdfsSitePath.placeholder'})}
        />
        <ProFormText
          name={PaimonParams.paimonHadoopConfPath}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.paimonHadoopConfPath'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.paimon.paimonHadoopConfPath.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.paimon.paimonHadoopConf'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.paimon.paimonHadoopConf.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={PaimonParams.paimonHadoopConf}/>
        </ProFormGroup>
        <ProFormText
          name={PaimonParams.paimonTablePrimaryKeys}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.paimonTablePrimaryKeys'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.paimon.paimonTablePrimaryKeys.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormText
          name={PaimonParams.paimonTablePartitionKeys}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.paimonTablePartitionKeys'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.paimon.paimonTablePartitionKeys.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.paimon.paimonTableWriteProps'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.paimon.paimonTableWriteProps.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={PaimonParams.paimonTableWriteProps}/>
        </ProFormGroup>

        <SaveModeItem/>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkPaimonStepForm;
