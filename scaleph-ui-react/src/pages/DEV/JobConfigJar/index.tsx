import {useAccess, useIntl} from "@@/exports";
import {useRef, useState} from "react";
import {ActionType, ProFormInstance} from "@ant-design/pro-components";
import {FlinkJobConfigJar} from "@/services/dev/typings";

const JobConfigJarWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkJobConfigJar[]>([]);
  return (<div>Job Config for Jar Artifact</div>);
}

export default JobConfigJarWeb;
