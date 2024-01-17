import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {HbaseParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const SinkHbaseStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <Drawer
        open={visible}
        title={data.data.label}
        width={780}
        bodyStyle={{overflowY: 'scroll'}}
        destroyOnClose={true}
        onClose={onCancel}
        extra={
          <Button
            type="primary"
            onClick={() => {
              form.validateFields().then((values) => {
                StepSchemaService.formatRowKeyColumn(values)
                StepSchemaService.formatHbaseExtraConfig(values)
                if (onOK) {
                  onOK(values);
                }
              });
            }}
          >
            {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
          </Button>
        }
      >
        <ProForm form={form} initialValues={data.data.attrs} grid={true} submitter={false}>
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
    </XFlow>
  );
};

export default SinkHbaseStepForm;
