import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, NodeIndexOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {history, useAccess, useIntl} from "@umijs/max";
import {WORKSPACE_CONF} from "@/constants/constant";

const DataServiceConfigWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<WsDorisOperatorTemplate[]>([]);
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);


  return (<div>
    数据服务配置
  </div>);
}

export default DataServiceConfigWeb;
