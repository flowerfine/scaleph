import {useIntl} from "umi";
import React from "react";
import {WsDorisOperatorInstance} from "@/services/project/typings";
import {ProCard} from "@ant-design/pro-components";
import {WsDorisOperatorInstanceService} from "@/services/project/WsDorisOperatorInstanceService";
import {Button, message, Popconfirm} from "antd";
import {CaretRightOutlined, CloseOutlined} from "@ant-design/icons";

const DorisInstanceDetailAction: React.FC<{ data: WsDorisOperatorInstance }> = ({data}) => {
  const intl = useIntl();

  return (
    <ProCard.Group direction={'row'}>
      <ProCard extra={
        <div>
          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            onConfirm={() => {
              WsDorisOperatorInstanceService.deploy(data.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button type="default" icon={<CaretRightOutlined/>}>
              {intl.formatMessage({id: 'pages.project.doris.instance.detail.deploy'})}
            </Button>
          </Popconfirm>


          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            onConfirm={() => {
              WsDorisOperatorInstanceService.shutdown(data.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button icon={<CloseOutlined/>}>
              {intl.formatMessage({id: 'pages.project.doris.instance.detail.shutdown'})}
            </Button>
          </Popconfirm>
        </div>
      }>
        集群信息，敬请期待~
      </ProCard>
    </ProCard.Group>
  );
}

export default DorisInstanceDetailAction;
