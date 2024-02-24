import React from "react";
import {ProCard, ProFormDigit, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";

const AdvancedPeriodicSavepoint: React.FC = () => {
  return (<ProCard
    title={'Periodic Savepoint'}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormGroup>
      <ProFormText
        name="kubernetes.operator.periodic.savepoint.interval"
        label={'kubernetes.operator.periodic.savepoint.interval'}
        colProps={{span: 10, offset: 1}}
        help={"Interval at which periodic savepoints will be triggered."}
      />
      <ProFormText
        name="kubernetes.operator.savepoint.trigger.grace-period"
        label={'kubernetes.operator.savepoint.trigger.grace-period'}
        colProps={{span: 10, offset: 1}}
        help={"The interval before a savepoint trigger attempt is marked as unsuccessful."}
      />
      <ProFormSelect
        name="kubernetes.operator.savepoint.format.type"
        label={"kubernetes.operator.savepoint.format.type"}
        colProps={{span: 10, offset: 1}}
        options={["CANONICAL", "NATIVE"]}
        help={"Type of the binary format in which a savepoint should be taken."}
      />
      <ProFormText
        name="kubernetes.operator.savepoint.history.max.age"
        label={'kubernetes.operator.savepoint.history.max.age'}
        colProps={{span: 10, offset: 1}}
        help={"Maximum age for savepoint history entries to retain. Due to lazy clean-up, the most recent savepoint may live longer than the max age."}
      />
      <ProFormDigit
        name="kubernetes.operator.savepoint.history.max.count"
        label={'kubernetes.operator.savepoint.history.max.count'}
        colProps={{span: 10, offset: 1}}
        initialValue={10}
        help={"Maximum number of savepoint history entries to retain."}
      />
    </ProFormGroup>
  </ProCard>);
}

export default AdvancedPeriodicSavepoint;
