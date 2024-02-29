import {useIntl} from "umi";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from "@/constants/dictType";
import {MetaDataElement} from "@/services/stdata/typings";
import {MetaDataElementService} from "@/services/stdata/data-element.service";
import {RefdataService} from "@/services/stdata/refdata.service";
import {DictDataService} from "@/services/admin/dictData.service";

const MetaDataElementForm: React.FC<ModalFormProps<MetaDataElement>> = ({
                                                                          data,
                                                                          visible,
                                                                          onVisibleChange,
                                                                          onCancel,
                                                                        }) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.stdata.dataElement'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.stdata.dataElement'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: MetaDataElement = {
            id: values.id,
            elementCode: values.elementCode,
            elementName: values.elementName,
            dataType: values.dataType,
            dataLength: values.dataLength,
            dataPrecision: values.dataPrecision,
            dataScale: values.dataScale,
            nullable: values.nullable,
            dataDefault: values.dataDefault,
            lowValue: values.lowValue,
            highValue: values.highValue,
            dataSetType: {id: values.dataSetType}
          };
          data.id
            ? MetaDataElementService.update(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : MetaDataElementService.add(param).then((response) => {
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
        initialValues={{
          id: data.id,
          elementCode: data.elementCode,
          elementName: data.elementName,
          dataType: data.dataType?.value,
          dataLength: data.dataLength,
          dataPrecision: data.dataPrecision,
          dataScale: data.dataScale,
          nullable: data.nullable?.value,
          dataDefault: data.dataDefault,
          lowValue: data.lowValue,
          highValue: data.highValue,
          dataSetType: data.dataSetType?.id
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormText
          name={"elementCode"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.elementCode'})}
          rules={[{required: true}, {min: 1}, {max: 30}]}
        />
        <ProFormText
          name={"elementName"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.elementName'})}
          rules={[{required: true}, {max: 250}]}
        />
        <ProFormSelect
          name={"dataType"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.dataType'})}
          rules={[{required: true}]}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.dataType)}
        />
        <ProFormDigit
          name={"dataLength"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.dataLength'})}
        />
        <ProFormDigit
          name={"dataPrecision"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.dataPrecision'})}
        />
        <ProFormDigit
          name={"dataScale"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.dataScale'})}
        />
        <ProFormSelect
          name={"nullable"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.nullable'})}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.yesNo)}
        />
        <ProFormText
          name={"dataDefault"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.dataDefault'})}
        />
        <ProFormText
          name={"lowValue"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.lowValue'})}
        />
        <ProFormText
          name={"highValue"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.highValue'})}
        />
        <ProFormSelect
          name={"dataSetType"}
          label={intl.formatMessage({id: 'pages.stdata.dataElement.dataSetType'})}
          request={(params, props) => {
            return RefdataService.listDataSetType({}).then((response) => {
              if (response.data) {
                return response.data.map((item) => {
                  return {value: item.id, label: item.dataSetTypeCode + '-' + item.dataSetTypeName, item: item}
                })
              }
              return []
            })
          }}
        />
      </ProForm>
    </Modal>
  );
}
export default MetaDataElementForm;
