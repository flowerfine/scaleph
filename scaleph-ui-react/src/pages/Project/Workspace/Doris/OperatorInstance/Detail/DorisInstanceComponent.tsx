import {useIntl} from "umi";
import React from "react";
import {WsDorisOperatorInstance} from "@/services/project/typings";
import {ProCard, StatisticCard} from "@ant-design/pro-components";
import {Divider, Space, Statistic} from "antd";

const DorisInstanceDetailComponent: React.FC<{ data: WsDorisOperatorInstance }> = ({data}) => {
  const intl = useIntl();

  return (
    <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component'})}
                   direction={'row'}>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.fe'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: data.feSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={data.feStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={data.feStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={data.feStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.feSpec?.image ? data.feSpec?.image : '-'}</div>
                       }/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.be'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: data.beSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={data.beStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={data.beStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={data.beStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.beSpec?.image ? data.beSpec?.image : '-'}</div>
                       }/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.cn'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: data.cnSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={data.cnStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={data.cnStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={data.cnStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.cnSpec?.image ? data.cnSpec?.image : '-'}</div>
                       }/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.broker'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: data.brokerSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={data.brokerStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={data.brokerStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={data.brokerStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.brokerSpec?.image ? data.brokerSpec?.image : '-'}</div>
                       }/>
      </ProCard>
    </ProCard.Group>
  );
}

export default DorisInstanceDetailComponent;
