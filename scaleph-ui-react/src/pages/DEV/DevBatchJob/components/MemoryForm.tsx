import {useIntl} from "@@/exports";
import {ProForm, ProFormText} from "@ant-design/pro-components";

const MemoryForm: React.FC = () => {
  const intl = useIntl();
  return (<div>
    <ProForm.Group
      title={"Memory Configuration"}
      collapsible={true}
      defaultCollapsed={true}
    >
      <ProFormText
        name="jobmanager.memory.process.size"
        label={'jobmanager.memory.process.size'}
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
        name="jobmanager.memory.flink.size"
        label={'jobmanager.memory.flink.size'}
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
        name="taskmanager.memory.process.size"
        label={'taskmanager.memory.process.size'}
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
        name="taskmanager.memory.flink.size"
        label={'taskmanager.memory.flink.size'}
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

export default MemoryForm;
