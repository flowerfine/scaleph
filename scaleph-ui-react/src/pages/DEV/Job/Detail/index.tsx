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
import JobInfoForJarWeb from "@/pages/DEV/Job/Detail/components/JobInfo";
import JobInstanceWeb from "@/pages/DEV/Job/Detail/components/JobInstanceTab";
import JobSavepointsWeb from "@/pages/DEV/Job/Detail/components/JobSavepointsTab";
import JobArtifactForJarWeb from "@/pages/DEV/Job/Detail/components/JobArtifactForJar";
import JobConfigurationWeb from "@/pages/DEV/Job/Detail/components/JobConfiguration";
import JobOverviewWeb from "@/pages/DEV/Job/Detail/components/JobOverview";

const JobDetailWeb: React.FC = () => {

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
        <JobInfoForJarWeb/>
      </Col>
    </Row>
    <Row>
      <Col>
        <Tabs
          defaultActiveKey="1"
          items={[
            {
              label: (<span><ProjectOutlined />Overview</span>),
              key: "1",
              disabled: false,
              children: (<JobOverviewWeb/>),
            },
            {
              label: (<span><ToolOutlined />Configuration</span>),
              key: "2",
              disabled: false,
              children: (<JobConfigurationWeb/>),
            },
            {
              label: (<span><FileZipOutlined />Artifact</span>),
              key: "3",
              disabled: false,
              children: (<JobArtifactForJarWeb/>),
            },
            {
              label: (<span><ContainerOutlined/>Jobs</span>),
              key: "4",
              disabled: false,
              children: (<JobInstanceWeb/>),
            },
            {
              label: (<span><SaveOutlined />Savepoints</span>),
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
