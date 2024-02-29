import {useIntl} from "umi";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {MetaDataSet} from "@/services/stdata/typings";
import {RefdataService} from "@/services/stdata/refdata.service";
import {MetaSystemService} from "@/services/stdata/system.service";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";

const DataSetForm: React.FC<ModalFormProps<MetaDataSet>> = ({
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
          intl.formatMessage({id: 'pages.stdata.dataSet'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.stdata.dataSet'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: MetaDataSet = {
            dataSetType: {id: values.dataSetType},
            dataSetCode: values.dataSetCode,
            dataSetValue: values.dataSetValue,
            system: {id: values.system},
            isStandard: values.isStandard,
            remark: values.remark
          };
          data.id
            ? RefdataService.updateDataSet(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : RefdataService.addDataSet(param).then((response) => {
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
          dataSetType: data.dataSetType?.id,
          dataSetCode: data.dataSetCode,
          dataSetValue: data.dataSetValue,
          system: data.system?.id,
          isStandard: data.isStandard?.value,
          remark: data.remark
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormSelect
          name={"dataSetType"}
          label={intl.formatMessage({id: 'pages.stdata.dataSet.dataSetType'})}
          rules={[{required: true}]}
          request={(params, props) => {
            return RefdataService.listDataSetType({dataSetTypeCode: params.keyWords}).then((response) => {
              if (response.data) {
                return response.data.map((item) => {
                  return {value: item.id, label: item.dataSetTypeCode + '-' + item.dataSetTypeName, item: item}
                })
              }
              return []
            })
          }}
        />
        <ProFormText
          name={"dataSetCode"}
          label={intl.formatMessage({id: 'pages.stdata.dataSet.dataSetCode'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"dataSetValue"}
          label={intl.formatMessage({id: 'pages.stdata.dataSet.dataSetValue'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"system"}
          label={intl.formatMessage({id: 'pages.stdata.dataSet.system'})}
          rules={[{required: true}]}
          allowClear={false}
          request={(params, props) => {
            return MetaSystemService.list({systemName: params.keyWords}).then((response) => {
              if (response.data) {
                return response.data.map((item) => {
                  return {value: item.id, label: item.systemName, item: item}
                })
              }
              return []
            })
          }}
        />
        <ProFormSelect
          name={"isStandard"}
          label={intl.formatMessage({id: 'pages.stdata.dataSet.isStandard'})}
          rules={[{required: true}]}
          allowClear={false}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.yesNo)
          }}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'pages.stdata.remark'})}
        />
      </ProForm>
    </Modal>
  );

}

export default DataSetForm;
