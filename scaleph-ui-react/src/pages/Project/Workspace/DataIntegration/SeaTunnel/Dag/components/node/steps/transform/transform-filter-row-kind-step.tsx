import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProForm, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {FilterRowKindParams, STEP_ATTR_TYPE} from '../constant';
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";

const TransformFilterRowKindStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <ProForm form={form} initialValues={data.data.attrs} grid={true} submitter={false}>
          <ProFormText
            name={STEP_ATTR_TYPE.stepTitle}
            label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
            rules={[{required: true}, {max: 120}]}
          />
          <ProFormSelect
            name={FilterRowKindParams.includeKinds}
            label={intl.formatMessage({id: 'pages.project.di.step.filterRowKind.includeKinds'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.filterRowKind.includeKinds.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            mode={'multiple'}
            request={() => {
              return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelRowKind)
            }}
          />
          <ProFormSelect
            name={FilterRowKindParams.excludeKinds}
            label={intl.formatMessage({id: 'pages.project.di.step.filterRowKind.excludeKinds'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.filterRowKind.excludeKinds.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            mode={'multiple'}
            request={() => {
              return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelRowKind)
            }}
          />
        </ProForm>
      </DrawerForm>
    </XFlow>
  );
};

export default TransformFilterRowKindStepForm;
