import {useIntl} from "umi";
import React from "react";
import {WsDorisOperatorInstance} from "@/services/project/typings";
import {ProCard} from "@ant-design/pro-components";

const DorisInstanceDetailAccess: React.FC<{ data: WsDorisOperatorInstance }> = ({data}) => {
  const intl = useIntl();

  return (
    <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.access'})}
                   direction={'row'}>
      <ProCard bordered>
      集群连接信息，敬请期待~
      </ProCard>
      </ProCard.Group>
      );
    }

export default DorisInstanceDetailAccess;
