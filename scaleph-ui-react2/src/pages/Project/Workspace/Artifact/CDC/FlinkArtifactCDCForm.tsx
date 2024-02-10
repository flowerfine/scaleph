import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {ModalFormProps} from '@/typings';
import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {WsFlinkArtifactCDC} from '@/services/project/typings';
import {DictDataService} from "@/services/admin/dictData.service";
import {WsFlinkCDCService} from "@/services/project/WsFlinkCDCService";

const FlinkArtifactCDCForm: React.FC<ModalFormProps<WsFlinkArtifactCDC>> = ({
                                                                              data,
                                                                              visible,
                                                                              onVisibleChange,
                                                                              onCancel
                                                                            }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <Modal
      open={visible}
      title={
        intl.formatMessage({id: 'app.common.operate.new.label'}) +
        intl.formatMessage({id: 'pages.project.artifact.cdc'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param = {
            id: values.id,
            projectId: projectId,
            name: values.name,
            remark: values.remark,
            flinkVersion: values.flinkVersion,
          };
          data?.id
            ? WsFlinkCDCService.update(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : WsFlinkCDCService.add(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
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
          id: data?.id,
          name: data?.wsFlinkArtifact?.name,
          flinkVersion: data?.flinkVersion?.value,
          remark: data?.wsFlinkArtifact?.remark,
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.project.artifact.name'})}
          rules={[{required: true}, {max: 32}]}
        />
        <ProFormSelect
          name="flinkVersion"
          label={intl.formatMessage({id: 'pages.resource.flinkRelease.version'})}
          rules={[{required: true}]}
          allowClear={false}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)
          }}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({id: 'app.common.data.remark'})}
          rules={[{max: 200}]}
        />
      </ProForm>
    </Modal>
  );
};

export default FlinkArtifactCDCForm;
