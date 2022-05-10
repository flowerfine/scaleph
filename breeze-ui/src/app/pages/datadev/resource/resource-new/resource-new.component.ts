import { DOCUMENT } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ViewChild, Inject, ElementRef, Component, OnInit, Input } from '@angular/core';
import { IFileOptions, IUploadOptions, SingleUploadComponent } from 'ng-devui';
import { USER_AUTH } from 'src/app/@core/data/app.data';
import { ResourceFileService } from 'src/app/@core/services/datadev/resource.service';

@Component({
  selector: 'app-resource-new',
  templateUrl: './resource-new.component.html',
  styleUrls: ['../resource.component.scss'],
})
export class ResourceNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('singleupload', { static: true }) singleupload: SingleUploadComponent;
  fileOptions: IFileOptions = {
    multiple: false,
  };
  projectId: string = '';
  fileName: string = '';
  uploadedFiles: Array<Object> = [];
  uploadOptions: IUploadOptions = {
    uri: '',
  };
  successFlag = false;
  constructor(
    private elr: ElementRef,
    private resourceService: ResourceFileService,
    private http: HttpClient,
    @Inject(DOCUMENT) private doc: any
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.projectId = this.data.projectId;
    this.uploadOptions = {
      uri: 'api/di/resource/upload',
      method: 'POST',
      maximumSize: 512,
      headers: { u_token: localStorage.getItem(USER_AUTH.token) },
      additionalParameter: { projectId: this.projectId },
      fileFieldName: 'file',
      withCredentials: true,
      responseType: 'json',
    };
  }

  submitForm(): void {
    this.resourceService.add(this.projectId, this.fileName).subscribe((d) => {
      if (d.success) {
        this.data.onClose();
        this.data.refresh();
      }
    });
  }

  onSuccess(result) {
    if (result) {
      this.fileName = result[0].file.name;
      this.successFlag = true;
    }
  }

  deleteUploadedFile(file) {
    const params: HttpParams = new HttpParams().set('projectId', this.projectId).set('fileName', file.name);
    this.http.delete('api/di/resource/upload', { params }).subscribe(() => {});
  }

  onError(error) {
    this.successFlag = false;
  }

  onFileSelect(result) {
    this.successFlag = false;
  }

  close(event) {
    //const params: HttpParams = new HttpParams().set('projectId', this.projectId).set('fileName', this.fileName);
    // this.http.delete('api/di/resource/upload', { params }).subscribe(() => {});
    this.data.onClose(event);
  }
}
