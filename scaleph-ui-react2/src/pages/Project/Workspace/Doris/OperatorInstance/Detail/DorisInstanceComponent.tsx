import React from "react";
import {Divider, Space, Statistic} from "antd";
import {ProCard, StatisticCard} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";

const DorisInstanceDetailComponent: React.FC = (props: any) => {
  const intl = useIntl();

  return (
    <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component'})}
                   direction={'row'}>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.fe'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: props.dorisInstanceDetail.instance?.feSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={props.dorisInstanceDetail.instance?.feStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={props.dorisInstanceDetail.instance?.feStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={props.dorisInstanceDetail.instance?.feStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + props.dorisInstanceDetail.instance?.feSpec?.image ? props.dorisInstanceDetail.instance?.feSpec?.image : '-'}</div>
                       }/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.be'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: props.dorisInstanceDetail.instance?.beSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={props.dorisInstanceDetail.instance?.beStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={props.dorisInstanceDetail.instance?.beStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={props.dorisInstanceDetail.instance?.beStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + props.dorisInstanceDetail.instance?.beSpec?.image ? props.dorisInstanceDetail.instance?.beSpec?.image : '-'}</div>
                       }/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.cn'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: props.dorisInstanceDetail.instance?.cnSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={props.dorisInstanceDetail.instance?.cnStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={props.dorisInstanceDetail.instance?.cnStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={props.dorisInstanceDetail.instance?.cnStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + props.dorisInstanceDetail.instance?.cnSpec?.image ? props.dorisInstanceDetail.instance?.cnSpec?.image : '-'}</div>
                       }/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard bordered hoverable>
        <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.broker'})}
                       statistic={{
                         prefix: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}) + ': ',
                         value: props.dorisInstanceDetail.instance?.brokerSpec?.replicas,
                         description: (
                           <Space>
                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'})}
                               value={props.dorisInstanceDetail.instance?.brokerStatus?.creatingInstances?.length}
                               status={"processing"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'})}
                               value={props.dorisInstanceDetail.instance?.brokerStatus?.runningInstances?.length}
                               status={"success"}/>

                             <Statistic
                               title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'})}
                               value={props.dorisInstanceDetail.instance?.brokerStatus?.failedInstances?.length}
                               status={"error"}/>
                           </Space>
                         )
                       }}
                       footer={
                         <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + props.dorisInstanceDetail.instance?.brokerSpec?.image ? props.dorisInstanceDetail.instance?.brokerSpec?.image : '-'}</div>
                       }/>
      </ProCard>
    </ProCard.Group>
  );
}

const mapModelToProps = ({dorisInstanceDetail}: any) => ({dorisInstanceDetail})
export default connect(mapModelToProps)(DorisInstanceDetailComponent);
