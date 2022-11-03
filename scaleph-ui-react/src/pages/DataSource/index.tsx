import DataSourceTypeWeb from "@/pages/DataSource/components/DataSourceType";
import {Col, Row} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {useState} from "react";
import DataSourceCategoryMenu from "@/pages/DataSource/components/CategoryMenu";

const DataSourceCategoryAndTypeWeb: React.FC = () => {
  const [categoryId, setCategoryId] = useState<number | undefined>()
  return (
    <ProCard>
      <Row>
        <Col span={4}>
          <DataSourceCategoryMenu onCategoryChange={(categoryId) => setCategoryId(categoryId)}/>
        </Col>
        <Col span={20}>
          <DataSourceTypeWeb categoryId={categoryId}/>
        </Col>
      </Row>¬
    </ProCard>
  );
}

export default DataSourceCategoryAndTypeWeb;
