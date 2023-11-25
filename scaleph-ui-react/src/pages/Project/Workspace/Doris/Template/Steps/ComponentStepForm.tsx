import React from "react";
import {Form} from "antd";
import {ProForm} from "@ant-design/pro-components";
import DorisFeComponent from "@/pages/Project/Workspace/Doris/Template/Steps/Component/DorisFeComponent";

const DorisTemplateComponent: React.FC = () => {
  const [form] = Form.useForm()

  return (
    <ProForm
      form={form}
      grid={true}
      submitter={false}
    >
      <DorisFeComponent/>
    </ProForm>
  );
}

export default DorisTemplateComponent;
