import React, {useRef} from "react";
import {ActionType, ProCard, ProDescriptions, ProFormInstance} from "@ant-design/pro-components";
import {connect, useAccess, useIntl} from "@umijs/max";
import {Props} from '@/typings';
import {WsArtifactFlinkJar, WsFlinkKubernetesJob} from "@/services/project/typings";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
import {Button, message, Popconfirm} from "antd";
import {
  AreaChartOutlined,
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  DashboardOutlined, OrderedListOutlined,
  RedoOutlined
} from "@ant-design/icons";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const ArtifactFlinkJarWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();


  const descriptionColumns: ProDescriptionsItemProps<WsArtifactFlinkJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.artifact.name'}),
      key: `name`,
      renderText: (text: any, record: WsArtifactFlinkJar, index: number, action: ProCoreActionType) => {
        return record.artifact?.name
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.jar.fileName'}),
      key: `fileName`,
      dataIndex: 'fileName'
    },
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      key: `flinkVersion`,
      dataIndex: 'flinkVersion',
      renderText: (text: any, record: WsArtifactFlinkJar, index: number, action: ProCoreActionType) => {
        return record.flinkVersion?.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.jar.entryClass'}),
      key: `entryClass`,
      dataIndex: 'entryClass'
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.jar.jarParams'}),
      key: `jarParams`,
      dataIndex: 'jarParams',
      valueType: 'jsonCode'
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      key: `remark`,
      dataIndex: 'remark',
      renderText: (text: any, record: WsArtifactFlinkJar, index: number, action: ProCoreActionType) => {
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
      dataSource={props.flinkKubernetesJobDetail.job?.artifactFlinkJar}
      columns={descriptionColumns}
    />
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(ArtifactFlinkJarWeb);
