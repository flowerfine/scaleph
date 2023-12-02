import React, {useEffect} from "react";
import {ProCard} from "@ant-design/pro-components";
import DorisFeComponent from "@/pages/Project/Workspace/Doris/Template/Steps/Component/DorisFeComponent";
import DorisAdminUser from "@/pages/Project/Workspace/Doris/Template/Steps/Component/DorisAdminUser";
import DorisBeComponent from "@/pages/Project/Workspace/Doris/Template/Steps/Component/DorisBeComponent";
import DorisCnComponent from "@/pages/Project/Workspace/Doris/Template/Steps/Component/DorisCnComponent";
import {connect} from "umi";
import {Form} from "antd";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";

const DorisInstanceComponent: React.FC = (props: any) => {
  const form = Form.useFormInstance()

  useEffect(() => {
    if (props.dorisInstanceSteps.instance) {
      form.setFieldsValue(WsDorisTemplateService.parseData({...props.dorisInstanceSteps.instance}))
    }
  }, [props.dorisInstanceSteps.instance]);

  return (
    <ProCard>
      <DorisAdminUser/>
      <DorisFeComponent/>
      <DorisBeComponent/>
      <DorisCnComponent/>
    </ProCard>
  );
}

const mapModelToProps = ({dorisInstanceSteps}: any) => ({dorisInstanceSteps})
export default connect(mapModelToProps)(DorisInstanceComponent);
