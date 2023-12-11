import {ModalFormProps} from '@/app';
import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {WsFlinkArtifactSql, WsFlinkArtifactSqlAddParam} from '@/services/project/typings';
import {Form, message, Modal} from 'antd';
import {useIntl} from 'umi';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DictDataService} from "@/services/admin/dictData.service";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";

const FlinkArtifactSqlForm: React.FC<ModalFormProps<WsFlinkArtifactSql>> = ({
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
        intl.formatMessage({id: 'pages.project.artifact.sql'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: WsFlinkArtifactSqlAddParam = {
            id: values.id,
            projectId: projectId + '',
            name: values.name,
            remark: values.remark,
            flinkVersion: values.flinkVersion,
          };
          data?.id
            ? FlinkArtifactSqlService.update(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : FlinkArtifactSqlService.add(param).then((response) => {
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
        <ProFormText
          name="remark"
          label={intl.formatMessage({id: 'app.common.data.remark'})}
          rules={[{max: 200}]}
        />
      </ProForm>
    </Modal>
  );
};

export default FlinkArtifactSqlForm;
