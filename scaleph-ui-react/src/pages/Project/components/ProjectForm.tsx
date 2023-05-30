import {ModalFormProps} from '@/app.d';
import {WsProjectService} from '@/services/project/WsProjectService';
import {WsProject} from '@/services/project/typings';
import {Form, message, Modal} from 'antd';
import {useIntl} from 'umi';
import {ProForm, ProFormDigit, ProFormText} from "@ant-design/pro-components";

const ProjectForm: React.FC<ModalFormProps<WsProject>> = ({
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
          intl.formatMessage({id: 'pages.project'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let param: WsProject = {
            id: values.id,
            projectCode: values.projectCode,
            projectName: values.projectName,
            remark: values.remark,
          };
          data.id
            ? WsProjectService.updateProject({...param}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                onVisibleChange(false);
              }
            })
            : WsProjectService.addProject({...param}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                onVisibleChange(false);
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
          name="projectCode"
          label={intl.formatMessage({id: 'pages.project.projectCode'})}
          rules={[
            {required: true},
            {max: 30},
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({id: 'app.common.validate.characterWord'}),
            },
          ]}
          disabled={data.id ? true : false}
        />
        <ProFormText
          name="projectName"
          label={intl.formatMessage({id: 'pages.project.projectName'})}
          rules={[{required: true}, {max: 60}]}
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

export default ProjectForm;
