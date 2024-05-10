import React from 'react';
import {ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {getIntl, getLocale} from "@umijs/max";
import {CommonListParams} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/constant";
import {Props} from "@/typings";

const CommonListItem: React.FC<Props<string>> = ({data}) => {
  const intl = getIntl(getLocale());

  return (
    <ProFormList
      name={data + CommonListParams.commonList}
      copyIconProps={false}
      creatorButtonProps={{
        creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.commonList'}),
        type: 'text',
      }}
    >
      <ProFormGroup>
        <ProFormText
          name={data + CommonListParams.commonListItem}
          colProps={{span: 23, offset: 1}}
        />
      </ProFormGroup>
    </ProFormList>
  );
}

export default CommonListItem;
