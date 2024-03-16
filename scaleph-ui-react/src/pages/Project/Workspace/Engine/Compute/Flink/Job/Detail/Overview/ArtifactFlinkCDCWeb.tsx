import React from "react";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
import {ProDescriptions} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";
import {WsArtifactFlinkCDC, WsArtifactFlinkSql} from "@/services/project/typings";

const ArtifactFlinkCDCWeb: React.FC = (props: any) => {
  const intl = useIntl();

  const descriptionColumns: ProDescriptionsItemProps<WsArtifactFlinkCDC>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.artifact.name'}),
      key: `name`,
      renderText: (text: any, record: WsArtifactFlinkCDC, index: number, action: ProCoreActionType) => {
        return record.artifact?.name
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.cdc.flinkCDCVersion'}),
      key: `flinkCDCVersion`,
      dataIndex: 'flinkCDCVersion',
      renderText: (text: any, record: WsArtifactFlinkCDC, index: number, action: ProCoreActionType) => {
        return record.flinkCDCVersion?.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      key: `flinkVersion`,
      dataIndex: 'flinkVersion',
      renderText: (text: any, record: WsArtifactFlinkCDC, index: number, action: ProCoreActionType) => {
        return record.flinkVersion?.label
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      key: `remark`,
      dataIndex: 'remark',
      renderText: (text: any, record: WsArtifactFlinkCDC, index: number, action: ProCoreActionType) => {
        return record.artifact?.remark
      }
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
    }
  ]

  return (
    <ProDescriptions
      column={2}
      bordered={true}
      dataSource={props.flinkKubernetesJobDetail.job?.artifactFlinkCDC}
      columns={descriptionColumns}
    />
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(ArtifactFlinkCDCWeb);
