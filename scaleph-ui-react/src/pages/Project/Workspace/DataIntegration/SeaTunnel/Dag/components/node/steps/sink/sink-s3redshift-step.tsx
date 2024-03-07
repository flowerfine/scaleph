import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
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
import {BaseFileParams, S3FileParams, S3RedshiftParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";

const SinkS3RedshiftStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          colProps={{span: 24}}
        />
        <DataSourceItem dataSource={"S3"}/>
        <ProFormText
          name={S3RedshiftParams.jdbcUrl}
          label={intl.formatMessage({id: 'pages.project.di.step.s3redshift.jdbcUrl'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={S3RedshiftParams.jdbcUser}
          label={intl.formatMessage({id: 'pages.project.di.step.s3redshift.jdbcUser'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={S3RedshiftParams.jdbcPassword}
          label={intl.formatMessage({id: 'pages.project.di.step.s3redshift.jdbcPassword'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={S3RedshiftParams.executeSql}
          label={intl.formatMessage({id: 'pages.project.di.step.s3redshift.executeSql'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.s3redshift.executeSql.placeholder'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={S3RedshiftParams.bucket}
          label={intl.formatMessage({id: 'pages.project.di.step.s3redshift.bucket'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.s3redshift.bucket.placeholder'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.s3redshift.bucket.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          rules={[{required: true}]}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
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
          colProps={{span: 24}}
        />
        <ProFormSelect
          name={BaseFileParams.fileFormatType}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFormat'})}
          colProps={{span: 24}}
          valueEnum={{
            json: 'json',
            parquet: 'parquet',
            orc: 'orc',
            text: 'text',
            csv: 'csv',
          }}
        />
        <ProFormDependency name={[BaseFileParams.fileFormatType]}>
          {({file_format_type}) => {
            if (file_format_type == 'text' || file_format_type == 'csv') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={BaseFileParams.fieldDelimiter}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fieldDelimiter'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={BaseFileParams.rowDelimiter}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.rowDelimiter'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormText
          name={BaseFileParams.fileNameExpression}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileNameExpression'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={BaseFileParams.filenameTimeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.filenameTimeFormat'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={BaseFileParams.partitionBy}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionBy'})}
          colProps={{span: 8}}
        />
        <ProFormText
          name={BaseFileParams.partitionDirExpression}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionDirExpression'})}
          colProps={{span: 8}}
        />
        <ProFormSwitch
          name={BaseFileParams.isPartitionFieldWriteInFile}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isPartitionFieldWriteInFile'})}
          colProps={{span: 8}}
        />
        <ProFormText
          name={BaseFileParams.sinkColumns}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sinkColumns'})}
        />
        <ProFormSwitch
          name={BaseFileParams.isEnableTransaction}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isEnableTransaction'})}
          initialValue={true}
          disabled
        />
        <ProFormDigit
          name={BaseFileParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.batchSize'})}
          initialValue={1000000}
          fieldProps={{
            step: 10000,
            min: 0,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkS3RedshiftStepForm;
