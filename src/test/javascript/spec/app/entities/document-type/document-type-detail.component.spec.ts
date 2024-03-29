import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { DocumentTypeDetailComponent } from 'app/entities/document-type/document-type-detail.component';
import { DocumentType } from 'app/shared/model/document-type.model';

describe('Component Tests', () => {
  describe('DocumentType Management Detail Component', () => {
    let comp: DocumentTypeDetailComponent;
    let fixture: ComponentFixture<DocumentTypeDetailComponent>;
    const route = ({ data: of({ documentType: new DocumentType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DocumentTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DocumentTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
