import {useIntl} from "umi";
import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";

const JobBase: React.FC = () => {
  const intl = useIntl();

  return (<ProCard>
    <ProFormGroup>
      <ProFormText
        name="name"
        label={intl.formatMessage({id: 'pages.dev.job.name'})}
        colProps={{span: 21, offset: 1}}
        rules={[{required: true}, {max: 128}]}/>
      <ProFormText
        name="remark"
        label={intl.formatMessage({id: 'pages.dev.remark'})}
        colProps={{span: 21, offset: 1}}
        rules={[{max: 200}]}/>
    </ProFormGroup>
  </ProCard>);
}

export default JobBase;
