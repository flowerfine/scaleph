import {useIntl} from '@umijs/max';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormText} from '@ant-design/pro-components';
import {MetaSystem} from "@/services/stdata/typings";
import {MetaSystemService} from "@/services/stdata/system.service";
import { ModalFormProps } from '@/typings';

const MetaSystemForm: React.FC<ModalFormProps<MetaSystem>> = ({
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
          intl.formatMessage({id: 'pages.stdata.system'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.stdata.system'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: MetaSystem = {...values};
          data.id
            ? MetaSystemService.update(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : MetaSystemService.add(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
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
        initialValues={data}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormText
          name={"systemCode"}
          label={intl.formatMessage({id: 'pages.stdata.system.systemCode'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"systemName"}
          label={intl.formatMessage({id: 'pages.stdata.system.systemName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"contacts"}
          label={intl.formatMessage({id: 'pages.stdata.system.contacts'})}
        />
        <ProFormText
          name={"contactsPhone"}
          label={intl.formatMessage({id: 'pages.stdata.system.contactsPhone'})}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'pages.stdata.remark'})}
        />
      </ProForm>
    </Modal>
  );
};

export default MetaSystemForm;
