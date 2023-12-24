import {connect, useIntl} from "umi";
import React from "react";
import {ProCard, ProDescriptions} from "@ant-design/pro-components";
import {WsDorisOperatorInstanceService} from "@/services/project/WsDorisOperatorInstanceService";
import {Button, message, Popconfirm, Space} from "antd";
import {
  AreaChartOutlined,
  CaretRightOutlined,
  CloseOutlined,
  DashboardOutlined,
  OrderedListOutlined
} from "@ant-design/icons";
import {YesOrNo} from "@/constants/enum";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const DorisInstanceDetailAction: React.FC = (props: any) => {
  const intl = useIntl();

  const descriptionColumns: ProDescriptionsItemProps<WsFlinkKubernetesJob>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.doris.instance.name'}),
      key: `name`,
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.project.doris.instance.instanceId'}),
      key: `instanceId`,
      dataIndex: 'instanceId',
    },
    {
      title: intl.formatMessage({id: 'pages.project.doris.instance.namespace'}),
      key: `namespace`,
      dataIndex: 'namespace',
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      key: `remark`,
      dataIndex: 'remark',
    },
    {
      title: intl.formatMessage({id: 'app.common.data.createTime'}),
      key: `createTime`,
      dataIndex: 'createTime',
    },
    {
      title: intl.formatMessage({id: 'app.common.data.updateTime'}),
      key: `updateTime`,
      dataIndex: 'updateTime',
    },
  ]

  return (
    <ProCard.Group direction={'row'}>
      <ProCard extra={(
        <Space>
          <div>
            <Popconfirm
              title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
              disabled={props.dorisInstanceDetail.instance?.deployed?.value == YesOrNo.YES}
              onConfirm={() => {
                WsDorisOperatorInstanceService.deploy(props.dorisInstanceDetail.instance?.id).then(response => {
                  if (response.success) {
                    message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                  }
                })
              }}
            >
              <Button
                type="default"
                icon={<CaretRightOutlined/>}
                disabled={props.dorisInstanceDetail.instance?.deployed?.value == YesOrNo.YES}
              >
                {intl.formatMessage({id: 'pages.project.doris.instance.detail.deploy'})}
              </Button>
            </Popconfirm>
            <Popconfirm
              title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
              disabled={props.dorisInstanceDetail.instance?.deployed?.value == YesOrNo.NO}
              onConfirm={() => {
                WsDorisOperatorInstanceService.shutdown(props.dorisInstanceDetail.instance?.id).then(response => {
                  if (response.success) {
                    message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                  }
                })
              }}
            >
              <Button
                icon={<CloseOutlined/>}
                disabled={props.dorisInstanceDetail.instance?.deployed?.value == YesOrNo.NO}
              >
                {intl.formatMessage({id: 'pages.project.doris.instance.detail.shutdown'})}
              </Button>
            </Popconfirm>
          </div>

          <div>
            <Button
              type="default"
              icon={<DashboardOutlined/>}
              disabled={props.dorisInstanceDetail.instance?.deployed?.value == YesOrNo.NO}
              onClick={() => {
                WsDorisOperatorInstanceService.feEndpoint(props.dorisInstanceDetail.instance?.id).then((response) => {
                  if (response.data?.http) {
                    const a = document.createElement('a');
                    a.href = response.data?.http;
                    a.target = "_blank";
                    a.click();
                  }
                });
              }}
            >
              {intl.formatMessage({id: 'pages.project.doris.instance.detail.fe'})}
            </Button>
          </div>

          <div>
            <Button
              type="default"
              icon={<AreaChartOutlined/>}
              disabled
            >
              {intl.formatMessage({id: 'pages.project.doris.instance.detail.metrics'})}
            </Button>
            <Button
              type="default"
              icon={<OrderedListOutlined/>}
              disabled
            >
              {intl.formatMessage({id: 'pages.project.doris.instance.detail.logs'})}
            </Button>
          </div>
        </Space>

      )}>
        <ProDescriptions
          column={2}
          dataSource={props.dorisInstanceDetail.instance}
          columns={descriptionColumns}
        />
      </ProCard>
    </ProCard.Group>
  );
}

const mapModelToProps = ({dorisInstanceDetail}: any) => ({dorisInstanceDetail})
export default connect(mapModelToProps)(DorisInstanceDetailAction);
