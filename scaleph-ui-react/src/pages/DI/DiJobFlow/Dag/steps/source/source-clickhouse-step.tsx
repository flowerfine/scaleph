import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from '@/constant';
import {DictDataService} from '@/services/admin/dictData.service';
import {DataSourceService} from '@/services/project/dataSource.service';
import {JobService} from '@/services/project/job.service';
import {DiJob, MetaDataSourceParam} from '@/services/project/typings';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {ClickHouseParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
import {ProForm, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";

const SourceClickHouseStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as DiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
  }, []);

  return (
    <Modal
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let map: Map<string, string> = new Map();
          map.set(STEP_ATTR_TYPE.jobId, jobInfo.id + '');
          map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
          map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
          map.set(STEP_ATTR_TYPE.stepAttrs, form.getFieldsValue());
          JobService.saveStepAttr(map).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.success'}));
              onCancel();
              onOK ? onOK() : null;
            }
          });
        });
      }}
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <ProFormSelect
          name={"dataSourceType"}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSourceType'})}
          colProps={{span: 6}}
          fieldProps={{
            defaultValue: "ClickHouse",
            disabled: true
          }}
          request={(() => {
            return DictDataService.listDictDataByType(DICT_TYPE.datasourceType);
          })}
        />
        <ProFormSelect
          name={STEP_ATTR_TYPE.dataSource}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSource'})}
          rules={[{required: true}]}
          colProps={{span: 18}}
          dependencies={["dataSourceType"]}
          request={((params, props) => {
            const param: MetaDataSourceParam = {
              datasourceName: params.keyWords,
              datasourceType: "ClickHouse",
            };
            return DataSourceService.listDataSourceByPage(param).then((response) => {
              return response.data.map((item) => {
                return {label: item.datasourceName, value: item.id, item: item};
              });
            });
          })}
        />

        <ProFormText
          name={ClickHouseParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.database'})}
        />

        <ProFormTextArea
          name={ClickHouseParams.sql}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.sql'})}
        />

      </ProForm>
    </Modal>
  );
};

export default SourceClickHouseStepForm;
