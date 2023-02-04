import {history, useIntl} from 'umi';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from '@/constant';
import {DictDataService} from '@/services/admin/dictData.service';
import {ClusterCredentialService} from '@/services/resource/clusterCredential.service';
import {ClusterCredential} from '@/services/resource/typings';

const ClusterCredentialForm: React.FC<ModalFormProps<ClusterCredential>> = ({
                                                                              data,
                                                                              visible,
                                                                              onVisibleChange,
                                                                              onCancel
                                                                            }) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.resource.clusterCredential'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.resource.clusterCredential'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: ClusterCredential = {
            id: values.id,
            configType: values.configType,
            name: values.name,
            remark: values.remark,
          };
          data.id
            ? ClusterCredentialService.update(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : ClusterCredentialService.add(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
                history.push('/resource/cluster-credential/file', {id: response.data?.id});
              }
            });
        });
      }}
    >
      <ProForm
        form={form}
        layout={"horizontal"}
        submitter={false}
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
        initialValues={{
          id: data.id,
          configType: data.configType?.value,
          name: data.name,
          remark: data.remark,
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormSelect
          name="configType"
          label={intl.formatMessage({id: 'pages.resource.clusterCredential.configType'})}
          rules={[{required: true}]}
          request={(params, props) => {
            return DictDataService.listDictDataByType2(DICT_TYPE.flinkResourceProvider)
          }}
        />
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.resource.clusterCredential.name'})}
          rules={[
            {required: true},
            {max: 30},
            {
              pattern: /^[\w\s-_.]+$/,
              message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
            },
          ]}
        />
        <ProFormText
          name="remark"
          label={intl.formatMessage({id: 'app.common.data.remark'})}
          rules={[{max: 200}]}
        />
      </ProForm>
    </Modal>
  );
};

export default ClusterCredentialForm;
