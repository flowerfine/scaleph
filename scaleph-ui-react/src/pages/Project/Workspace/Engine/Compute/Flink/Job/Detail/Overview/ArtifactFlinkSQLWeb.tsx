import React from "react";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
import {ProDescriptions} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";
import {WsArtifactFlinkSql} from "@/services/project/typings";

const ArtifactFlinkSQLWeb: React.FC = (props: any) => {
  const intl = useIntl();

  const descriptionColumns: ProDescriptionsItemProps<WsArtifactFlinkSql>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.artifact.name'}),
      key: `name`,
      renderText: (text: any, record: WsArtifactFlinkSql, index: number, action: ProCoreActionType) => {
        return record.artifact?.name
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.sql'}),
      key: `script`,
      dataIndex: 'script',
      renderText: (text: any, record: WsArtifactFlinkSql, index: number, action: ProCoreActionType) => {
        return record.script
      }
    },
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      key: `flinkVersion`,
      dataIndex: 'flinkVersion',
      renderText: (text: any, record: WsArtifactFlinkSql, index: number, action: ProCoreActionType) => {
        return record.flinkVersion?.label
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      key: `remark`,
      dataIndex: 'remark',
      renderText: (text: any, record: WsArtifactFlinkSql, index: number, action: ProCoreActionType) => {
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
      dataSource={props.flinkKubernetesJobDetail.job?.artifactFlinkSql}
      columns={descriptionColumns}
    />
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(ArtifactFlinkSQLWeb);
