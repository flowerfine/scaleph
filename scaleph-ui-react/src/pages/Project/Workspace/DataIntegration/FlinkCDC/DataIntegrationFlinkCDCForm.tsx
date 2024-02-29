import {Form, message} from 'antd';
import {ModalForm, ProFormDigit, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {ModalFormProps} from '@/typings';
import {WORKSPACE_CONF} from '@/constants/constant';
import {WsArtifactFlinkCDC} from '@/services/project/typings';
import {WsArtifactFlinkCDCService} from "@/services/project/WsArtifactFlinkCDCService";

const DataIntegrationFlinkCDCForm: React.FC<ModalFormProps<WsArtifactFlinkCDC>> = ({
                                                                              data,
                                                                              visible,
                                                                              onVisibleChange
                                                                            }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <ModalForm
      title={
        intl.formatMessage({id: 'app.common.operate.new.label'}) +
        intl.formatMessage({id: 'pages.project.artifact.cdc'})
      }
      form={form}
      initialValues={{
        id: data?.id,
        name: data?.artifact?.name,
        remark: data?.artifact?.remark,
      }}
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
        const param = {
          id: values.id,
          projectId: projectId,
          name: values.name,
          remark: values.remark
        };
        return values.id
          ? WsArtifactFlinkCDCService.update(param).then((response) => {
            if (response.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
              if (onVisibleChange) {
                onVisibleChange(false);
              }
            }
          })
          : WsArtifactFlinkCDCService.add(param).then((response) => {
            if (response.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
              if (onVisibleChange) {
                onVisibleChange(false);
              }
            }
          })
      }}
    >
      <ProFormDigit name="id" hidden/>
      <ProFormText
        name="name"
        label={intl.formatMessage({id: 'pages.project.artifact.name'})}
        rules={[{required: true}, {max: 32}]}
      />
      <ProFormTextArea
        name="remark"
        label={intl.formatMessage({id: 'app.common.data.remark'})}
        rules={[{max: 200}]}
      />
    </ModalForm>
  );
};

export default DataIntegrationFlinkCDCForm;
