import {useIntl} from '@umijs/max';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {SecResourceWeb} from "@/services/admin/typings";
import {DICT_TYPE} from "@/constants/dictType";
import {SysDictService} from "@/services/admin/system/sysDict.service";
import {ResourceWebService} from "@/services/admin/security/resourceWeb.service";

interface ModalFormParentProps<T> {
  parent: T;
  data: T;
  visible: boolean;
  onVisibleChange?: (visible: boolean) => void;
  onCancel: () => void;
  onOK?: (values: any) => void;
};

const WebResourceForm: React.FC<ModalFormParentProps<SecResourceWeb>> = ({
                                                                           parent,
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
          data.id
            ? ResourceWebService.update({...values}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : ResourceWebService.add({...values}).then((response) => {
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
        initialValues={{
          id: data.id,
          pid: parent?.id ? parent.id : (data?.pid ? data.pid : 0),
          type: data.type?.value,
          value: data.value,
          label: data.label,
          path: data.path,
          order: data.order,
          status: data.status?.value,
          remark: data.remark
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormText
          name={"pid"}
          label={intl.formatMessage({id: 'pages.admin.resource.pid'})}
          disabled
        />
        <ProFormSelect
          name={"type"}
          label={intl.formatMessage({id: 'pages.admin.resource.type'})}
          request={() => SysDictService.listDictByDefinition(DICT_TYPE.carpSecResourceWebType)}
          allowClear={false}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"value"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.value'})}
        />
        <ProFormText
          name={"label"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.label'})}
        />
        <ProFormText
          name={"path"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.path'})}
        />
        <ProFormDigit
          name={"order"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.order'})}
          min={0}
        />
        <ProFormSelect
          name={"status"}
          label={intl.formatMessage({id: 'pages.admin.resource.status'})}
          request={() => SysDictService.listDictByDefinition(DICT_TYPE.carpSecResourceStatus)}
          allowClear={false}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );
}

export default WebResourceForm;
