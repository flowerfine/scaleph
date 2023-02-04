import {useIntl} from 'umi';
import {useState} from 'react';
import {Form, message, Modal, UploadFile, UploadProps} from 'antd';
import {ProForm, ProFormUploadDragger} from '@ant-design/pro-components';
import {ModalFormProps} from '@/app.d';
import {ClusterCredentialService} from '@/services/resource/clusterCredential.service';
import {CredentialFileUploadParam} from '@/services/resource/typings';

const CredentialFileForm: React.FC<ModalFormProps<{ id: number }>> =
  ({data, visible, onVisibleChange, onCancel}) => {
    const intl = useIntl();
    const [form] = Form.useForm();
    const [fileList, setFileList] = useState<UploadFile[]>([]);
    const [uploading, setUploading] = useState(false);

    const props: UploadProps = {
      name: 'files',
      multiple: true,
      maxCount: 10,
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
        title={
          intl.formatMessage({id: 'app.common.operate.upload.label'}) +
          intl.formatMessage({id: 'pages.resource.credentialFile'})
        }
        open={visible}
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
            const uploadParam: CredentialFileUploadParam = {
              id: data.id,
              files: fileList,
            };
            setUploading(true);
            ClusterCredentialService.uploadFiles(uploadParam)
              .then((response) => {
                if (response.success) {
                  setFileList([]);
                  message.success(intl.formatMessage({id: 'app.common.operate.upload.success'}));
                }
              })
              .finally(() => {
                setUploading(false);
                onVisibleChange(false);
              });
          });
        }}
      >
        <ProForm form={form} submitter={false}>
          <ProFormUploadDragger
            name={"files"}
            description={intl.formatMessage({id: 'pages.resource.files.upload.hint'})}
            fieldProps={props}
            rules={[{required: true}]}
          />
        </ProForm>
      </Modal>
    );
  };

export default CredentialFileForm;
