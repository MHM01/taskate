import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskerDetailComponent } from './tasker-detail.component';

describe('Tasker Management Detail Component', () => {
  let comp: TaskerDetailComponent;
  let fixture: ComponentFixture<TaskerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaskerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tasker: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TaskerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TaskerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tasker on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tasker).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
