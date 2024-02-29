import {connect, useIntl, useLocation} from "umi";
import React, {useEffect} from "react";
import {WsDorisOperatorInstance} from "@/services/project/typings";
import {PageContainer} from "@ant-design/pro-components";
import {Divider} from "antd";
import DorisInstanceDetailComponent
  from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/DorisInstanceComponent";
import DorisInstanceDetailYAML from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/YAML";
import DorisInstanceDetailAction from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/DorisInstanceAction";
import DorisInstanceDetailAccess from "@/pages/Project/Workspace/Doris/OperatorInstance/Detail/DorisInstanceAccess";

const DorisInstanceDetailWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const data = useLocation().state as WsDorisOperatorInstance

  useEffect(() => {
    refreshJob(data.id)
    let timer = setInterval(() => {
      refreshJob(data.id)
    }, 10 * 1000);
    return () => {
      clearInterval(timer);
    };
  }, []);

  const refreshJob = (id: number) => {
    props.dispatch({
      type: 'dorisInstanceDetail/editInstance',
      payload: id
    })
  }

  return (
    <PageContainer title={intl.formatMessage({id: 'pages.project.doris.instance.detail'})}>
      <DorisInstanceDetailAction data={data}/>
      <Divider type={'horizontal'}/>
      <DorisInstanceDetailComponent data={data}/>
      <Divider type={'horizontal'}/>
      <DorisInstanceDetailAccess data={data}/>
      <Divider type={'horizontal'}/>
      <DorisInstanceDetailYAML data={data}/>
    </PageContainer>
  );
}

const mapModelToProps = ({dorisInstanceDetail}: any) => ({dorisInstanceDetail})
export default connect(mapModelToProps)(DorisInstanceDetailWeb);
