import React, {useState} from "react";
import {ProCard, ProList} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";

const defaultData = [
  {
    id: '1',
    name: 'FE地址',
    content: '我是一条地址',
  },
  {
    id: '2',
    name: 'BE地址',
    content: '我是一条地址',
  },
  {
    id: '3',
    name: 'CN地址',
    content: '我是一条地址',
  },
  {
    id: '4',
    name: 'Broker地址',
    content: '我是一条地址',
  },
];

type DataItem = (typeof defaultData)[number];

const DorisInstanceDetailAccess: React.FC = (props: any) => {
  const intl = useIntl();

  const [dataSource, setDataSource] = useState<DataItem[]>(defaultData);

  return (
    <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.access'})}
                   direction={'row'}>
      <ProCard bordered>
        <ProList<DataItem>
          rowKey="id"
          dataSource={dataSource}
          showActions="hover"
          split
          onDataSourceChange={setDataSource}
          metas={{
            title: {
              dataIndex: 'name',
            },
            content: {
              dataIndex: 'content',
            }
          }}
        />
      </ProCard>
    </ProCard.Group>
  );
}

const mapModelToProps = ({dorisInstanceDetail}: any) => ({dorisInstanceDetail})
export default connect(mapModelToProps)(DorisInstanceDetailAccess);
