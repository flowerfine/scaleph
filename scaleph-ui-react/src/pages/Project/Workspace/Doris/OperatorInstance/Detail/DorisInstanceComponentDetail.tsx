import {useIntl} from "umi";
import React from "react";
import {Divider} from "antd";
import {StatisticCard} from "@ant-design/pro-components";
import {WsDorisOperatorInstance} from "@/services/project/typings";

const DorisInstanceDetailComponentDetail: React.FC<{ data: WsDorisOperatorInstance }> = ({data}) => {
  const intl = useIntl();

  return (
    <StatisticCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.component.fe'})}
                         direction={'row'}>
      <StatisticCard bordered
                     statistic={{
                       title: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.replicas'}),
                       value: data.feSpec?.replicas
                     }}
      />
      <Divider type={"vertical"}/>
      <StatisticCard bordered
                     statistic={{
                       title: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.creating'}),
                       value: data.feStatus?.creatingInstances?.length,
                       status: "processing",
                       style: {width: 120}
                     }}
      />
      <Divider type={"vertical"}/>
      <StatisticCard bordered
                     statistic={{
                       title: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.running'}),
                       value: data.feStatus?.runningInstances?.length,
                       status: "success",
                       style: {width: 120}
                     }}
      />
      <Divider type={"vertical"}/>
      <StatisticCard bordered
                     statistic={{
                       title: intl.formatMessage({id: 'pages.project.doris.instance.detail.component.failed'}),
                       value: data.feStatus?.failedInstances?.length,
                       status: "error",
                       style: {width: 120}
                     }}
      />
    </StatisticCard.Group>
  );
}

export default DorisInstanceDetailComponentDetail;
