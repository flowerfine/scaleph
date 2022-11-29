import { ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkClusterConfigService } from '@/services/project/flinkClusterConfig.service';
import { FlinkCLusterInstanceService } from '@/services/project/flinkClusterInstance.service';
import { FlinkClusterConfigParam, FlinkSessionClusterNewParam } from '@/services/project/typings';
import { useIntl } from '@@/exports';
import { ProForm, ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { Form, message, Modal } from 'antd';
import { useState } from 'react';

const SessionClusterForm: React.FC<ModalFormProps<any>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [confirming, setConfirming] = useState(false);

  return (
    <Modal
      open={visible}
      title={
        intl.formatMessage({ id: 'app.common.operate.new.label' }) +
        ' ' +
        intl.formatMessage({ id: 'pages.dev.clusterInstance.session' })
      }
      width={580}
      confirmLoading={confirming}
      closable={false}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: FlinkSessionClusterNewParam = {
            flinkClusterConfigId: values.flinkClusterConfig,
            remark: values.remark,
          };
          setConfirming(true);
          FlinkCLusterInstanceService.newSession(param)
            .then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
              }
            })
            .catch(() => {
              message.error(intl.formatMessage({ id: 'app.common.operate.new.failure' }));
            })
            .finally(() => {
              setConfirming(false);
              onVisibleChange(false);
            });
        });
      }}
    >
      <ProForm
        form={form}
        layout={'horizontal'}
        submitter={false}
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 16 }}
      >
        <ProFormSelect
          name="flinkVersion"
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.flinkVersion' })}
          rules={[{ required: true }]}
          showSearch={true}
          request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkVersion)}
        />
        <ProFormSelect
          name="resourceProvider"
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.resourceProvider' })}
          rules={[{ required: true }]}
          showSearch={true}
          request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkResourceProvider)}
        />
        <ProFormSelect
          name="flinkClusterConfig"
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig' })}
          rules={[{ required: true }]}
          showSearch={true}
          dependencies={['flinkVersion', 'resourceProvider']}
          request={(params) => {
            const listParam: FlinkClusterConfigParam = {
              flinkVersion: params.flinkVersion,
              resourceProvider: params.resourceProvider,
              deployMode: '2',
            };
            return FlinkClusterConfigService.list(listParam).then((response) => {
              return response.data.map((item) => {
                return { label: item.name, value: item.id };
              });
            });
          }}
        />
        <ProFormText
          name="remark"
          label={intl.formatMessage({ id: 'pages.dev.remark' })}
          rules={[{ max: 200 }]}
        />
      </ProForm>
    </Modal>
  );
};

export default SessionClusterForm;
