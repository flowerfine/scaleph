import {ActionType, ProFormInstance, ProList, ProListMetas} from "@ant-design/pro-components";
import {DsCategory} from "@/services/datasource/typings";
import {DsCategoryService} from "@/services/datasource/category.service";
import {useIntl} from "umi";
import {useRef} from "react";

const DataSourceCategoryWeb: React.FC<{ onCategoryChange: (id: number) => void }> = ({onCategoryChange}) => {
  const intl = useIntl();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const metas: ProListMetas<DsCategory> = {
    title: {
      dataIndex: "name"
    },
    description: {
      dataIndex: "remark"
    }
  }

  return (
    <ProList<DsCategory>
      search={false}
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      metas={metas}
      request={(params, sorter, filter) => DsCategoryService.list()}
      toolbar={[]}
      pagination={false}
      onRow={(record) => {
        return {
          onClick: () => onCategoryChange(record.id)
        };
      }}
    />
  );
}

export default DataSourceCategoryWeb;
