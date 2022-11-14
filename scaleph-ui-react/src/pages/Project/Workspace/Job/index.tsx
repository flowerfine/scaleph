import { PRIVILEGE_CODE } from '@/constant';
import { ProList } from '@ant-design/pro-components';
import {
  Button,
  Card,
  Col,
  Divider,
  Form,
  Input,
  PageHeader,
  Pagination,
  Row,
  Space,
  Tag,
  Typography,
} from 'antd';
import { values } from 'lodash';
import { useIntl, useAccess, history } from 'umi';

const JobListView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <>
      <div style={{ backgroundColor: '#ffffff' }}>
        <PageHeader
          title={intl.formatMessage({ id: 'pages.project.job.list' })}
          extra={
            <>
              {access.canAccess(PRIVILEGE_CODE.datadevProjectAdd) && (
                <Button
                  key="new"
                  type="primary"
                  onClick={() => {
                    // setProjectFormData({ visible: true, data: {} });
                  }}
                >
                  {intl.formatMessage({ id: 'pages.project.job.create' })}
                </Button>
              )}
            </>
          }
        >
          <Row gutter={[12, 12]}>
            <Col span={8}>
              <Input addonBefore="作业编码"></Input>
            </Col>
            <Col span={8}>
              <Input addonBefore="作业类型"></Input>
            </Col>
            <Col span={8}>
              <Input addonBefore="运行状态"></Input>
            </Col>
          </Row>
        </PageHeader>
      </div>
      <Card hoverable={true} bordered={false} style={{ marginTop: 12 }} onClick={()=>{history.push('/workspace/job/detail')}}>
        <Typography.Title level={5}>
          <Space>
            flink_cdc
            <Tag color="#108ee9">运行中</Tag>
            <Tag color="#108ee9">JAR</Tag>
          </Space>
        </Typography.Title>
        <Space split={<Divider type="vertical" />}>
          <Typography.Text type="secondary">作业版本:12</Typography.Text>
          <Typography.Text type="secondary">创建时间:2022-10-10 12:13:22</Typography.Text>
          <Typography.Text type="secondary">责任人:张三</Typography.Text>
        </Space>
      </Card>
      <Card hoverable={true} bordered={false} style={{ marginTop: 12 }}>
        <Typography.Title level={5}>
          <Space>
            flink_cdc
            <Tag color="#108ee9">运行中</Tag>
            <Tag color="#108ee9">JAR</Tag>
          </Space>
        </Typography.Title>
        <Space split={<Divider type="vertical" />}>
          <Typography.Text type="secondary">创建时间:2022-10-10 12:13:22</Typography.Text>
          <Typography.Text type="secondary">作业状态:草稿</Typography.Text>
          <Typography.Text type="secondary">版本号:12</Typography.Text>
          <Typography.Text type="secondary">责任人:张三</Typography.Text>
        </Space>
      </Card>
      <Card hoverable={true} bordered={false} style={{ marginTop: 12 }}>
        <Typography.Title level={5}>
          <Space>
            flink_cdc
            <Tag color="#108ee9">运行中</Tag>
            <Tag color="#108ee9">JAR</Tag>
          </Space>
        </Typography.Title>
        <Space split={<Divider type="vertical" />}>
          <Typography.Text type="secondary">作业版本:12</Typography.Text>
          <Typography.Text type="secondary">创建时间:2022-10-10 12:13:22</Typography.Text>
          <Typography.Text type="secondary">责任人:张三</Typography.Text>
        </Space>
      </Card>
      <Card bordered={false} style={{ marginTop: 12, textAlign: 'right' }}>
        <Pagination size="small" total={85} showSizeChanger showQuickJumper />
      </Card>
    </>
  );
};
export default JobListView;
