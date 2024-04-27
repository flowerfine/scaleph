import {useAccess, useIntl} from '@umijs/max';
import {PageContainer} from "@ant-design/pro-components";
import React from "react";
import OamComponentDefinitionWeb from "@/pages/OAM/Definition/ComponentDefinition";

const OamDefinitionWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  const items = [
    {
      label: intl.formatMessage({id: 'pages.oam.definition.component'}),
      key: 'component',
      children: <OamComponentDefinitionWeb/>
    },
    {
      label: intl.formatMessage({id: 'pages.oam.definition.policy'}),
      key: 'policy',
      children: <OamComponentDefinitionWeb/>
    },
    {
      label: intl.formatMessage({id: 'pages.oam.definition.trait'}),
      key: 'trait',
      children: <OamComponentDefinitionWeb/>
    }
  ]

  return (
    <PageContainer
      title={false}
      tabList={[
        {
          key: "component",
          tab: "ComponentDefinition"
        },
        {
          key: "policy",
          tab: "PolicyDefinition"
        },
        {
          key: "trait",
          tab: "TraitDefinition"
        }
      ]}
      tabProps={{
        type: 'card',
        tabPosition: "top",
        items: items
      }}
    >
    </PageContainer>
  );
};

export default OamDefinitionWeb;
