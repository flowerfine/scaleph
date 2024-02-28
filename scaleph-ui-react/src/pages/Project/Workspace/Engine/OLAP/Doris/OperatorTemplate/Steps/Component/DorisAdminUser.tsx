import React from "react";
import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const DorisAdminUser: React.FC = () => {
  const intl = useIntl();

  return (<ProCard
    title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.admin'})}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormText
        name="admin.name"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.admin.name'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"admin"}
      />
      <ProFormText
        name="admin.password"
        label={intl.formatMessage({id: 'pages.project.doris.template.steps.component.admin.password'})}
        colProps={{span: 10, offset: 1}}
        initialValue={"Admin123"}
      />
    </ProFormGroup>
  </ProCard>);
}

export default DorisAdminUser;
