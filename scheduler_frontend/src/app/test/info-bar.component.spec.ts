import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoBarComponent } from '../dashboard/info-bar/info-bar.component';

describe('InfoBarComponent', () => {
  let component: InfoBarComponent;
  let fixture: ComponentFixture<InfoBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InfoBarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InfoBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
