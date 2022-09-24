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

const JobInfoForJarWeb: React.FC = () => {

  return (
    <ProDescriptions
      title="任务信息"
      request={async () => {
        return Promise.resolve({
          success: true,
          data: {
            id: 1,
            name: 'TopSpeedWindowing',
            resourceProvider: 'Native Kubernetes',
            deployMode: 'Application',
            date: '2022-09-23 12:01:35',
          },
        });
      }}
      columns={[
        {
          title: '名称',
          key: 'name',
          dataIndex: 'name',
        },
        {
          title: 'Entry Class',
          key: 'entryClass',
          dataIndex: 'entryClass',
        },
        {
          title: 'version',
          key: 'version',
          dataIndex: 'version',
        },
        {
          title: 'File Name',
          key: 'fileName',
          dataIndex: 'fileName',
        },
        {
          title: 'Flink 版本',
          key: 'flinkVersion',
          dataIndex: 'flinkVersion',
        },
        {
          title: 'Flink Resource Provider',
          key: 'resourceProvider',
          dataIndex: 'resourceProvider',
        },
        {
          title: 'Flink Deploy Mode',
          key: 'deployMode',
          dataIndex: 'deployMode',
        },
        {
          title: '时间',
          key: 'date',
          dataIndex: 'date'
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
