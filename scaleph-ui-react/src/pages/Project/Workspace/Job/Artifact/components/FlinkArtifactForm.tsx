import {useIntl} from 'umi';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormText} from '@ant-design/pro-components';
import {ModalFormProps} from '@/app.d';
import {WORKSPACE_CONF} from '@/constant';
import {FlinkArtifactService} from '@/services/project/flinkArtifact.service';
import {WsFlinkArtifact} from '@/services/project/typings';

const FlinkArtifactForm: React.FC<ModalFormProps<WsFlinkArtifact>> = ({data, visible, onVisibleChange, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.project.job.artifact'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.job.artifact'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: WsFlinkArtifact = {
            id: values.id,
            name: values.name,
            projectId: projectId + '',
            remark: values.remark,
          };
          data.id
            ? FlinkArtifactService.update(param).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : FlinkArtifactService.add(param).then((d) => {
              if (d.success) {
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
        layout="horizontal"
        submitter={false}
        labelCol={{span: 4}}
        wrapperCol={{span: 18}}
        initialValues={{
          id: data.id,
          name: data.name,
          remark: data.remark,
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.project.artifact.name'})}
          rules={[{required: true}, {max: 32}]}
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

export default FlinkArtifactForm;
