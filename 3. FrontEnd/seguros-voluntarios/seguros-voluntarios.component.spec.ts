import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SegurosVoluntariosComponent } from './seguros-voluntarios.component';

describe('SegurosVoluntariosComponent', () => {
  let component: SegurosVoluntariosComponent;
  let fixture: ComponentFixture<SegurosVoluntariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SegurosVoluntariosComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SegurosVoluntariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
