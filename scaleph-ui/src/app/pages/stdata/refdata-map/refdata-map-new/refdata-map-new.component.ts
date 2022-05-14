import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { DFormGroupRuleDirective, DValidateRules, FormLayout, SelectComponent } from 'ng-devui';
import { DEFAULT_PAGE_PARAM } from 'src/app/@core/data/app.data';
import { MetaDataMap, MetaDataSet, MetaDataSetType, MetaDataSetTypeParam } from 'src/app/@core/data/stdata.data';
import { RefdataService } from 'src/app/@core/services/stdata/refdata.service';

@Component({
  selector: 'app-refdata-map-new',
  templateUrl: './refdata-map-new.component.html',
  styleUrls: ['../refdata-map.component.scss'],
})
export class RefdataMapNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  @ViewChild('srcDataSetTypeSelect', { static: true }) srcDataSetTypeSelect: SelectComponent;
  @ViewChild('tgtDataSetTypeSelect', { static: true }) tgtDataSetTypeSelect: SelectComponent;

  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    srcDataSetTypeRules: {
      validators: [{ required: true }],
    },
    srcDataSetRules: {
      validators: [{ required: true }],
    },
    tgtDataSetTypeRules: {
      validators: [{ required: true }],
    },
    tgtDataSetRules: {
      validators: [{ required: true }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    srcDataSetType: null,
    srcDataSet: null,
    tgtDataSetType: null,
    tgtDataSet: null,
    remark: null,
  };
  srcDataSetList: MetaDataSet[];
  tgtDataSetList: MetaDataSet[];
  private srcDataSetTypeMap: Map<number, MetaDataSetType> = new Map();
  srcDataSetTypeSearchStr: any;
  srcDataSetTypeSearchPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  private tgtDataSetTypeMap: Map<number, MetaDataSetType> = new Map();
  tgtDataSetTypeSearchStr: any;
  tgtDataSetTypeSearchPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  constructor(private elr: ElementRef, private translate: TranslateService, private refdataService: RefdataService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({ valid }) {
    let d: MetaDataMap = {
      srcDataSetId: this.formData.srcDataSet.id,
      tgtDataSetId: this.formData.tgtDataSet.id,
      remark: this.formData.remark,
    };
    if (valid) {
      this.refdataService.addMap(d).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  srcDataSetTypeChange(event) {
    this.refdataService.listDataByType(event?.id).subscribe((d) => {
      this.srcDataSetList = d;
    });
  }

  tgtDataSetTypeChange(event) {
    this.refdataService.listDataByType(event?.id).subscribe((d) => {
      this.tgtDataSetList = d;
    });
  }
  searchSrcDataSetType = (term) => {
    if (
      this.srcDataSetTypeSearchStr === term &&
      this.srcDataSetTypeSearchPager.total > this.srcDataSetTypeSearchPager.pageSize * (this.srcDataSetTypeSearchPager.pageIndex - 1)
    ) {
      let param: MetaDataSetTypeParam = {
        pageSize: this.srcDataSetTypeSearchPager.pageSize,
        current: this.srcDataSetTypeSearchPager.pageIndex++,
        dataSetTypeCode: term,
      };

      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.srcDataSetTypeMap.set(m.id, m);
        });
        this.srcDataSetTypeSearchPager.total = d.total;
        this.srcDataSetTypeSelect.loadFinish();
      });
    } else {
      this.srcDataSetTypeSearchStr = term;
      this.srcDataSetTypeSearchPager = {
        total: 0,
        pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
        pageSize: DEFAULT_PAGE_PARAM.pageSize,
        pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
      };
      let param: MetaDataSetTypeParam = {
        pageSize: this.srcDataSetTypeSearchPager.pageSize,
        current: this.srcDataSetTypeSearchPager.pageIndex,
        dataSetTypeCode: term,
      };
      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.srcDataSetTypeMap.set(m.id, m);
        });
        this.srcDataSetTypeSearchPager.total = d.total;
        this.srcDataSetTypeSelect.loadFinish();
      });
    }
    let mdts: MetaDataSetType[] = [];
    this.srcDataSetTypeMap.forEach((option, index) => {
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

  loadMoreSrcDataSetType(data: any) {
    this.srcDataSetTypeSelect.forceSearchNext();
  }

  searchTgtDataSetType = (term) => {
    if (
      this.tgtDataSetTypeSearchStr === term &&
      this.tgtDataSetTypeSearchPager.total > this.tgtDataSetTypeSearchPager.pageSize * (this.tgtDataSetTypeSearchPager.pageIndex - 1)
    ) {
      let param: MetaDataSetTypeParam = {
        pageSize: this.tgtDataSetTypeSearchPager.pageSize,
        current: this.tgtDataSetTypeSearchPager.pageIndex++,
        dataSetTypeCode: term,
      };

      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.tgtDataSetTypeMap.set(m.id, m);
        });
        this.tgtDataSetTypeSearchPager.total = d.total;
        this.tgtDataSetTypeSelect.loadFinish();
      });
    } else {
      this.tgtDataSetTypeSearchStr = term;
      this.tgtDataSetTypeSearchPager = {
        total: 0,
        pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
        pageSize: DEFAULT_PAGE_PARAM.pageSize,
        pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
      };
      let param: MetaDataSetTypeParam = {
        pageSize: this.tgtDataSetTypeSearchPager.pageSize,
        current: this.tgtDataSetTypeSearchPager.pageIndex,
        dataSetTypeCode: term,
      };
      this.refdataService.listTypeByPage(param).subscribe((d) => {
        const list: MetaDataSetType[] = d.records;
        list.forEach((m) => {
          this.tgtDataSetTypeMap.set(m.id, m);
        });
        this.tgtDataSetTypeSearchPager.total = d.total;
        this.tgtDataSetTypeSelect.loadFinish();
      });
    }
    let mdts: MetaDataSetType[] = [];
    this.tgtDataSetTypeMap.forEach((option, index) => {
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

  loadMoreTgtDataSetType(data: any) {
    this.tgtDataSetTypeSelect.forceSearchNext();
  }
}
