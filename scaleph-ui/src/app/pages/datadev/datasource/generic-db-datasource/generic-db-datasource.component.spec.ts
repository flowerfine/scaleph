import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericDbDatasourceComponent } from './generic-db-datasource.component';

describe('GenericDbDatasourceComponent', () => {
  let component: GenericDbDatasourceComponent;
  let fixture: ComponentFixture<GenericDbDatasourceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenericDbDatasourceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenericDbDatasourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
