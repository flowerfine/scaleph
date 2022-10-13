import {ProDescriptions} from "@ant-design/pro-components";
import {FlinkJobInstance} from "@/pages/DEV/Job/typings";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";

const JobOverviewWeb: React.FC<{
  data: FlinkJobInstance
}> = ({data}) => {

  const columns: ProDescriptionsItemProps<FlinkJobInstance>[] = [
    {
      title: 'jobId',
      key: 'jobId',
      dataIndex: 'jobId',
    },
    {
      title: 'jobName',
      key: 'jobName',
      dataIndex: 'jobName',
    },
    {
      title: 'jobState',
      key: 'jobState',
      render: (dom, entity, index, action, schema) => {
        return entity.jobState?.label
      }
    },
    {
      title: 'startTime',
      key: 'startTime',
      dataIndex: 'startTime',
    },
    {
      title: 'endTime',
      key: 'endTime',
      dataIndex: 'endTime',
    },
    {
      title: 'duration',
      key: 'duration',
      dataIndex: 'duration',
    },
  ]

  return (<ProDescriptions
    column={1}
    dataSource={data}
    columns={columns}>

  </ProDescriptions>);
}

export default JobOverviewWeb;
