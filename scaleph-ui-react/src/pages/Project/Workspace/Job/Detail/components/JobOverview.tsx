import { FlinkJobForJar } from '@/pages/DEV/Job/typings';
import { Card, Col, List, Row } from 'antd';
import { useIntl } from 'umi';

const JobOverviewWeb: React.FC<{
  data: FlinkJobForJar;
}> = ({ data }) => {
  const intl = useIntl();

  return (
    <>
      <Row gutter={[12, 12]}>
        <Col span={12}>
          <Card title={intl.formatMessage({ id: 'pages.dev.job.detail.overview.basic' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.artifactType',
                  })}
                ></List.Item.Meta>
                <div>{data.type}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.jar',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkArtifactJar?.fileName}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.jarPath',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkArtifactJar?.path}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.jarVersion',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkArtifactJar?.version}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.mainClass',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkArtifactJar?.entryClass}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.flinkVersion',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkArtifactJar?.flinkVersion?.label}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.creator',
                  })}
                ></List.Item.Meta>
                <div>{data.creator}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.basic.editor',
                  })}
                ></List.Item.Meta>
                <div>{data.editor}</div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={12}>
          <Card
            title={intl.formatMessage({
              id: 'pages.dev.job.detail.overview.cluster',
            })}
          >
            <List>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.cluster.id',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkClusterInstance?.clusterId}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.cluster.name',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkClusterInstance?.name}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.cluster.status',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkClusterInstance?.status?.label}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.cluster.webui',
                  })}
                ></List.Item.Meta>
                <div>
                  <a target="_blank" href={data.flinkClusterInstance?.webInterfaceUrl}>
                    {data.flinkClusterInstance?.webInterfaceUrl}
                  </a>
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.cluster.deployMode',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkClusterConfig?.deployMode?.label}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta
                  title={intl.formatMessage({
                    id: 'pages.dev.job.detail.overview.cluster.resourceProvider',
                  })}
                ></List.Item.Meta>
                <div>{data.flinkClusterConfig?.resourceProvider?.label}</div>
              </List.Item>
            </List>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default JobOverviewWeb;
