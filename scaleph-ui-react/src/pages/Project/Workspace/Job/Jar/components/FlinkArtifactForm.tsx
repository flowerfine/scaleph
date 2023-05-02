import {ModalFormProps} from '@/app.d';
import {DICT_TYPE, WORKSPACE_CONF} from '@/constant';
import {WsFlinkArtifactJar, WsFlinkArtifactJarUploadParam} from '@/services/project/typings';
import {Form, Input, message, Modal, UploadFile, UploadProps} from 'antd';
import {useIntl} from 'umi';
import {FlinkArtifactJarService} from "@/services/project/flinkArtifactJar.service";
import {
  ProForm,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormUploadButton
} from "@ant-design/pro-components";
import {useState} from "react";
import {DictDataService} from "@/services/admin/dictData.service";

const FlinkArtifactForm: React.FC<ModalFormProps<WsFlinkArtifactJar>> = ({
                                                                           data,
                                                                           visible,
                                                                           onVisibleChange,
                                                                           onCancel
                                                                         }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const props: UploadProps = {
    accept: '.jar',
    multiple: false,
    maxCount: 1,
    onRemove: (file) => {
      const index = fileList.indexOf(file);
      const newFileList = fileList.slice();
      newFileList.splice(index, 1);
      setFileList(newFileList);
    },
    beforeUpload: (file) => {
      setFileList([...fileList, file]);
      return false;
    },
    fileList,
  };

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.project.job.jar'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.job.jar'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: WsFlinkArtifactJarUploadParam = {
            projectId: projectId + '',
            name: values.name,
            remark: values.remark,
            entryClass: values.entryClass,
            flinkVersion: values.flinkVersion,
            jarParams: values.jarParams,
            file: fileList[0]
          };
          data.id
            ? FlinkArtifactJarService.update(param).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : FlinkArtifactJarService.upload(param).then((d) => {
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
        layout={"horizontal"}
        submitter={false}
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
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
        <ProFormSelect
          name="flinkVersion"
          label={intl.formatMessage({id: 'pages.resource.flinkRelease.version'})}
          rules={[{required: true}]}
          allowClear={false}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)
          }}
        />
        <ProFormUploadButton
          name={"file"}
          label={intl.formatMessage({id: 'pages.project.artifact.jar'})}
          title={intl.formatMessage({id: 'pages.project.artifact.jar.file'})}
          max={1}
          fieldProps={props}
          rules={[{required: true}]}
        />
        <ProFormText
          name="entryClass"
          label={intl.formatMessage({ id: 'pages.project.artifact.jar.entryClass' })}
          rules={[{ required: true }]}
        />
        <ProFormTextArea
          name="jarParams"
          label={intl.formatMessage({ id: 'pages.project.artifact.jar.jarParams' })}
          placeholder={intl.formatMessage({id: 'pages.project.artifact.jar.jarParams.placeholder'})}
          fieldProps={{
            rows: 3
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

export default FlinkArtifactForm;
