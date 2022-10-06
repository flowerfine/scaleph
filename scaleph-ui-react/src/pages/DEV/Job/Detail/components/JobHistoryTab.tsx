import JobLogTable from "@/pages/DEV/Job/Detail/components/JobLogTable";

const JobHistoryWeb: React.FC<{ flinkJobCode: number }> = ({flinkJobCode}) => {

  return (<div>
    <JobLogTable flinkJobCode={flinkJobCode}/>
  </div>);
}

export default JobHistoryWeb;
