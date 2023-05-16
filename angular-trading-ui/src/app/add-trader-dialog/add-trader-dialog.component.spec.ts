import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTraderDialogComponent } from './add-trader-dialog.component';

describe('AddTraderDialogComponent', () => {
  let component: AddTraderDialogComponent;
  let fixture: ComponentFixture<AddTraderDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddTraderDialogComponent]
    });
    fixture = TestBed.createComponent(AddTraderDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
