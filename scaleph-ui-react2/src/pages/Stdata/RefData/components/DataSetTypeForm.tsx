import {useIntl} from "@umijs/max";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/typings';
import {MetaDataSetType} from "@/services/stdata/typings";
import {RefdataService} from "@/services/stdata/refdata.service";

const DataSetTypeForm: React.FC<ModalFormProps<MetaDataSetType>> = ({
                                                                      data,
                                                                      visible,
                                                                      onVisibleChange,
                                                                      onCancel
                                                                    }) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.stdata.dataSetType'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.stdata.dataSetType'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: MetaDataSetType = {...values};
          data.id
            ? RefdataService.updateDataSetType(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : RefdataService.addDataSetType(param).then((response) => {
              if (response.success) {
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
        initialValues={data}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormText
          name={"dataSetTypeCode"}
          label={intl.formatMessage({id: 'pages.stdata.dataSetType.dataSetTypeCode'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"dataSetTypeName"}
          label={intl.formatMessage({id: 'pages.stdata.dataSetType.dataSetTypeName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'pages.stdata.remark'})}
        />
      </ProForm>
    </Modal>
  );

}

export default DataSetTypeForm;
