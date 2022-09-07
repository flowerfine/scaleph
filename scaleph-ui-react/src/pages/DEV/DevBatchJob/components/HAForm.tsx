import {useIntl} from "@@/exports";
import {ProForm, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

const HAForm: React.FC = () => {
  const intl = useIntl();
  return (<div>
    <ProForm.Group
      title={"High Availability"}
      collapsible={true}
      defaultCollapsed={true}
    >
      <ProFormSelect
        name="high-availability"
        label={"high-availability"}
        colProps={{span: 8, offset: 2}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkHA)}
      />
      <ProFormText
        name="high-availability.storageDir"
        label={'high-availability.storageDir'}
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
      <ProFormText
        name="high-availability.zookeeper.path.root"
        label={'high-availability.zookeeper.path.root'}
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
      <ProFormText
        name="high-availability.cluster-id"
        label={'high-availability.cluster-id'}
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
      <ProFormText
        name="high-availability.zookeeper.quorum"
        label={'high-availability.zookeeper.quorum'}
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
    </ProForm.Group>
  </div>)
}

export default HAForm;
