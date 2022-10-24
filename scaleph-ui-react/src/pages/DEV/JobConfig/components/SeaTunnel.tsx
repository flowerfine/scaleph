import {ProCard, ProFormCascader} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {WORKSPACE_CONF} from "@/constant";
import {DirectoryService} from "@/services/project/directory.service";
import { DefaultOptionType } from "antd/lib/cascader";
import {DiDirectoryTreeNode} from "@/services/project/typings";

interface Option {
  value: string;
  label: string;
  children?: Option[];
  isLeaf?: boolean;
  loading?: boolean;
  disabled?: boolean;
}

const SeaTunnelOptions: React.FC = () => {
  const intl = useIntl();

  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId) + '';

  const loadData = (selectedOptions: DefaultOptionType[]) => {
    console.log('selectedOptions', selectedOptions)
  }

  const buildOption = (dir: DiDirectoryTreeNode) => {
    return {value: dir.id, label: dir.directoryName, isLeaf: dir.children ? false : true, item: dir}
  }

  return (
    <ProCard
      title={intl.formatMessage({id: 'pages.dev.job.seatunnel'})}
      headerBordered
      collapsible={true}>
      <ProFormCascader
        name="seatunnel"
        label={(intl.formatMessage({id: 'pages.dev.job.seatunnel'}))}
        colProps={{span: 21, offset: 1}}
        fieldProps={{
          multiple: false,
          showSearch: false,
          changeOnSelect: true,
          loadData: loadData
        }}
        request={(params) => {
          return DirectoryService.listProjectDir(projectId).then((data) => {
            return data.map(buildOption)
          });
        }}
      />
    </ProCard>);
}

export default SeaTunnelOptions;
