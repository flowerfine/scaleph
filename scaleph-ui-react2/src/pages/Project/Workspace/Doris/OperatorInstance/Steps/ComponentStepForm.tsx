import React, {useEffect} from "react";
import {Form} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {connect} from "@umijs/max";
import DorisFeComponent from "@/pages/Project/Workspace/Doris/OperatorTemplate/Steps/Component/DorisFeComponent";
import DorisBeComponent from "@/pages/Project/Workspace/Doris/OperatorTemplate/Steps/Component/DorisBeComponent";
import DorisCnComponent from "@/pages/Project/Workspace/Doris/OperatorTemplate/Steps/Component/DorisCnComponent";
import {WsDorisOperatorTemplateService} from "@/services/project/WsDorisOperatorTemplateService";

const DorisInstanceComponent: React.FC = (props: any) => {
  const form = Form.useFormInstance()

  useEffect(() => {
    if (props.dorisInstanceSteps.instance) {
      form.setFieldsValue(WsDorisOperatorTemplateService.parseData({...props.dorisInstanceSteps.instance}))
    }
  }, [props.dorisInstanceSteps.instance]);

  return (
    <ProCard>
      <DorisFeComponent/>
      <DorisBeComponent/>
      <DorisCnComponent/>
    </ProCard>
  );
}

const mapModelToProps = ({dorisInstanceSteps}: any) => ({dorisInstanceSteps})
export default connect(mapModelToProps)(DorisInstanceComponent);
