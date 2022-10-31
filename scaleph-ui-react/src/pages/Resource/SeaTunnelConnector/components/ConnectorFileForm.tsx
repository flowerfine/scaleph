import {ModalFormProps} from '@/app.d';
import {SeaTunnelConnectorUploadParam} from '@/services/resource/typings';
import {Form, message, Modal, UploadFile, UploadProps} from 'antd';
import {useState} from 'react';
import {useIntl} from 'umi';
import {ProForm, ProFormSelect, ProFormUploadButton} from "@ant-design/pro-components";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {SeatunnelReleaseService} from "@/services/resource/seatunnelRelease.service";

const ConnectorFileForm: React.FC<ModalFormProps<{ id: number }>> =
  ({data, visible, onVisibleChange, onCancel}) => {
    const intl = useIntl();
    const [form] = Form.useForm();
    const [fileList, setFileList] = useState<UploadFile[]>([]);
    const [uploading, setUploading] = useState(false);

    const props: UploadProps = {
      name: 'files',
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
        title={
          intl.formatMessage({id: 'app.common.operate.upload.label'}) +
          intl.formatMessage({id: 'pages.resource.seatunnelRelease.connector'})
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
            const uploadParam: SeaTunnelConnectorUploadParam = {
              id: data.id,
              pluginName: values.pluginName,
              file: fileList[0]
            };
            setUploading(true);
            SeatunnelReleaseService.uploadConnector(uploadParam)
              .then(() => {
                setFileList([]);
                message.success(intl.formatMessage({id: 'app.common.operate.upload.success'}));
              })
              .finally(() => {
                setUploading(false);
                onVisibleChange(false);
              });
          });
        }}
      >
        <ProForm form={form} layout={"horizontal"} submitter={false} labelCol={{span: 6}} wrapperCol={{span: 16}}>
          <ProFormSelect
            name={"pluginName"}
            label={intl.formatMessage({id: 'pages.resource.seatunnelRelease.pluginName'})}
            rules={[{required: true}]}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.seatunnelPluginName)}
          />
          <ProFormUploadButton
            name={"file"}
            label={intl.formatMessage({id: 'pages.resource.file'})}
            title={intl.formatMessage({id: 'pages.resource.seatunnelRelease.connector'})}
            max={1}
            fieldProps={props}
            rules={[{required: true}]}
          />
        </ProForm>
      </Modal>
    );
  };

export default ConnectorFileForm;
