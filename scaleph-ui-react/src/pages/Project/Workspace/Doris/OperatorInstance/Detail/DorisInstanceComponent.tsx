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
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.fe'})}
                       statistic={{
                         value: data.feSpec?.replicas,
                         prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                               value={data.feSpec?.requests?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                               value={data.feSpec?.requests?.memory}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                               value={data.feSpec?.limits?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                               value={data.feSpec?.limits?.memory}/>
                           </Space>
                         ),
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.feSpec?.image ? data.feSpec?.image : '-'}</div>
                       }
        />
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.be'})}
                       statistic={{
                         value: data.beSpec?.replicas,
                         prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                               value={data.beSpec?.requests?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                               value={data.beSpec?.requests?.memory}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                               value={data.beSpec?.limits?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                               value={data.beSpec?.limits?.memory}/>
                           </Space>
                         ),
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.beSpec?.image ? data.beSpec?.image : '-'}</div>
                       }
        />
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.cn'})}
                       statistic={{
                         value: data.cnSpec?.replicas,
                         prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                               value={data.cnSpec?.requests?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                               value={data.cnSpec?.requests?.memory}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                               value={data.cnSpec?.limits?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                               value={data.cnSpec?.limits?.memory}/>
                           </Space>
                         ),
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.cnSpec?.image ? data.cnSpec?.image : '-'}</div>
                       }
        />
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.broker'})}
                       statistic={{
                         value: data.brokerSpec?.replicas,
                         prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                               value={data.brokerSpec?.requests?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                               value={data.brokerSpec?.requests?.memory}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                               value={data.brokerSpec?.limits?.cpu}/>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                               value={data.brokerSpec?.limits?.memory}/>
                           </Space>
                         ),
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.brokerSpec?.image ? data.brokerSpec?.image : '-'}</div>
                       }
        />
      </ProCard>
    </ProCard.Group>
  );
}

export default DorisInstanceDetailComponent;
