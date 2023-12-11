import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {Form, Modal} from 'antd';
import {useIntl} from 'umi';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import React from "react";
import {DictDataService} from "@/services/admin/dictData.service";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DICT_TYPE} from "@/constants/dictType";

interface DiJobFormProps<DiJob> {
  data: DiJob;
  visible: boolean;
  onVisibleChange: (visible: boolean, data: any) => void;
  onCancel: () => void;
}

const DiJobForm: React.FC<DiJobFormProps<WsDiJob>> = ({data, visible, onVisibleChange, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const submit = () => {
    form.validateFields().then((values) => {
      const job = {
        projectId: projectId,
        name: values.name,
        jobEngine: values.jobEngine,
        remark: values.remark,
      };
      data.id
        ? WsDiJobService.updateJob({...job, id: data.id}).then((response) => {
          if (response.success) {
            onVisibleChange(false, response.data);
          }
        })
        : WsDiJobService.addJob({...job}).then((response) => {
          if (response.success) {
            onVisibleChange(false, response.data);
          }
        });
    });
  };

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.project.di.job'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.di.job'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={submit}
    >
      <ProForm
        form={form}
        layout="horizontal"
        submitter={false}
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
        initialValues={{
          id: data?.id,
          name: data?.wsFlinkArtifact?.name,
          jobEngine: data?.jobEngine?.value,
          remark: data?.wsFlinkArtifact?.remark,
        }}
      >
        <ProFormDigit name={"id"} hidden/>
        <ProFormText
          name={"name"}
          label={intl.formatMessage({id: 'pages.project.artifact.name'})}
          rules={[{required: true}, {max: 200}]}
        />
        <ProFormSelect
          name={"jobEngine"}
          label={intl.formatMessage({id: 'pages.project.di.jobEngine'})}
          rules={[{required: true}]}
          allowClear={false}
          request={(params, props) => {
            return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelEngineType)
          }}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
          rules={[{max: 200}]}
        />
      </ProForm>
    </Modal>
  );
};

export default DiJobForm;
