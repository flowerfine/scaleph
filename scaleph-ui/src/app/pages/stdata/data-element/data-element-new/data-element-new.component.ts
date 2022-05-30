import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { DFormGroupRuleDirective, DValidateRules, FormLayout, SelectComponent } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, Dict, DICT_TYPE } from 'src/app/@core/data/app.data';
import { MetaDataElement, MetaDataSetType, MetaDataSetTypeParam } from 'src/app/@core/data/stdata.data';
import { RefdataService } from 'src/app/@core/services/stdata/refdata.service';
import { DataElementService } from 'src/app/@core/services/stdata/data-element.service';
import { SysDictDataService } from 'src/app/@core/services/admin/dict-data.service';

@Component({
  selector: 'app-data-element-new',
  templateUrl: './data-element-new.component.html',
  styleUrls: ['../data-element.component.scss'],
})
export class DataElementNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  @ViewChild('dataSetTypeSelect', { static: true }) dataSetTypeSelect: SelectComponent;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    elementCodeRules: {
      validators: [
        { required: true },
        { maxlength: 32 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    elementNameRules: {
      validators: [{ required: true }, { maxlength: 500 }],
    },
    dataTypeRules: {
      validators: [{ required: true }],
    },
    dataDefaultRules: {
      validators: [{ maxlength: 500 }],
    },
  };

  formData = {
    elementCode: null,
    elementName: null,
    dataType: null,
    dataLength: 0,
    dataPrecision: 0,
    dataScale: 0,
    nullable: null,
    dataDefault: null,
    lowValue: null,
    highValue: null,
    dataSetType: null,
  };
  nullableList: Dict[] = [];
  dataTypeList: Dict[] = [];
  private metaDataSetTypeMap: Map<number, MetaDataSetType> = new Map();
  metaDataSetTypeSearchStr: any;
  metaDataSetTypeSearchPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private refdataService: RefdataService,
    private dictDataService: SysDictDataService,
    private metaElementService: DataElementService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.yesNo).subscribe((d) => {
      this.nullableList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.dataType).subscribe((d) => {
      this.dataTypeList = d;
    });
  }

  submitForm({ valid }) {
    let d: MetaDataElement = this.formData;
    if (valid) {
      this.metaElementService.add(d).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  searchDataSetType = (term) => {
    if (
      this.metaDataSetTypeSearchStr === term &&
      this.metaDataSetTypeSearchPager.total > this.metaDataSetTypeSearchPager.pageSize * (this.metaDataSetTypeSearchPager.pageIndex - 1)
    ) {
      let param: MetaDataSetTypeParam = {
        pageSize: this.metaDataSetTypeSearchPager.pageSize,
        current: this.metaDataSetTypeSearchPager.pageIndex++,
        dataSetTypeCode: term,
      };

      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.metaDataSetTypeMap.set(m.id, m);
        });
        this.metaDataSetTypeSearchPager.total = d.total;
        this.dataSetTypeSelect.loadFinish();
      });
    } else {
      this.metaDataSetTypeSearchStr = term;
      this.metaDataSetTypeSearchPager = {
        total: 0,
        pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
        pageSize: DEFAULT_PAGE_PARAM.pageSize,
        pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
      };
      let param: MetaDataSetTypeParam = {
        pageSize: this.metaDataSetTypeSearchPager.pageSize,
        current: this.metaDataSetTypeSearchPager.pageIndex,
        dataSetTypeCode: term,
      };
      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.metaDataSetTypeMap.set(m.id, m);
        });
        this.metaDataSetTypeSearchPager.total = d.total;
        this.dataSetTypeSelect.loadFinish();
      });
    }
    let mdts: MetaDataSetType[] = [];
    this.metaDataSetTypeMap.forEach((option, index) => {
      mdts.push(option);
    });
    return of(
      mdts
        .map((option, index) => ({ id: option.id, option: option }))
        .filter((item) => {
          return (
            item.option.dataSetTypeCode.toLowerCase().indexOf(term.toLowerCase()) !== -1 ||
            item.option.dataSetTypeName.toLowerCase().indexOf(term.toLowerCase()) !== -1
          );
        })
    );
  };

  loadMoreDataSetType(data: any) {
    this.dataSetTypeSelect.forceSearchNext();
  }
}
