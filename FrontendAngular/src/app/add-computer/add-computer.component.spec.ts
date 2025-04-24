import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddComputerComponent} from './add-computer.component';

describe('AddComputerComponent', () => {
  let component: AddComputerComponent;
  let fixture: ComponentFixture<AddComputerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddComputerComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddComputerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
