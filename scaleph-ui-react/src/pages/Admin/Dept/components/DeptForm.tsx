import {useIntl} from 'umi';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTreeSelect} from "@ant-design/pro-components";
import {SecDeptTree} from "@/services/admin/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";
import {DeptService} from "@/services/admin/dept.service";

interface ModalFormParentProps<T> {
  parent: T;
  data: T;
  visible: boolean;
  onVisibleChange?: (visible: boolean) => void;
  onCancel: () => void;
  onOK?: (values: any) => void;
};
const DeptForm: React.FC<ModalFormParentProps<SecDeptTree>> = ({
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
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.admin.dept'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.admin.dept'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          data.id
            ? DeptService.updateDept({...values}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : DeptService.addDept({...values}).then((response) => {
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
          pid: parent?.id ? parent.id : (data?.pid ? data.pid : null),
          deptName: data.deptName,
          deptCode: data.deptCode,
          deptStatus: data.deptStatus?.value
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormTreeSelect
          name={"pid"}
          label={intl.formatMessage({id: 'pages.admin.dept.pid'})}
          allowClear={false}
          request={() => DeptService.listAllDept().then((data) => DeptService.buildTree(data))}
          fieldProps={{
            style: {width: '100%'},
            dropdownStyle: {maxHeight: 480, overflow: 'auto'},
            treeLine: {showLeafIcon: false},
            showSearch: true,
            fieldNames: {label: 'title', value: 'key', children: 'children'},
            treeDefaultExpandAll: true
          }}
        />
        <ProFormText
          name={"deptName"}
          label={intl.formatMessage({id: 'pages.admin.dept.deptName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"deptCode"}
          label={intl.formatMessage({id: 'pages.admin.dept.deptCode'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"deptStatus"}
          label={intl.formatMessage({id: 'pages.admin.dept.deptStatus'})}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.deptStatus)}
          allowClear={false}
          rules={[{required: true}]}
        />
      </ProForm>
    </Modal>
  );

}

export default DeptForm;
