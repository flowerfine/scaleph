import {Form, message} from 'antd';
import {ModalForm, ProFormDigit, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {ModalFormProps} from '@/typings';
import {WsProjectService} from '@/services/project/WsProjectService';
import {WsProject} from '@/services/project/typings';

const ProjectForm: React.FC<ModalFormProps<WsProject>> = ({
                                                            data,
                                                            visible,
                                                            onVisibleChange
                                                          }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  console.log('ProjectForm', data)

  return (
    <ModalForm
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.project'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project'})
      }
      form={form}
      initialValues={data}
      open={visible}
      onOpenChange={onVisibleChange}
      width={580}
      layout={"horizontal"}
      labelCol={{span: 6}}
      wrapperCol={{span: 16}}
      modalProps={{
        destroyOnClose: true,
        closeIcon: false
      }}
      onFinish={(values: Record<string, any>) => {
        let param: WsProject = {
          id: values.id,
          projectCode: values.projectCode,
          projectName: values.projectName,
          remark: values.remark,
        };
        return param.id
          ? WsProjectService.updateProject(param).then((response) => {
            if (response.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
              onVisibleChange(false)
            }
          })
          : WsProjectService.addProject(param).then((response) => {
            if (response.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
              onVisibleChange(false)
            }
          });
      }}
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
      <ProFormTextArea
        name="remark"
        label={intl.formatMessage({id: 'app.common.data.remark'})}
        rules={[{max: 200}]}
      />
    </ModalForm>
  );
};

export default ProjectForm;
