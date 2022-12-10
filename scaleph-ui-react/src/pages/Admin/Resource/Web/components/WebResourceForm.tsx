import {useIntl} from 'umi';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {SecPrivilege} from "@/services/admin/typings";
import {PrivilegeService} from "@/services/admin/privilege.service";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

interface ModalFormParentProps<T> {
  parent: T;
  data: T;
  visible: boolean;
  onVisibleChange?: (visible: boolean) => void;
  onCancel: () => void;
  onOK?: (values: any) => void;
};
const WebResourceForm: React.FC<ModalFormParentProps<SecPrivilege>> = ({
                                                                         parent,
                                                                         data,
                                                                         visible,
                                                                         onVisibleChange,
                                                                         onCancel,
                                                                       }) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) + ' ' +
          intl.formatMessage({id: 'pages.admin.resource.web'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) + ' ' +
          intl.formatMessage({id: 'pages.admin.resource.web'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          console.log('validateFields', values)
          data.id
            ? PrivilegeService.update({...values}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : PrivilegeService.add({...values}).then((response) => {
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
          name={"pid"}
          label={intl.formatMessage({id: 'pages.admin.resource.pid'})}
          initialValue={parent?.id ? parent.id : (data?.pid ? data.pid : 0)}
          disabled
        />
        <ProFormText
          name={"privilegeName"}
          label={intl.formatMessage({id: 'pages.admin.resource.privilegeName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"privilegeCode"}
          label={intl.formatMessage({id: 'pages.admin.resource.privilegeCode'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"resourceType"}
          label={intl.formatMessage({id: 'pages.admin.resource.resourceType'})}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.resourceType)}
          allowClear={false}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"resourcePath"}
          label={intl.formatMessage({id: 'pages.admin.resource.resourcePath'})}
        />
      </ProForm>
    </Modal>
  );

}

export default WebResourceForm;
