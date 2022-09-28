import JobLogTable from "@/pages/DEV/Job/Detail/components/JobLogTable";
import JobInstanceTable from "@/pages/DEV/Job/Detail/components/JobInstanceTable";

const JobHistoryWeb: React.FC<{ flinkJobCode: number }> = ({flinkJobCode}) => {

  return (<div>
    <JobInstanceTable flinkJobCode={flinkJobCode}/>
    <JobLogTable flinkJobCode={flinkJobCode}/>
  </div>);
}

export default JobHistoryWeb;
