import {useIntl} from "@umijs/max";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDependency, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/typings';
import {MetaDataMap} from "@/services/stdata/typings";
import {RefdataService} from "@/services/stdata/refdata.service";

const RefDataMapForm: React.FC<ModalFormProps<MetaDataMap>> = ({
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
          intl.formatMessage({id: 'pages.stdata.dataMap'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.stdata.dataMap'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: MetaDataMap = {
            srcDataSetId: values.srcDataSetId,
            tgtDataSetId: values.tgtDataSetId
          };
          data.id
            ? RefdataService.updateDataMap(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : RefdataService.addDataMap(param).then((response) => {
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
        <ProFormSelect
          name={"srcDataSetTypeId"}
          label={intl.formatMessage({id: 'pages.stdata.dataMap.srcDataSetTypeCode'})}
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
        <ProFormDependency name={["srcDataSetTypeId"]}>
          {({ srcDataSetTypeId }) => {
            return <ProFormSelect
              name={"srcDataSetId"}
              label={intl.formatMessage({id: 'pages.stdata.dataMap.srcDataSetId'})}
              rules={[{required: true}]}
              disabled={!srcDataSetTypeId}
              dependencies={["srcDataSetTypeId"]}
              request={(params, props) => {
                console.log('params', params)
                if (params.srcDataSetTypeId) {
                  return RefdataService.listDataSetByType(params.srcDataSetTypeId).then((response) => {
                    if (response.data) {
                      return response.data.map((item) => {
                        return {value: item.id, label: item.dataSetCode + '-' + item.dataSetValue, item: item}
                      })
                    }
                    return []
                  })
                }
                return Promise.resolve([])
              }}
            />;
          }}
        </ProFormDependency>

        <ProFormSelect
          name={"tgtDataSetTypeId"}
          label={intl.formatMessage({id: 'pages.stdata.dataMap.srcDataSetTypeCode'})}
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
        <ProFormDependency name={["tgtDataSetTypeId"]}>
          {({ tgtDataSetTypeId }) => {
            return <ProFormSelect
              name={"tgtDataSetId"}
              label={intl.formatMessage({id: 'pages.stdata.dataMap.tgtDataSetId'})}
              rules={[{required: true}]}
              disabled={!tgtDataSetTypeId}
              dependencies={["tgtDataSetTypeId"]}
              request={(params, props) => {
                if (params.tgtDataSetTypeId) {
                  return RefdataService.listDataSetByType(params.tgtDataSetTypeId).then((response) => {
                    if (response.data) {
                      return response.data.map((item) => {
                        return {value: item.id, label: item.dataSetCode + '-' + item.dataSetValue, item: item}
                      })
                    }
                    return []
                  })
                }
                return Promise.resolve([])
              }}
            />;
          }}
        </ProFormDependency>
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'pages.stdata.remark'})}
        />
      </ProForm>
    </Modal>
  );

}
export default RefDataMapForm;
