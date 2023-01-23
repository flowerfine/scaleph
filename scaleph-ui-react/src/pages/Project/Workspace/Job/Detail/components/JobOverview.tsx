import { FlinkArtifactJarService } from '@/services/project/flinkArtifactJar.service';
import { WsFlinkArtifactJar, WsFlinkJob } from '@/services/project/typings';
import { WsDiJobService } from '@/services/project/WsDiJob.service';
import Editor, { useMonaco } from '@monaco-editor/react';
import { Card, Col, List, Row } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const JobOverviewWeb: React.FC<{
  data: WsFlinkJob;
}> = ({ data }) => {
  const intl = useIntl();
  const [flinkArtifactJar, setFlinkArtifactJar] = useState<WsFlinkArtifactJar>({});
  const [seatunnelJob, setSeatunnelJob] = useState<String>('');
  // const monaco = useMonaco();
  useEffect(() => {
    if (data.type.value == '0') {
      //jar
      FlinkArtifactJarService.selectOne(data.flinkArtifactId + '').then((d) => {
        setFlinkArtifactJar(d);
      });
    } else if (data.type.value == '1') {
      //sql todo
    } else if (data.type.value == '2') {
      //seatunnel
      WsDiJobService.previewJob(data.flinkArtifactId as number).then((d) => {
        setSeatunnelJob(d.data + '');
      });
    }
  }, []);

  return (
    <>
      <Row gutter={[12, 12]}>
        <Col span={12}>
          {data.type.value == '0' && (
            <Card title={intl.formatMessage({ id: 'pages.project.job.detail.overview.basic' })}>
              <List itemLayout="horizontal">
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.artifactType',
                    })}
                  ></List.Item.Meta>
                  <div>{data.type.label}</div>
                </List.Item>
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.jar',
                    })}
                  ></List.Item.Meta>
                  <div>{flinkArtifactJar.fileName}</div>
                </List.Item>
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.jarPath',
                    })}
                  ></List.Item.Meta>
                  <div>{flinkArtifactJar.path}</div>
                </List.Item>
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.jarVersion',
                    })}
                  ></List.Item.Meta>
                  <div>{flinkArtifactJar.version}</div>
                </List.Item>
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.mainClass',
                    })}
                  ></List.Item.Meta>
                  <div>{flinkArtifactJar.entryClass}</div>
                </List.Item>
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.flinkVersion',
                    })}
                  ></List.Item.Meta>
                  <div>{flinkArtifactJar.flinkVersion?.label}</div>
                </List.Item>
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.creator',
                    })}
                  ></List.Item.Meta>
                  <div>{data.creator}</div>
                </List.Item>
                <List.Item>
                  <List.Item.Meta
                    title={intl.formatMessage({
                      id: 'pages.project.job.detail.overview.basic.editor',
                    })}
                  ></List.Item.Meta>
                  <div>{data.editor}</div>
                </List.Item>
              </List>
            </Card>
          )}
          {data.type.value == '2' && (
            <Card title={intl.formatMessage({ id: 'pages.project.job.detail.overview.basic' })}>
              <Editor
                language="json"
                value={seatunnelJob + ''}
                height="480px"
                width="100%"
                options={{ readOnly: true, selectOnLineNumbers: true, minimap: {enabled: false} }}
              ></Editor>
            </Card>
          )}
        </Col>
        <Col span={12}>
          <Card
            title={intl.formatMessage({
              id: 'pages.project.job.detail.overview.cluster',
            })}
          >
            <List>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.project.job.detail.overview.cluster.id',
                  })}
                ></List.Item.Meta>
                <div>{data.wsFlinkClusterInstance?.clusterId}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.project.job.detail.overview.cluster.name',
                  })}
                ></List.Item.Meta>
                <div>{data.wsFlinkClusterInstance?.name}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.project.job.detail.overview.cluster.status',
                  })}
                ></List.Item.Meta>
                <div>{data.wsFlinkClusterInstance?.status?.label}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.project.job.detail.overview.cluster.webui',
                  })}
                ></List.Item.Meta>
                <div>
                  <a target="_blank" href={data.wsFlinkClusterInstance?.webInterfaceUrl}>
                    {data.wsFlinkClusterInstance?.webInterfaceUrl}
                  </a>
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.project.job.detail.overview.cluster.deployMode',
                  })}
                ></List.Item.Meta>
                <div>{data.wsFlinkClusterConfig?.deployMode?.label}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.project.job.detail.overview.cluster.resourceProvider',
                  })}
                ></List.Item.Meta>
                <div>{data.wsFlinkClusterConfig?.resourceProvider?.label}</div>
              </List.Item>
            </List>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default JobOverviewWeb;
