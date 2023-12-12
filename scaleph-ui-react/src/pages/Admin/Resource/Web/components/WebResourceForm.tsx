import {useIntl} from 'umi';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormSwitch, ProFormText} from "@ant-design/pro-components";
import {SecResourceWeb} from "@/services/admin/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";
import {ResourceWebService} from "@/services/admin/resourceWeb.service";

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
          name: data.name,
          path: data.path,
          redirect: data.redirect,
          layout: data.layout,
          icon: data.icon,
          component: data.component,
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
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.resourceType)}
          allowClear={false}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"name"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.name'})}
        />
        <ProFormText
          name={"path"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.path'})}
        />
        <ProFormText
          name={"redirect"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.redirect'})}
        />
        <ProFormSwitch
          name={"layout"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.layout'})}
        />
        <ProFormText
          name={"icon"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.icon'})}
        />
        <ProFormText
          name={"component"}
          label={intl.formatMessage({id: 'pages.admin.resource.web.component'})}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );

}

export default WebResourceForm;
