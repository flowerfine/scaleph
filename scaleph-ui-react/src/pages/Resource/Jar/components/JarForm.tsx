import {useIntl} from 'umi';
import {useState} from 'react';
import {Form, message, Modal, UploadFile, UploadProps} from 'antd';
import {ProForm, ProFormDigit, ProFormTextArea, ProFormUploadButton} from '@ant-design/pro-components';
import {ModalFormProps} from '@/app.d';
import {ResourceJarService} from '@/services/resource/jar.service';
import {Jar, JarUploadParam} from '@/services/resource/typings';

const JarForm: React.FC<ModalFormProps<Jar>> = ({data, visible, onVisibleChange, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);

  const props: UploadProps = {
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
          intl.formatMessage({id: 'pages.resource.jar'})
          : intl.formatMessage({id: 'app.common.operate.upload.label'}) +
          intl.formatMessage({id: 'pages.resource.jar'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      confirmLoading={uploading}
      okText={
        uploading
          ? intl.formatMessage({id: 'app.common.operate.uploading.label'})
          : intl.formatMessage({id: 'app.common.operate.upload.label'})
      }
      onOk={() => {
        form.validateFields().then((values) => {
          const uploadParam: JarUploadParam = {
            group: values.group,
            file: fileList[0],
            remark: values.remark,
          };
          setUploading(true);
          ResourceJarService.upload(uploadParam)
            .then(() => {
              setFileList([]);
              message.success(intl.formatMessage({id: 'app.common.operate.upload.success'}));
            })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.upload.failure'}));
            })
            .finally(() => {
              setUploading(false);
              onVisibleChange(false);
            });
        });
      }}
    >
      <ProForm form={form} layout={"horizontal"} submitter={false} labelCol={{span: 6}} wrapperCol={{span: 16}}>
        <ProFormDigit name="id" hidden/>
        <ProFormUploadButton
          name={"file"}
          label={intl.formatMessage({id: 'pages.resource.file'})}
          title={intl.formatMessage({id: 'pages.resource.jar.file'})}
          max={1}
          fieldProps={props}
          rules={[{required: true}]}
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

export default JarForm;
