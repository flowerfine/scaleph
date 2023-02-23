import {useIntl} from 'umi';
import {useState} from 'react';
import {Form, message, Modal, UploadFile, UploadProps} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormUploadButton} from '@ant-design/pro-components';
import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from '@/constant';
import {DictDataService} from '@/services/admin/dictData.service';
import {FlinkArtifactJarService} from '@/services/project/flinkArtifactJar.service';
import {WsFlinkArtifactJar} from '@/services/project/typings';

const FlinkArtifactJarForm: React.FC<ModalFormProps<WsFlinkArtifactJar>> = ({
                                                                              data,
                                                                              visible,
                                                                              onVisibleChange,
                                                                              onCancel
                                                                            }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);

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
          intl.formatMessage({id: 'pages.project.artifact.jar'})
          : intl.formatMessage({id: 'app.common.operate.upload.label'}) +
          intl.formatMessage({id: 'pages.project.artifact.jar'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      confirmLoading={uploading}
      okText={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.confirm.label'})
          : uploading
            ? intl.formatMessage({id: 'app.common.operate.uploading.label'})
            : intl.formatMessage({id: 'app.common.operate.upload.label'})
      }
      onOk={() => {
        data.id
          ? form.validateFields().then((values) => {
            const params: WsFlinkArtifactJar = {
              id: values.id,
              wsFlinkArtifact: {id: data.wsFlinkArtifact?.id as number},
              version: values.version,
              flinkVersion: values.flinkVersion,
              entryClass: values.entryClass,
              jarParams: values.jarParams,
            };
            FlinkArtifactJarService.update(params).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                onVisibleChange ? onVisibleChange(false) : null;
              }
            });
          })
          : form.validateFields().then((values) => {
            const uploadParam: WsFlinkArtifactJar = {
              wsFlinkArtifact: {id: data.wsFlinkArtifact?.id as number},
              version: values.version,
              flinkVersion: values.flinkVersion,
              entryClass: values.entryClass,
              file: fileList[0],
              jarParams: values.jarParams,
            };
            setUploading(true);
            FlinkArtifactJarService.upload(uploadParam)
              .then((response) => {
                setFileList([]);
                if (response.success) {
                  message.success(
                    intl.formatMessage({id: 'app.common.operate.upload.success'}),
                  );
                }
              })
              .catch(() => {
                message.error(intl.formatMessage({id: 'app.common.operate.upload.failure'}));
              })
              .finally(() => {
                setUploading(false);
                onVisibleChange ? onVisibleChange(false) : null;
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
          version: data.version,
          flinkVersion: data.flinkVersion?.value,
          entryClass: data.entryClass,
          jarParams: data.jarParams,
        }}
      >
        <ProFormDigit name="id" hidden/>
        {!data.id && (
          <ProFormUploadButton
            name={"file"}
            label={intl.formatMessage({id: 'pages.project.artifact.jar.file'})}
            title={intl.formatMessage({id: 'pages.project.artifact.jar'})}
            max={1}
            fieldProps={props}
            rules={[{required: true}]}
          />
        )}
        <ProFormText
          name="version"
          label={intl.formatMessage({id: 'pages.project.artifact.jar.version'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name="flinkVersion"
          label={intl.formatMessage({id: 'pages.resource.flinkRelease.version'})}
          rules={[{required: true}]}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion);
          }}
        />
        <ProFormText
          name="entryClass"
          label={intl.formatMessage({id: 'pages.project.artifact.jar.entryClass'})}
          rules={[{required: true}]}
        />
      </ProForm>
    </Modal>
  );
};

export default FlinkArtifactJarForm;
