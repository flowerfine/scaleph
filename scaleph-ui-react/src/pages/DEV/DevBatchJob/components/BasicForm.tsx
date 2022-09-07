import {Form} from "antd";
import {ProCard, ProForm, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "@@/exports";
import {ResourceListParam} from "@/services/resource/typings";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE, RESOURCE_TYPE} from "@/constant";
import {list} from "@/services/resource/resource.service";
import styles from './style.less';

const BasicForm: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();

  // [key:string]:any

  return (<div>
    <ProForm.Group
      title={"Basic"}
      collapsible={true}
    >
      <ProCard
        className={styles.card}
      >
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.name'})}
          colProps={{span: 8, offset: 2}}
          rules={[
            {required: true},
            {max: 30},
            {
              pattern: /^[\w\s_]+$/,
              message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
            },
          ]}
        />
        <ProFormSelect
          name="deployMode"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
          colProps={{span: 8, offset: 2}}
          rules={[{required: true}]}
          showSearch={true}
          allowClear={true}
          request={() => listDictDataByType(DICT_TYPE.flinkDeploymentMode)}
        />
        <ProFormSelect
          name="flinkVersion"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkVersion'})}
          colProps={{span: 8, offset: 2}}
          rules={[{required: true}]}
          showSearch={true}
          allowClear={true}
          request={() => listDictDataByType(DICT_TYPE.flinkVersion)}
        />
        <ProFormSelect
          name="flinkRelease"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'})}
          colProps={{span: 8, offset: 2}}
          rules={[{required: true}]}
          showSearch={true}
          allowClear={true}
          dependencies={['flinkVersion']}
          request={(params) => {
            const resourceParam: ResourceListParam = {
              resourceType: RESOURCE_TYPE.flinkRelease,
              label: params.flinkVersion
            }
            return list(resourceParam).then((response) => response.records)
          }}
          fieldProps={{
            optionItemRender(item) {
              return item.name;
            },
          }}
        />

        <ProFormSelect
          name="resourceProvider"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'})}
          colProps={{span: 8, offset: 2}}
          rules={[{required: true}]}
          showSearch={true}
          allowClear={true}
          request={() => listDictDataByType(DICT_TYPE.flinkResourceProvider)}
        />
        <ProFormSelect
          name="clusterCredential"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'})}
          colProps={{span: 8, offset: 2}}
          rules={[{required: true}]}
          showSearch={true}
          allowClear={true}
          dependencies={['resourceProvider']}
          request={(params) => {
            const resourceParam: ResourceListParam = {
              resourceType: RESOURCE_TYPE.clusterCredential,
              label: params.resourceProvider
            }
            return list(resourceParam).then((response) => response.records)
          }}
          fieldProps={{
            optionItemRender(item) {
              return item.name;
            },
          }}
        />
        <ProFormText
          name="remark"
          label={intl.formatMessage({id: 'pages.dev.remark'})}
          colProps={{span: 8, offset: 2}}
          rules={[{ max: 200 }]}
        />
      </ProCard>
    </ProForm.Group>
  </div>)
}

export default BasicForm;
