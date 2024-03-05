import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {JdbcParams, STEP_ATTR_TYPE} from '../constant';
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";
import {DsInfoParam} from "@/services/datasource/typings";
import {DsInfoService} from "@/services/datasource/info.service";

const SourceJdbcStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.datasourceType)}
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
          name={JdbcParams.partitionColumn}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionColumn'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionColumn.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />
        <ProFormDigit
          name={JdbcParams.partitionLowerBound}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionLowerBound'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionLowerBound.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />
        <ProFormDigit
          name={JdbcParams.partitionUpperBound}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionUpperBound'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionUpperBound.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />
        <ProFormDigit
          name={JdbcParams.partitionNum}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionNum'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionNum.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />





        <ProFormDigit
          name={JdbcParams.fetchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.fetchSize'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.fetchSize.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={0}
        />

        <ProFormTextArea
          name={JdbcParams.query}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.query'})}
          rules={[{required: true}]}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceJdbcStepForm;
