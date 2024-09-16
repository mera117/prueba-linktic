import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryRangosComponent } from './history-rangos.component';

describe('HistoryRangosComponent', () => {
  let component: HistoryRangosComponent;
  let fixture: ComponentFixture<HistoryRangosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoryRangosComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoryRangosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
