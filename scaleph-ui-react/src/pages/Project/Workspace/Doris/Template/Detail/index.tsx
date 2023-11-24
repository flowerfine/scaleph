import {useIntl, useLocation} from "umi";
import React from "react";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";

const DorisTemplateDetailWeb: React.FC = () => {
  const intl = useIntl();
  const data = useLocation().state as WsFlinkKubernetesTemplate

  return (
    <div>DorisTemplateDetailWeb</div>
  );
}

export default DorisTemplateDetailWeb;
