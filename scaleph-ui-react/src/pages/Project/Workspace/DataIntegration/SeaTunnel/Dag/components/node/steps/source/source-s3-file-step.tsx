import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {BaseFileParams, S3FileParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import SchemaItem from "../schema";
import DataSourceItem from "../dataSource";

const SourceS3FileStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatSchema(values);
            StepSchemaService.formatHadoopS3Properties(values)
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
        <DataSourceItem dataSource={"S3"}/>
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={S3FileParams.hadoopS3Properties}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.list'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={S3FileParams.key}
                label={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.key'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.key.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={S3FileParams.value}
                label={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.value'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.value.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
        <ProFormText
          name={BaseFileParams.path}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={BaseFileParams.readColumns}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.readColumns'})}
        />
        <ProFormText
          name={BaseFileParams.fileFilterPattern}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFilterPattern'})}
        />
        <SchemaItem/>
        <ProFormSwitch
          name={BaseFileParams.parsePartitionFromPath}
          label={intl.formatMessage({
            id: 'pages.project.di.step.baseFile.parsePartitionFromPath',
          })}
          initialValue={true}
        />
        <ProFormSelect
          name={BaseFileParams.dateFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.dateFormat'})}
          initialValue={'yyyy-MM-dd'}
          options={['yyyy-MM-dd', 'yyyy.MM.dd', 'yyyy/MM/dd']}
        />
        <ProFormSelect
          name={BaseFileParams.timeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.timeFormat'})}
          initialValue={'HH:mm:ss'}
          options={['HH:mm:ss', 'HH:mm:ss.SSS']}
        />
        <ProFormSelect
          name={BaseFileParams.datetimeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.datetimeFormat'})}
          initialValue={'yyyy-MM-dd HH:mm:ss'}
          options={[
            'yyyy-MM-dd HH:mm:ss',
            'yyyy.MM.dd HH:mm:ss',
            'yyyy/MM/dd HH:mm:ss',
            'yyyyMMddHHmmss',
          ]}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceS3FileStepForm;
