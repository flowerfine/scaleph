import {ActionType, ProFormInstance, ProList, ProListMetas} from "@ant-design/pro-components";
import {DsType} from "@/services/datasource/typings";
import {useIntl} from "umi";
import {useEffect, useRef} from "react";
import {DsCategoryService} from "@/services/datasource/category.service";
import {Button, Image} from "antd";

const DataSourceTypeWeb: React.FC<{ categoryId?: number }> = ({categoryId}) => {
  const intl = useIntl();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    actionRef.current?.reload(true)
  }, [categoryId])

  const metas: ProListMetas<DsType> = {
    content: {
      dataIndex: "logo",
      render: (dom, entity, index, action, schema) => {
        return <Image preview={false} src={entity.logo}></Image>
      }
    }
  }

  return (
    <ProList
      toolbar={{
        search: {
          onSearch: (value: string) => {
            alert(value);
          }
        }
      }}
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      metas={metas}
      request={(params, sort, filter) => {
        return DsCategoryService.listTypes({...params, categoryId: categoryId})
      }}
      pagination={false}
      grid={{gutter: 8, column: 6}}
    />);
}

export default DataSourceTypeWeb;
