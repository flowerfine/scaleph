import React, {useEffect} from "react";
import {Divider} from "antd";
import {PageContainer} from "@ant-design/pro-components";
import {connect, useIntl, useLocation} from "@umijs/max";
import {WsDorisOperatorInstance} from "@/services/project/typings";
import DorisInstanceDetailComponent
  from "./DorisInstanceComponent";
import DorisInstanceDetailYAML from "./YAML";
import DorisInstanceDetailAction from "./DorisInstanceAction";
import DorisInstanceDetailAccess from "./DorisInstanceAccess";

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
