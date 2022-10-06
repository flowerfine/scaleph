import {Button, Col, Row, Tabs} from "antd";
import {
  ContainerOutlined,
  FileZipOutlined,
  ProjectOutlined,
  RollbackOutlined,
  SaveOutlined,
  ToolOutlined
} from "@ant-design/icons";
import {history} from "umi";
import JobInfoForJarWeb from "@/pages/DEV/Job/Detail/components/JobInfoForJar";
import JobSavepointsWeb from "@/pages/DEV/Job/Detail/components/JobSavepointsTab";
import JobArtifactForJarWeb from "@/pages/DEV/Job/Detail/components/JobArtifactForJar";
import JobConfigurationWeb from "@/pages/DEV/Job/Detail/components/JobConfiguration";
import JobOverviewWeb from "@/pages/DEV/Job/Detail/components/JobOverview";
import {useIntl, useLocation} from "@@/exports";
import {FlinkJobForJar} from "@/pages/DEV/Job/typings";
import JobHistoryWeb from "@/pages/DEV/Job/Detail/components/JobHistoryTab";

const JobDetailWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const params = urlParams.state as FlinkJobForJar;

  return (<div>
    <Row>
      <Col>
        <Button onClick={() => history.back()}>
          <RollbackOutlined/>
        </Button>
      </Col>
    </Row>
    <Row>
      <Col span={23}>
        <JobInfoForJarWeb data={params}/>
      </Col>
    </Row>
    <Row>
      <Col>
        <Tabs
          defaultActiveKey="1"
          items={[
            {
              label: (<span><ProjectOutlined/>Overview</span>),
              key: "1",
              disabled: false,
              children: (<JobOverviewWeb data={params.flinkJobInstance ? params.flinkJobInstance : {}}/>),
            },
            {
              label: (<span><FileZipOutlined/>Artifact</span>),
              key: "3",
              disabled: false,
              children: (<JobArtifactForJarWeb data={params.flinkArtifactJar ? params.flinkArtifactJar : {}}/>),
            },
            {
              label: (<span><ToolOutlined/>Configuration</span>),
              key: "2",
              disabled: false,
              children: (<JobConfigurationWeb
                clusterConfig={params.flinkClusterConfig ? params.flinkClusterConfig : {}}
                clusterInstance={params.flinkClusterInstance ? params.flinkClusterInstance : {}}
                flinkConfig={params.flinkConfig}/>),
            },
            {
              label: (<span><ContainerOutlined/>Jobs</span>),
              key: "4",
              disabled: false,
              children: (<JobHistoryWeb flinkJobCode={params.code ? params.code : 0}/>),
            },
            {
              label: (<span><SaveOutlined/>Savepoints</span>),
              key: "5",
              disabled: false,
              children: (<JobSavepointsWeb/>),
            }
          ]}
        />
      </Col>
    </Row>
  </div>);
}

export default JobDetailWeb
