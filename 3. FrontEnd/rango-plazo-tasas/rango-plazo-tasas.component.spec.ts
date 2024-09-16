import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RangoPlazoTasasComponent } from './rango-plazo-tasas.component';

describe('RangoPlazoTasasComponent', () => {
  let component: RangoPlazoTasasComponent;
  let fixture: ComponentFixture<RangoPlazoTasasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RangoPlazoTasasComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RangoPlazoTasasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
