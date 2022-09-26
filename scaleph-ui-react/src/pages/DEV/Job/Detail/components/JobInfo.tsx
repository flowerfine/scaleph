import {ProDescriptions} from "@ant-design/pro-components";
import ButtonGroup from "antd/es/button/button-group";
import {Button} from "antd";
import {
  AreaChartOutlined,
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  DashboardOutlined,
  DeleteOutlined,
  PauseOutlined, UnorderedListOutlined
} from "@ant-design/icons";
import {FlinkJobForJar} from "@/pages/DEV/Job/typings";

const JobInfoForJarWeb: React.FC<{data: FlinkJobForJar}> = ({data}) => {

  return (
    <ProDescriptions
      title={data.name}
      dataSource={data}
      column={4}
      columns={[
        {
          title: 'File Name',
          key: 'fileName',
          render: (dom, entity, index, action, schema) => {
            return entity.flinkArtifactJar?.fileName
          }
        },
        {
          title: 'Entry Class',
          key: 'entryClass',
          span: 3,
          render: (dom, entity, index, action, schema) => {
            return entity.flinkArtifactJar?.entryClass
          }
        },
        {
          title: 'Flink 版本',
          key: 'flinkVersion',
          render: (dom, entity, index, action, schema) => {
            return entity.flinkClusterConfig?.flinkVersion?.label
          }
        },
        {
          title: 'Flink Resource Provider',
          key: 'resourceProvider',
          render: (dom, entity, index, action, schema) => {
            return entity.flinkClusterConfig?.resourceProvider?.label
          }
        },
        {
          title: 'Flink Deploy Mode',
          key: 'deployMode',
          render: (dom, entity, index, action, schema) => {
            return entity.flinkClusterConfig?.deployMode?.label
          }
        }
      ]}
      extra={(
        <div>
          <ButtonGroup>
            <Button icon={<CaretRightOutlined />}>
              Start
            </Button>
            <Button icon={<PauseOutlined/>}>
              Suspend
            </Button>
            <Button icon={<CameraOutlined />}>
              Savepoint
            </Button>
            <Button icon={<CloseOutlined />}>
              Cancel
            </Button>
          </ButtonGroup>
          <ButtonGroup>
            <Button danger icon={<DeleteOutlined />}>
              Delete
            </Button>
          </ButtonGroup>
          <ButtonGroup>
            <Button icon={<DashboardOutlined />}>
              Web-UI
            </Button>
            <Button icon={<AreaChartOutlined />}>
              Metrics
            </Button>
            <Button icon={<UnorderedListOutlined />}>
              Logs
            </Button>
          </ButtonGroup>
        </div>
      )}
    />
  );
}

export default JobInfoForJarWeb;
