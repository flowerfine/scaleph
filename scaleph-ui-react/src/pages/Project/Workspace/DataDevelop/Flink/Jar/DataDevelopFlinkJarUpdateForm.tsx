import {useState} from "react";
import {Form, message, UploadFile, UploadProps} from 'antd';
import {
  ModalForm,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormUploadButton
} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {ModalFormProps} from '@/typings';
import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {WsArtifactFlinkJar, WsArtifactFlinkJarUpdateParam} from '@/services/project/typings';
import {WsArtifactFlinkJarService} from "@/services/project/WsArtifactFlinkJarService";
import {DictDataService} from "@/services/admin/dictData.service";

const DataDevelopFlinkJarUpdateForm: React.FC<ModalFormProps<WsArtifactFlinkJar>> = ({
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
    <ModalForm
      title={
        intl.formatMessage({id: 'app.common.operate.edit.label'}) +
        intl.formatMessage({id: 'pages.project.artifact.jar'})
      }
      form={form}
      initialValues={{
        id: data.id,
        flinkArtifactId: data.artifact?.id,
        name: data.artifact?.name,
        remark: data.artifact?.remark,
        flinkVersion: data.flinkVersion?.value,
        entryClass: data.entryClass,
        jarParams: data.jarParams
      }}
      open={visible}
      onOpenChange={onVisibleChange}
      width={580}
      layout={"horizontal"}
      labelCol={{span: 6}}
      wrapperCol={{span: 16}}
      modalProps={{
        destroyOnClose: true,
        closeIcon: false,
        confirmLoading: uploading,
        okText: uploading
          ? intl.formatMessage({id: 'app.common.operate.uploading.label'})
          : intl.formatMessage({id: 'app.common.operate.upload.label'})
      }}
      onFinish={(values: Record<string, any>) => {
        const param: WsArtifactFlinkJarUpdateParam = {
          id: values.id,
          name: values.name,
          remark: values.remark,
          entryClass: values.entryClass,
          flinkVersion: values.flinkVersion,
          jarParams: values.jarParams,
          file: fileList[0]
        };

        setUploading(true);
        return WsArtifactFlinkJarService.updateJar(param).then((response) => {
          if (response.success) {
            message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
            setFileList([]);
            setUploading(false);
            if (onVisibleChange) {
              onVisibleChange(false);
            }
          }
        });
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
      />
      <ProFormText
        name="entryClass"
        label={intl.formatMessage({id: 'pages.project.artifact.jar.entryClass'})}
        rules={[{required: true}]}
      />
      <ProFormTextArea
        name="jarParams"
        label={intl.formatMessage({id: 'pages.project.artifact.jar.jarParams'})}
        placeholder={intl.formatMessage({id: 'pages.project.artifact.jar.jarParams.placeholder'})}
        fieldProps={{
          rows: 3
        }}
      />
      <ProFormTextArea
        name="remark"
        label={intl.formatMessage({id: 'app.common.data.remark'})}
        rules={[{max: 200}]}
      />
    </ModalForm>
  );
};

export default DataDevelopFlinkJarUpdateForm;
